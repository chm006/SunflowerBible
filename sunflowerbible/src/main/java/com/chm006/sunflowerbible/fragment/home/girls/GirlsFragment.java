package com.chm006.sunflowerbible.fragment.home.girls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

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

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * http://gank.io/
 * 妹子图
 * Created by Administrator on 2015/12/9.
 */
public class GirlsFragment extends BaseBackFragment implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {
    private List<String> pics = new ArrayList<>();
    private int page = 1;
    private int size = 10;
    private GirlsFragment.MyRVAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static GirlsFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        GirlsFragment fragment = new GirlsFragment();
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
        return R.layout.fragment_girls;
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
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                start(GirlEnlargedFragment.newInstance(pics.get(position)));
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
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
            Glide.with(getActivity())
                    .load(pics.get(position))
                    .asBitmap()
                    //.placeholder(R.mipmap.default_bg)
                    //.error(R.mipmap.default_bg)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            //holder.getImageView(R.id.item_imageview_iv2).setTag(pics.get(position));
                            //holder.getImageView(R.id.item_imageview_iv2).setImageBitmap(resource);
                            //ViewAlphaAnimationUtil.setShowAnimation(holder.getImageView(R.id.item_imageview_iv2),300);
                            TransitionDrawable td = new TransitionDrawable(
                                    new Drawable[]{new ColorDrawable(0xFFC7EDCC),
                                            new BitmapDrawable(resource)});
                            td.setCrossFadeEnabled(true);
                            holder.getImageView(R.id.item_imageview_iv2).setImageDrawable(td);
                            td.startTransition(500);
                        }
                    });
        }
    }
}
