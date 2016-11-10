package com.chm006.sunflowerbible.fragment.test1.pic;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chm006.library.adapter.BaseRVAdapter;
import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.library.widget.SwipeRefreshLayout;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.fragment.test1.main.bean.GirlsBean;
import com.chm006.sunflowerbible.http.RemoteHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * RecyclerView 防止图片错乱、上拉加载下拉刷新
 * Created by chenmin on 2016/9/27.
 */

public class RecyclerViewFragment extends BaseBackFragment implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {
    private List<String> pics = new ArrayList<>();
    private int page = 1;
    private int size = 10;
    private MyRVAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static RecyclerViewFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        RecyclerViewFragment fragment = new RecyclerViewFragment();
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
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void init() {
        initToolbar();
        initRecyclerView3SwipeRefreshLayout();
    }

    private void initRecyclerView3SwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);//下拉刷新
        swipeRefreshLayout.setOnLoadListener(this);//上拉加载
        swipeRefreshLayout.setColor(R.color.liji_material_blue_500, R.color.liji_material_red_500, R.color.orange, R.color.green);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRVAdapter(getActivity(), pics);
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, null);//这里不设置titleName
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMore();
            }
        }, 1000);
    }

    private void refresh() {
        page = 1;
        RemoteHelper.create().getGirls("福利", size, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GirlsBean>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);//停止刷新动画
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);//停止刷新动画
                    }

                    @Override
                    public void onNext(GirlsBean girlsBean) {
                        pics.clear();
                        for (GirlsBean.ResultsEntity resultsEntity : girlsBean.getResults()) {
                            pics.add(resultsEntity.getUrl());
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);//停止刷新动画
                    }
                });
    }

    private void loadMore() {
        RemoteHelper.create()
                .getGirls("福利", size, ++page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GirlsBean>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setLoading(false);
                    }

                    @Override
                    public void onNext(GirlsBean girlsBean) {
                        for (GirlsBean.ResultsEntity resultsEntity : girlsBean.getResults()) {
                            pics.add(resultsEntity.getUrl());
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setLoading(false);
                    }
                });
    }

    class MyRVAdapter extends BaseRVAdapter {

        MyRVAdapter(Context context, List<?> list) {
            super(context, list);
        }

        @Override
        public int onCreateViewLayoutID(int viewType) {
            return R.layout.item_imageview2;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            //imageview.setTag();getTag(); 防止图片显示错乱
            String tag = (String) holder.getImageView(R.id.item_imageview_iv2).getTag();
            if (!TextUtils.equals(pics.get(position), tag)) {
                holder.getImageView(R.id.item_imageview_iv2).setImageResource(R.mipmap.default_bg);
            }
            Glide.with(getActivity())
                    .load(pics.get(position))
                    .asBitmap()
                    .placeholder(R.mipmap.default_bg)
                    .error(R.mipmap.default_bg)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            holder.getImageView(R.id.item_imageview_iv2).setTag(pics.get(position));
                            holder.getImageView(R.id.item_imageview_iv2).setImageBitmap(resource);
                        }
                    });
            /*Glide.with(getActivity())
                    .load(pics.get(position))
                    .asBitmap()
                    .centerCrop()
                    //.fitCenter()
                    .placeholder(R.mipmap.default_bg)//请求等待时的图片
                    .error(R.mipmap.default_bg)//请求失败时的图片
                    .into(new MyBitmapImageViewTarget(holder.getImageView(R.id.item_imageview_iv)));*/
            /*Glide.with(getActivity())
                    .load(pics.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.default_bg)//请求等待时的图片
                    .error(R.mipmap.default_bg)//请求失败时的图片
                    .into(holder.getImageView(R.id.item_imageview_iv2));*/
        }
    }
}
