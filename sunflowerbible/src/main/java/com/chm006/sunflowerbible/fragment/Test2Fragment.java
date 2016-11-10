package com.chm006.sunflowerbible.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chm006.library.base.fragment.BaseMainFragment;
import com.chm006.library.utils.ToastUtil;
import com.chm006.sunflowerbible.R;

/**
 * Created by chm00 on 2016/9/11.
 */
public class Test2Fragment extends BaseMainFragment{

    public static Test2Fragment newInstance() {
        Bundle args = new Bundle();
        Test2Fragment fragment = new Test2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test2;
    }

    @Override
    public void init() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_menu_white_24dp, "Test2");
    }

}
