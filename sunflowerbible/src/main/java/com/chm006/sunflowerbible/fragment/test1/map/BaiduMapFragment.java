package com.chm006.sunflowerbible.fragment.test1.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.chm006.library.base.BaseApplication;
import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.library.utils.ToastUtil;
import com.chm006.library.utils.overlayutil.OverlayManager;
import com.chm006.library.widget.IconCenterEditText;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.http.Consts;

import java.util.ArrayList;
import java.util.List;

/**
 * 百度地图
 * Created by chenmin on 2016/9/9.
 */
public class BaiduMapFragment extends BaseBackFragment implements BDLocationListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    //是否首次定位
    private boolean isFirstLoc = true;
    private LocationClient mLocClient;
    private LocationMode mCurrentMode; //定位图层显示模式 (普通-跟随-罗盘)
    private BitmapDescriptor mCurrentMarker = null; //定位图标描述
    private double longitude, latitude; //当前位置经纬度
    private CheckBox checkBox;
    private List<OverlayOptions> overlayOptions;//覆盖物集合

    public static BaiduMapFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        BaiduMapFragment fragment = new BaiduMapFragment();
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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_baidu_map;
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (requestCode == Consts.RequestCode.REQUEST && resultCode == Consts.ResultCode.RESULT) {
            checkBox.setChecked(false);//定位自己button设置false
            ArrayList<PoiInfo> selectedPoi = data.getParcelableArrayList("data");
            overlayOptions = new ArrayList<>();
            for (int i = 0; i < selectedPoi.size(); i++) {
                addMyLocation(selectedPoi.get(i).location.latitude, selectedPoi.get(i).location.longitude, selectedPoi.get(i));
            }
            OverlayManager overlayManager  = new OverlayManager(mBaiduMap){
                @Override
                public List<OverlayOptions> getOverlayOptions() {
                    return overlayOptions;
                }
                @Override
                public boolean onPolylineClick(Polyline polyline) {
                    return false;
                }
                //添加marker点击事件的监听
                @Override
                public boolean onMarkerClick(Marker marker) {
                    //从marker中获取info信息
                    Bundle bundle = marker.getExtraInfo();
                    PoiInfo poiInfo = bundle.getParcelable("info");
                    //将信息显示在界面上

                    //将布局显示出来

                    //infowindow中的布局
                    TextView tv = new TextView(getActivity());
                    tv.setBackgroundResource(R.mipmap.location_tips);
                    tv.setPadding(20, 10, 20, 20);
                    tv.setTextColor(android.graphics.Color.WHITE);
                    tv.setText(poiInfo.name);
                    //tv.setGravity(Gravity.CENTER);
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(tv);
                    //infowindow位置
                    LatLng latLng = new LatLng(poiInfo.location.latitude, poiInfo.location.longitude);
                    //infowindow点击事件
                    InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick() {
                            //隐藏infowindow
                            mBaiduMap.hideInfoWindow();
                        }
                    };
                    //显示infowindow
                    InfoWindow infoWindow = new InfoWindow(bitmapDescriptor, latLng, -47, listener);
                    mBaiduMap.showInfoWindow(infoWindow);
                    return true;
                }
            };
            overlayManager.addToMap();
            overlayManager.zoomToSpan();
            mBaiduMap.setOnMarkerClickListener(overlayManager);
        }
    }

    @Override
    public void init() {
        initToolbar();
        initMap();
        initEditText();
        initCheckBox();
    }

    private void initCheckBox() {
        //定位自己当前位置
        checkBox = (CheckBox) rootView.findViewById(R.id.btn_checkbox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16.0f));
                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude));
                    mBaiduMap.animateMapStatus(msu);
                }
                checkBox.setChecked(true);
            }
        });
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                checkBox.setChecked(false);
            }
        });
    }

    private void initEditText() {
        IconCenterEditText iconCenterEditText = (IconCenterEditText) rootView.findViewById(R.id.iconCenterEditText);
        final RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.relativeLayout);
        iconCenterEditText.setOnEditTextListener(new IconCenterEditText.OnEditTextListener() {
            @Override
            public void onEnterKeyAction(View view) {

            }

            @Override
            public void onHasFocusAction(View view) {
                //清楚地图上的覆盖物
                mBaiduMap.clear();
                //得到焦点
                startForResult(BaiduMapListFragment.newInstance("搜索列表"), Consts.RequestCode.REQUEST);
                relativeLayout.setFocusable(true);
                relativeLayout.setFocusableInTouchMode(true);
            }

            @Override
            public void onLostFocusAction(View view) {
                //失去焦点
            }
        });
    }

    private void initMap() {
        //获取地图控件引用
        mMapView = (MapView) rootView.findViewById(R.id.bmapView);
        //隐藏百度地图logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        //隐藏地图上比例尺
        //mMapView.showScaleControl(false);
        //隐藏缩放控件
        //mMapView.showZoomControls(false);
        //设置地图缩放级别16 类型普通地图
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16.0f));
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位初始化
        startLocation();
    }

    /**
     * 定位并添加标注
     */
    private void addMyLocation(double latitude, double longitude, PoiInfo info) {
        //更新
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
        //mBaiduMap.clear();
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_openmap_mark);

        //构建MarkerOption，用于在地图上添加Marker
        //new MarkerOptions().position(point1).icon(bdA).zIndex(i).draggable(true).extraInfo(bundle);
        Bundle bundle = new Bundle();
        bundle.putParcelable("info", info);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .extraInfo(bundle);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        //将地图上覆盖物添加到集合中
        overlayOptions.add(option);
    }

    //开始定位,定位初始化
    //注意: 实例化定位服务 LocationClient类必须在主线程中声明 并注册定位监听接口
    private void startLocation() {
        mLocClient = new LocationClient(BaseApplication.mApplication);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);//打开获取地址信息
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        mLocClient.setLocOption(option);//设置定位参数
        mLocClient.start(); // 本地定位开始, 结果在onReceiveLocation中处理
        mLocClient.requestLocation();
    }

    //结束定位
    private void stopLocation() {
        if (mLocClient != null) {
            mLocClient.stop();
        }
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        //mapview 销毁后不在处理新接收的位置
        if (location == null || mBaiduMap == null) {
            return;
        }
        //MyLocationData.Builder定位数据建造器
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(100)
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
        //设置定位数据
        mBaiduMap.setMyLocationData(locData);
        mCurrentMode = LocationMode.NORMAL;
        //获取经纬度
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //第一次定位的时候，那地图中心点显示为定位到的位置
        if (isFirstLoc) {
            isFirstLoc = false;
            //地理坐标基本数据结构
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            //MapStatusUpdate描述地图将要发生的变化
            //MapStatusUpdateFactory生成地图将要反生的变化
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
            mBaiduMap.animateMapStatus(msu);
            ToastUtil.showShort(getActivity(), location.getAddrStr());
        }
    }

    @Override
    public void onDestroy() {
        stopLocation();                       //退出时销毁定位
        mBaiduMap.setMyLocationEnabled(false);   //关闭定位图层
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }
}
