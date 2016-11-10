package com.chm006.sunflowerbible.fragment.home.five_in_a_row.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.fragment.home.five_in_a_row.util.Constants;
import com.gc.materialdesign.views.ButtonRectangle;

/**
 * Created by Administrator on 2015/12/9.
 */
public class FiveInARowFragment extends BaseBackFragment implements View.OnClickListener {

    public static FiveInARowFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        FiveInARowFragment fragment = new FiveInARowFragment();
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
        return R.layout.fragment_five_in_a_row;
    }

    @Override
    public void init() {
        initToolbar();
        initView(rootView);
    }

    private void initView(View root){
        ButtonRectangle coupeTextView = (ButtonRectangle) root.findViewById(R.id.tv_coupe_mode);
        ButtonRectangle wifiTextView = (ButtonRectangle) root.findViewById(R.id.tv_wifi_mode);
        ButtonRectangle blueToothTextView = (ButtonRectangle) root.findViewById(R.id.tv_blue_tooth_mode);

        coupeTextView.setOnClickListener(this);
        wifiTextView.setOnClickListener(this);
        blueToothTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_coupe_mode:
                start(CoupleGameFragment.newInstance());
                break;
            case R.id.tv_wifi_mode:
                start(NetGameFragment.newInstance(Constants.WIFI_MODE));
                break;
            case R.id.tv_blue_tooth_mode:
                start(NetGameFragment.newInstance(Constants.BLUE_TOOTH_MODE));
                break;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }
}
