package com.chm006.sunflowerbible.fragment.test1.map;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chm006.library.adapter.BaseRVAdapter;
import com.chm006.library.base.BaseApplication;
import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.library.utils.LogUtil;
import com.chm006.library.utils.StringUtil;
import com.chm006.library.utils.ToastUtil;
import com.chm006.library.widget.IconCenterEditText;
import com.chm006.library.widget.SwipeRefreshLayout;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.http.Consts;

import java.util.ArrayList;
import java.util.List;

/**
 * 百度地图
 * Created by chenmin on 2016/9/9.
 */
public class BaiduMapListFragment extends BaseBackFragment implements BDLocationListener, SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {

    private List<PoiInfo> allPoi = new ArrayList<>();//百度地图搜索结果展示列表
    private List<Boolean> checked_list = new ArrayList<>();//CheckBox选择的状态
    private int pageNum = 0;//地图检索分页
    private String search;//查找关键词
    private double longitude, latitude;
    private String province, city, district, street, addrStr;

    private PoiSearch poiSearch;//百度地图检索
    private PoiCitySearchOption poiCitySearchOption;
    private GeoCoder geoCoder;//反地理编码
    private LocationClient mLocClient;//定位

    private MyRVAdapter adapter;
    private IconCenterEditText iconCenterEditText;//仿ios输入框：EditText
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新，上拉加载
    private RelativeLayout relativeLayout2;//ProgressBar加载进度条（玩命加载中...）

