package com.chm006.library.base.fragment;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.chm006.library.utils.StringUtil;


/**
 * 带toolbar的fragment，点击toolbar左上角退出当前fragment
 * Created by YoKeyword on 16/2/7.
 */
public abstract class BaseBackFragment extends BaseFragment {
    protected static final String ARG_TITLE = "arg_title"; //toolbar 标题的 key，存储到 Bundle.putString(ARG_TITLE, title);
    protected String mTitle;//toolbar 标题名

    /**
     * @param toolbar        toolbar
     * @param navigationIcon 设置NavigationIcon（Toolbar左上角图标，点击返回）
     * @param titleName      标题名称
     */
    protected void initToolbarNav(Toolbar toolbar, int navigationIcon, String titleName) {
        if (StringUtil.isNotEmpty(titleName))
            toolbar.setTitle(titleName);
        toolbar.setNavigationIcon(navigationIcon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        initToolbarMenu(toolbar);
    }

}
