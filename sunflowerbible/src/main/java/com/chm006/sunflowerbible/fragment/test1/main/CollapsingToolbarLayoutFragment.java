package com.chm006.sunflowerbible.fragment.test1.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chm006.library.adapter.BaseRVAdapter;
import com.chm006.library.base.BaseApplication;
import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.fragment.test1.main.bean.GirlsBean;
import com.chm006.sunflowerbible.http.RemoteService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * CollapsingToolbarLayout
 * 继承至FrameLayout，提供了一个可以折叠的Toolbar，给它设置layout_scrollFlags，它可以控制包含在CollapsingToolbarLayout中的控件
 * 如：ImageView、Toolbar，在响应layout_behavior事件时作出相应的scrollFlags滚动事件(移除屏幕或固定在屏幕顶端)
 * Created by chenmin on 2016/9/9.
 */
public class CollapsingToolbarLayoutFragment extends BaseBackFragment {
    private List<String> pics = new ArrayList<>();
    private int page = 1;
    private int size = 20;
    private MyRVAdapter myRVAdapter;

    public static CollapsingToolbarLayoutFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        CollapsingToolbarLayoutFragment fragment = new CollapsingToolbarLayoutFragment();
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
        return R.layout.fragment_collapsing_toolbar_layout;
    }

    @Override
    public void init() {
        initToolbar();
        initCollapsingToolbarLayout();
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        myRVAdapter = new MyRVAdapter(getActivity(), pics);
        recyclerView.setAdapter(myRVAdapter);
        new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(BaseApplication.defaultOkHttpClient())
                .build()
                .create(RemoteService.class)
                .getGirls("福利", size, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GirlsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GirlsBean girlsBean) {
                        for (GirlsBean.ResultsEntity resultsEntity : girlsBean.getResults()) {
                            pics.add(resultsEntity.getUrl());
                        }
                        myRVAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initCollapsingToolbarLayout() {
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle(mTitle);
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.GREEN);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, null);//这里不设置titleName
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
        }
    }
}
