package com.chm006.sunflowerbible.fragment.test1.main.level3linkage;

import android.os.Bundle;

import com.chm006.library.base.fragment.BaseFragment;
import com.chm006.sunflowerbible.R;

/**
 *
 * Created by chenmin on 2016/9/12.
 */
public class Level3LinkageFragment2 extends BaseFragment {


    public static Level3LinkageFragment2 newInstance() {
        Bundle args = new Bundle();
        Level3LinkageFragment2 fragment = new Level3LinkageFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_level3linkage2;
    }

    @Override
    public void init() {

    }

}
