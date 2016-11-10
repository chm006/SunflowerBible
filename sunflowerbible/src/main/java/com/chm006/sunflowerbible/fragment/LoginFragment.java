package com.chm006.sunflowerbible.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chm006.library.base.fragment.BaseFragment;
import com.chm006.library.base.fragment.BaseMainFragment;
import com.chm006.library.utils.ToastUtil;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.fragment.login.LoginWelcomeFragment;
import com.chm006.sunflowerbible.fragment.test1.main.EditTextStyleFragment;

/**
 * Created by chm00 on 2016/9/11.
 */
public class LoginFragment extends BaseMainFragment {

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void init() {
        loadRootFragment(R.id.frameLayout, LoginWelcomeFragment.newInstance("登陆"));
        //initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_menu_white_24dp, "登陆");
    }
}
