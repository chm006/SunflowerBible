package com.chm006.sunflowerbible.fragment.test1.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.chm006.library.adapter.MyFragmentPagerAdapter;
import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.fragment.test1.main.level3linkage.Level3LinkageFragment0;
import com.chm006.sunflowerbible.fragment.test1.main.level3linkage.Level3LinkageFragment1;
import com.chm006.sunflowerbible.fragment.test1.main.level3linkage.Level3LinkageFragment2;

import java.util.ArrayList;
import java.util.List;

/**
 * 三级联动（省市区）
 * Created by chenmin on 2016/9/9.
 */
public class Level3LinkageFragment extends BaseBackFragment {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    public static Level3LinkageFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        Level3LinkageFragment fragment = new Level3LinkageFragment();
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
        return R.layout.fragment_level3linkage;
    }

    @Override
    public void init() {
        initToolbar();
        initTabLayoutAndViewPager();
    }

    private void initTabLayoutAndViewPager() {
        titles.add("省市区1");
        titles.add("省市区2");
        titles.add("时间");
        fragmentList.add(Level3LinkageFragment0.newInstance());
        fragmentList.add(Level3LinkageFragment1.newInstance());
        fragmentList.add(Level3LinkageFragment2.newInstance());

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.fragment_test1_home0_vp);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }
}
