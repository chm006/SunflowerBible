package com.chm006.library.base.fragment;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.chm006.library.base.fragment.BaseFragment;

import me.yokeyword.fragmentation.anim.FragmentAnimator;


/**
 * 带toolbar的fragment，点击toolbar左上角弹出侧边栏（抽屉）
 * Created by YoKeyword on 16/2/3.
 */
public abstract class BaseMainFragment extends BaseFragment {
    protected static final String TAG = "Fragmentation";

    protected OnFragmentOpenDrawerListener mOpenDraweListener;

    /**
     * @param toolbar        toolbar
     * @param navigationIcon 设置NavigationIcon（Toolbar左上角图标，点击弹出侧边栏）
     * @param titleName      标题名称
     */
    protected void initToolbarNav(Toolbar toolbar, int navigationIcon, String titleName) {
        initToolbarNav(toolbar, navigationIcon, titleName, false);
    }

    protected void initToolbarNav(Toolbar toolbar, int navigationIcon, String titleName, boolean isHome) {
        toolbar.setNavigationIcon(navigationIcon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOpenDraweListener != null) {
                    mOpenDraweListener.onOpenDrawer();
                }
            }
        });

        if (!TextUtils.isEmpty(titleName))
            toolbar.setTitle(titleName);
        //if (!TextUtils.isEmpty(subTitleName))
        //    toolbar.setSubtitle(subTitleName);//副标题

        if (!isHome) {
            initToolbarMenu(toolbar);
        }
    }

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        FragmentAnimator fragmentAnimator = _mActivity.getFragmentAnimator();
        fragmentAnimator.setEnter(0);
        fragmentAnimator.setExit(0);
        return fragmentAnimator;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentOpenDrawerListener) {
            mOpenDraweListener = (OnFragmentOpenDrawerListener) context;
        } else {
            //throw new RuntimeException(context.toString() + " must implement OnFragmentOpenDrawerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOpenDraweListener = null;
    }

    public interface OnFragmentOpenDrawerListener {
        void onOpenDrawer();
    }
}
