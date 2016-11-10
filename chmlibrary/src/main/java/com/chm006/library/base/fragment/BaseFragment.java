package com.chm006.library.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.chm006.library.R;
import com.chm006.library.utils.ToastUtil;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.helper.FragmentResultRecord;

/**
 * 带toolbar的fragment
 * Created by chenmin on 2016/8/15.
 */
public abstract class BaseFragment extends SupportFragment implements Toolbar.OnMenuItemClickListener {
    protected static final String TAG = "Fragmentation";
    protected View rootView;
    protected LayoutInflater inflater;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //防止重新绘制视图
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(getLayoutId(), container, false);
            this.inflater = inflater;
            init();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 隐藏软键盘
        if(getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void startFragment(SupportFragment toFragment) {
        if (getParentFragment() instanceof BaseFragment) {
            ((BaseFragment) getParentFragment()).start(toFragment);
        }
    }

    public void startForResultFragment(SupportFragment toFragment, int requestCode) {
        if (getParentFragment() instanceof BaseFragment) {
            ((BaseFragment) getParentFragment()).startForResult(toFragment, requestCode);
        }
    }

    /**
     * fragment的findViewById（）方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int id)
    {
        if (rootView == null)
        {
            return null;
        }

        return (T) rootView.findViewById(id);
    }

    /**
     * 获取fragment布局文件ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化
     */
    public abstract void init();

    protected void initToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(inflateMenu());
        toolbar.setOnMenuItemClickListener(this);
    }

    /**
     * Toolbar menu id
     * R.menu.xxx
     */
    protected int inflateMenu() {
        return R.menu.toolbar_menu;
    }

    /**
     * Toolbar menu item 点击事件
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_share) {
            ToastUtil.showShort(getContext(), "分享");
        } else if (i == R.id.action_hierarchy) {//显示栈视图
            _mActivity.showFragmentStackHierarchyView();
            _mActivity.logFragmentStackHierarchy(TAG);
        }
        return true;
    }
}
