package com.chm006.sunflowerbible.fragment.test1.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.sunflowerbible.R;

/**
 * ProgressBar样式
 * Created by chenmin on 2016/9/9.
 */
public class ProgressBarStyleFragment extends BaseBackFragment {

    public static ProgressBarStyleFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        ProgressBarStyleFragment fragment = new ProgressBarStyleFragment();
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
        return R.layout.fragment_progress_bar_style;
    }

    @Override
    public void init() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }
}
