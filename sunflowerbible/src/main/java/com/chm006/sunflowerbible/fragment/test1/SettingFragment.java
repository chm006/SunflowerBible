package com.chm006.sunflowerbible.fragment.test1;

import android.os.Bundle;

import com.chm006.library.base.fragment.BaseFragment;
import com.chm006.sunflowerbible.R;

/**
 * 设置
 * Created by chenmin on 2016/8/12.
 */
public class SettingFragment extends BaseFragment {

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void init() {

    }

}
