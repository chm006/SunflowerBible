package com.chm006.sunflowerbible.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.chm006.library.base.fragment.BaseMainFragment;
import com.chm006.library.utils.AppUtil;
import com.chm006.sunflowerbible.R;

/**
 * 关于
 * Created by chenmin on 2016/10/19.
 */

public class AboutFragment extends BaseMainFragment {

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    public void init() {
        initToolbar();
        TextView textView = (TextView) rootView.findViewById(R.id.textView);
        textView.setText(TextUtils.isEmpty(AppUtil.getVersionName(getActivity()))? "": AppUtil.getVersionName(getActivity()));
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_menu_white_24dp, "关于");
    }
}
