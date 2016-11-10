package com.chm006.sunflowerbible.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.chm006.library.adapter.MyFragmentPagerAdapter;
import com.chm006.library.base.fragment.BaseMainFragment;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.fragment.test1.MainFragment;
import com.chm006.sunflowerbible.fragment.test1.MapFragment;
import com.chm006.sunflowerbible.fragment.test1.PicFragment;
import com.chm006.sunflowerbible.fragment.test1.SettingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chm00 on 2016/9/11.
 */
public class Test1Fragment extends BaseMainFragment {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private MyFragmentPagerAdapter adapter;

    public static Test1Fragment newInstance() {
        Bundle args = new Bundle();
        Test1Fragment fragment = new Test1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test1;
    }

    @Override
    public void init() {
        initToolbar();
        initData();
        initTabLayoutAndViewPager();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_menu_white_24dp, "Test1");
    }

    private void initData() {
        titles.add("主页");
        titles.add("地图");
        titles.add("图片");
        titles.add("设置");
        fragmentList.add(MainFragment.newInstance());
        fragmentList.add(MapFragment.newInstance());
        fragmentList.add(PicFragment.newInstance());
        fragmentList.add(SettingFragment.newInstance());
        //adapter.notifyDataSetChanged();
    }

    private void initTabLayoutAndViewPager() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.fragment_test1_vp);
        adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


/*
    private int radioGroup_number;
    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState==null)
            loadMultipleRootFragment(R.id.fragment_test1_fl, 0, fragmentList.get(0),fragmentList.get(1),fragmentList.get(2),fragmentList.get(3));
    }

    private void initViewRadio() {
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.fragment_radiogroup);
        //RadioButton切换Fragment
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fragment_rb_1:
                        // show一个Fragment，hide一个Fragment； 主要用于类似微信主页那种 切换tab的情况
                        showHideFragment(fragmentList.get(0), fragmentList.get(radioGroup_number));
                        radioGroup_number = 0;
                        break;
                    case R.id.fragment_rb_2:
                        showHideFragment(fragmentList.get(1), fragmentList.get(radioGroup_number));
                        radioGroup_number = 1;
                        break;
                    case R.id.fragment_rb_3:
                        showHideFragment(fragmentList.get(2), fragmentList.get(radioGroup_number));
                        radioGroup_number = 2;
                        break;
                    case R.id.fragment_rb_4:
                        showHideFragment(fragmentList.get(3), fragmentList.get(radioGroup_number));
                        radioGroup_number = 3;
                        break;
                }
            }
        });
    }
    */
}
