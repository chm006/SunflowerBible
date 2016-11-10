package com.chm006.sunflowerbible.fragment.home.girls;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.sunflowerbible.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * PhotoView图片放大缩小
 * Created by chenmin on 2016/9/9.
 */
public class GirlEnlargedFragment extends BaseBackFragment {
    private static String GIRL_URL = "girl_url";
    private String girl_url;

    public static GirlEnlargedFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(GIRL_URL, url);
        GirlEnlargedFragment fragment = new GirlEnlargedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            girl_url = bundle.getString(GIRL_URL);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo_view;
    }

    @Override
    public void init() {
        initToolbar();
        initPhotoView();
    }

    private void initPhotoView() {
        PhotoView photoView = findViewById(R.id.photoView);
        Glide.with(getActivity())
                .load(girl_url)
                //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(photoView);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, "");
    }

}