package com.chm006.sunflowerbible.fragment.test1.pic;

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
public class PhotoViewFragment extends BaseBackFragment {
    private List<String> pics = new ArrayList<>();

    public static PhotoViewFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        PhotoViewFragment fragment = new PhotoViewFragment();
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
        return R.layout.fragment_photo_view;
    }

    @Override
    public void init() {
        initToolbar();
        initData();
        initPhotoView();
    }

    private void initPhotoView() {
        PhotoView photoView = findViewById(R.id.photoView);
        Glide.with(getActivity())
                .load(pics.get(0))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.default_bg)//请求等待时的图片
                .error(R.mipmap.default_bg)//请求失败时的图片
                .into(photoView);
    }

    private void initData() {
        pics.add("http://www.bz55.com/uploads/allimg/140919/138-140919162646.jpg");
        pics.add("http://www.bz55.com/uploads/allimg/140919/138-140919162638.jpg");
        pics.add("http://i.imgur.com/SWssO7A.gif");
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }

}