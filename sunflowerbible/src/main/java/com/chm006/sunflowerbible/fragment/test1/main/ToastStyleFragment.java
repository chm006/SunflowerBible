package com.chm006.sunflowerbible.fragment.test1.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.sunflowerbible.R;

/**
 * Toast样式
 * Created by chenmin on 2016/9/9.
 */
public class ToastStyleFragment extends BaseBackFragment implements View.OnClickListener {

    private View fab;

    public static ToastStyleFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        ToastStyleFragment fragment = new ToastStyleFragment();
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
        return R.layout.fragment_toast_style;
    }

    @Override
    public void init() {
        initToolbar();
        initSnackbar();
    }

    private void initSnackbar() {
        fab = rootView.findViewById(R.id.fab);
        Button button1 = (Button) rootView.findViewById(R.id.button1);
        button1.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                Snackbar.make(fab, "Hello SnackBar!", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Perform anything for the action selected
                            }
                        })
                        .setActionTextColor(Color.GREEN)
                        .show();
                break;
        }
    }
}
