package com.chm006.sunflowerbible.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chm006.library.adapter.BaseRVAdapter;
import com.chm006.library.base.fragment.BaseMainFragment;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.fragment.home.five_in_a_row.view.fragment.FiveInARowFragment;
import com.chm006.sunflowerbible.fragment.home.girls.GirlEnlargedFragment;
import com.chm006.sunflowerbible.fragment.home.girls.GirlsFragment;
import com.chm006.sunflowerbible.fragment.login.LoginWelcomeFragment;
import com.chm006.sunflowerbible.fragment.test1.main.bean.GirlsBean;
import com.chm006.sunflowerbible.http.RemoteHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenmin on 2016/9/10.
 */
public class HomeFragment extends BaseMainFragment {

    private List<ItemBean> data = new ArrayList<>();
    private String url = null;
    private BaseRVAdapter adapter;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void init() {
        initToolbar();
        initRecyclerView();
        initData();
    }

    private void initData() {
        data.add(new ItemBean("五子棋", R.mipmap.five_in_a_row));
        RemoteHelper.create().getGirls("福利", 1, 1)
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
                            url = resultsEntity.getUrl();
                        }
                        if (url != null) {
                            data.add(new ItemBean("Girls", url));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new BaseRVAdapter(getActivity(), data) {
            @Override
            public int onCreateViewLayoutID(int viewType) {
                return R.layout.item_home;
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {
                ItemBean itemBean = data.get(position);
                holder.getTextView(R.id.item_textview).setText(itemBean.name);
                Glide.with(getActivity())
                        .load(itemBean.image)
                        .into(holder.getImageView(R.id.item_imageview));
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        start(FiveInARowFragment.newInstance(data.get(position).name));
                        break;
                    case 1:
                        start(GirlsFragment.newInstance(data.get(position).name));
                        break;
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_menu_white_24dp, "首页");
    }

    class ItemBean {
        String name;
        Object image;
        ItemBean(String name, Object imageId) {
            this.name = name;
            this.image = imageId;
        }
    }
}
