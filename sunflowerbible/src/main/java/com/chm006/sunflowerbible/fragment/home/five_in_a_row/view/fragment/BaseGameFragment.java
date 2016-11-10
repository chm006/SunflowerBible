package com.chm006.sunflowerbible.fragment.home.five_in_a_row.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chm006.library.base.fragment.BaseFragment;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.fragment.home.five_in_a_row.EventBus.BusProvider;


/**
 * Created by Xuf on 2016/1/23.
 */
public class BaseGameFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game;
    }

    @Override
    public void init() {

    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
