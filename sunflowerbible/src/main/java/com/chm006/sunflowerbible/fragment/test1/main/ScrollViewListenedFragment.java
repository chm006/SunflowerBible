package com.chm006.sunflowerbible.fragment.test1.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.library.utils.DensityUtil;
import com.chm006.library.utils.my_utils.ViewAlphaAnimationUtil;
import com.chm006.library.widget.ListenedScrollView;
import com.chm006.sunflowerbible.R;

/**
 * 滚动监听的ScrollView（title渐隐渐显）
 * Created by chenmin on 2016/9/9.
 */
public class ScrollViewListenedFragment extends BaseBackFragment {

    private int down = 0, up = 500;
    private Toolbar toolbar;
    private Toolbar toolbar2;

    public static ScrollViewListenedFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        ScrollViewListenedFragment fragment = new ScrollViewListenedFragment();
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
        return R.layout.fragment_scroll_view_listened;
    }

    @Override
    public void init() {
        initToolbar();
        initListenedScrollView();
    }

    private void initListenedScrollView() {
        ListenedScrollView scroll_view = (ListenedScrollView) rootView.findViewById(R.id.listenedScrollView);
        scroll_view.setOnScrollListener(new ListenedScrollView.OnScrollListener() {

            @Override
            public void onBottomArrived() {
            }

            @Override
            public void onScrollStateChanged(ListenedScrollView view, int scrollState) {
            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                synchronized (ScrollViewListenedFragment.class) {
                    if (oldt >= 0 && oldt < DensityUtil.dp2px(getContext(), 500) && (up == 500)) {
                        down = 500;
                        up = 0;
                        toolbar2.setBackgroundResource(R.color.red);
                        ViewAlphaAnimationUtil.setHideAnimation(toolbar,1000);
                        ViewAlphaAnimationUtil.setShowAnimation(toolbar2,1000);
                    } else if (oldt >= DensityUtil.dp2px(getContext(), 500) && oldt < DensityUtil.dp2px(getContext(), 1000) && (down == 500 || up == 1000)) {
                        down = 1000;
                        up = 500;
                        toolbar.setBackgroundResource(R.color.orange);
                        ViewAlphaAnimationUtil.setHideAnimation(toolbar2,1000);
                        ViewAlphaAnimationUtil.setShowAnimation(toolbar,1000);
                    } else if (oldt >= DensityUtil.dp2px(getContext(), 1000) && oldt < DensityUtil.dp2px(getContext(), 1500) && (down == 1000 || up == 1500)) {
                        down = 1500;
                        up = 1000;
                        toolbar2.setBackgroundResource(R.color.yellow);
                        ViewAlphaAnimationUtil.setHideAnimation(toolbar,1000);
                        ViewAlphaAnimationUtil.setShowAnimation(toolbar2,1000);
                    } else if (oldt >= DensityUtil.dp2px(getContext(), 1500) && oldt < DensityUtil.dp2px(getContext(), 2000) && (down == 1500 || up == 2000)) {
                        down = 2000;
                        up = 1500;
                        toolbar.setBackgroundResource(R.color.green);
                        ViewAlphaAnimationUtil.setHideAnimation(toolbar2,1000);
                        ViewAlphaAnimationUtil.setShowAnimation(toolbar,1000);
                    } else if (oldt >= DensityUtil.dp2px(getContext(), 2000) && oldt < DensityUtil.dp2px(getContext(), 2500) && (down == 2000 || up == 2500)) {
                        down = 2500;
                        up = 2000;
                        toolbar2.setBackgroundResource(R.color.blue);
                        ViewAlphaAnimationUtil.setHideAnimation(toolbar,1000);
                        ViewAlphaAnimationUtil.setShowAnimation(toolbar2,1000);
                    } else if (oldt >= DensityUtil.dp2px(getContext(), 2500) && oldt < DensityUtil.dp2px(getContext(), 3000) && (down == 2500 || up == 3000)) {
                        down = 3000;
                        up = 2500;
                        toolbar.setBackgroundResource(R.color.indigo);
                        ViewAlphaAnimationUtil.setHideAnimation(toolbar2,1000);
                        ViewAlphaAnimationUtil.setShowAnimation(toolbar,1000);
                    } else if (oldt >= DensityUtil.dp2px(getContext(), 3000) && oldt < DensityUtil.dp2px(getContext(), 3500) && (down == 3000)) {
                        down = 3500;
                        up = 3000;
                        toolbar2.setBackgroundResource(R.color.purple);
                        ViewAlphaAnimationUtil.setHideAnimation(toolbar,1000);
                        ViewAlphaAnimationUtil.setShowAnimation(toolbar2,1000);
                    }
                }
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar2 = (Toolbar) rootView.findViewById(R.id.toolbar2);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
        initToolbarNav(toolbar2, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }
}