    public static BaiduMapListFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        BaiduMapListFragment fragment = new BaiduMapListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(ARG_TITLE);
        }
        startLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放地理编码检索实例
        geoCoder.destroy();
        // 释放百度地图检索
        poiSearch.destroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_baidu_map_list;
    }

    @Override
    public void init() {
        initToolbar();
        initView();
        initRecyclerView3SwipeRefreshLayout();
    }

    private void initRecyclerView3SwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);//下拉刷新
        swipeRefreshLayout.setOnLoadListener(this);//上拉加载
        swipeRefreshLayout.setColor(R.color.liji_material_blue_500, R.color.liji_material_red_500, R.color.orange, R.color.green);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRVAdapter(getActivity(), allPoi);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                relativeLayout2.setVisibility(View.VISIBLE);
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(allPoi.get(position).location));
            }
        });
    }

    @Override
    public void onRefresh() {
        relativeLayout2.setVisibility(View.VISIBLE);
        pageNum = 0;
        allPoi.clear();
        checked_list.clear();
        if (StringUtil.isNotEmpty(search)) {
            poiSearch.searchInCity(poiCitySearchOption.city(city).keyword(search).pageCapacity(15).pageNum(pageNum));
        } else {
            relativeLayout2.setVisibility(View.INVISIBLE);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoad() {
        relativeLayout2.setVisibility(View.VISIBLE);
        pageNum++;
        if (StringUtil.isNotEmpty(search)) {
            poiSearch.searchInCity(poiCitySearchOption.city(city).keyword(search).pageCapacity(15).pageNum(pageNum));
        } else {
            relativeLayout2.setVisibility(View.INVISIBLE);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setLoading(false);
        }
    }

    private void initView() {
        //正在玩命加载中...
        relativeLayout2 = (RelativeLayout) rootView.findViewById(R.id.relativeLayout2);
        relativeLayout2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //点击跳转到地图
        View location = rootView.findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allPoi.size() == checked_list.size()) {
                    ArrayList<PoiInfo> data = new ArrayList<PoiInfo>();
                    for (int i = 0; i < checked_list.size(); i++) {
                        if (checked_list.get(i)) {
                            data.add(allPoi.get(i));
                        }
                    }
                    if (data.size() != 0) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("data", data);
                        setFramgentResult(Consts.ResultCode.RESULT, bundle);
                        pop();
                    } else {
                        ToastUtil.showLong(getActivity(), "至少要选中一条数据");
                    }
                } else {
                    ToastUtil.showLong(getActivity(), "获取数据失败，请重新搜索");
                }
            }
        });

        //第一次创建对象
        if (poiSearch == null) {
            initPoiSearch();
        }
        // 反地理编码查询
        getReverseGeoCode();

        iconCenterEditText = findViewById(R.id.iconCenterEditText);
        iconCenterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search = s.toString();
                onRefresh();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //初始化百度地图检索
    public void initPoiSearch() {
        poiSearch = PoiSearch.newInstance();
        //百度地图检索监听
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    ToastUtil.showShort(getActivity(), "未找到结果");
                } else if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    allPoi.addAll(poiResult.getAllPoi());
                    for (int i = 0; i < poiResult.getAllPoi().size(); i++) {
                        checked_list.add(false);
                    }
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setLoading(false);
                relativeLayout2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //详情检索失败
                    ToastUtil.showShort(getActivity(), "抱歉，未能找到结果");
                } else {
                    //检索成功
                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
        poiCitySearchOption = new PoiCitySearchOption();
    }

    //百度反地理编码
    public void getReverseGeoCode() {
        // 创建地理编码检索实例
        geoCoder = GeoCoder.newInstance();
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    ToastUtil.showShort(getActivity(), "抱歉，未能找到结果");
                    relativeLayout2.setVisibility(View.INVISIBLE);
                } else {
                    allPoi.clear();
                    checked_list.clear();
                    LatLng location = result.getLocation();
                    longitude = location.longitude;//经度
                    latitude = location.latitude;//维度
                    ReverseGeoCodeResult.AddressComponent addressDetail = result.getAddressDetail();
                    province = addressDetail.province;
                    city = addressDetail.city;
                    district = addressDetail.district;
                    street = addressDetail.street;
                    iconCenterEditText.setText(province + city + district);
                }
            }

            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    ToastUtil.showShort(getActivity(), "抱歉，未能找到结果");
                }
            }
        });
    }

    //开始定位,定位初始化
    private void startLocation() {
        mLocClient = new LocationClient(BaseApplication.mApplication);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);//打开获取地址信息
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start(); // 本地定位开始, 结果在onReceiveLocation中处理
    }

    //结束定位
    private void stopLocation() {
        if (mLocClient != null) {
            mLocClient.stop();
        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        longitude = bdLocation.getLongitude();//经度
        latitude = bdLocation.getLatitude();//维度
        addrStr = bdLocation.getAddrStr();//获取详细地址信息
        province = bdLocation.getProvince();//获取省份
        city = bdLocation.getCity();//获取城市
        district = bdLocation.getDistrict();//获取区/县信息
        street = bdLocation.getStreet();//获取街道信息
        String streetNumber = bdLocation.getStreetNumber();//获取街道号码
        String cityCode = bdLocation.getCityCode();//获取城市编码
        String time = bdLocation.getTime();//server返回的当前定位时间
        LogUtil.d("经度==" + longitude +
                "\n维度==" + latitude +
                "\n详细地址信息==" + addrStr +
                "\n省份==" + province +
                "\n城市==" + city +
                "\n区/县==" + district +
                "\n街道==" + street +
                "\nserver返回的当前定位时间==" + time);
        if (StringUtil.isNotEmpty(city)) {
            stopLocation();//结束定位
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }

    class MyRVAdapter extends BaseRVAdapter {

        MyRVAdapter(Context context, List<?> list) {
            super(context, list);
        }

        @Override
        public int onCreateViewLayoutID(int viewType) {
            return R.layout.item_baidu_map;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            String tag = (String) holder.getTextView(R.id.item_map_textview).getTag();
            if (!TextUtils.equals(allPoi.get(position).name, tag)) {
                holder.getTextView(R.id.item_map_textview).setText("");
            }
            if (StringUtil.isNotEmpty(allPoi.get(position).name)) {
                holder.getTextView(R.id.item_map_textview).setTag(allPoi.get(position).name);
                holder.getTextView(R.id.item_map_textview).setText(allPoi.get(position).name);
            }
            String s = "";
            if (StringUtil.isNotEmpty(allPoi.get(position).city)) {
                s = s + allPoi.get(position).city + "\t";
            }
            if (StringUtil.isNotEmpty(allPoi.get(position).address)) {
                s = s + allPoi.get(position).address;
            }

            final CheckBox checkBox = holder.get(R.id.btn_checkbox);
            checkBox.setChecked(checked_list.get(position));

            holder.getTextView(R.id.item_map_textview2).setText(s);
            holder.get(R.id.relativeLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        checked_list.remove(position);
                        checked_list.add(position, false);
                    } else {
                        checkBox.setChecked(true);
                        checked_list.remove(position);
                        checked_list.add(position, true);
                    }
                }
            });
        }
    }
}
