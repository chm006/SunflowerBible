package com.chm006.sunflowerbible.fragment.test1.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.library.utils.StringUtil;
import com.chm006.sunflowerbible.R;

/**
 * EditText样式
 * Created by chenmin on 2016/9/9.
 */
public class EditTextStyleFragment extends BaseBackFragment {

    public static EditTextStyleFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        EditTextStyleFragment fragment = new EditTextStyleFragment();
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
        return R.layout.fragment_edit_text_style;
    }

    @Override
    public void init() {
        initToolbar();
        initEditText();
        EditText edittext_style1 = findViewById(R.id.edittext_style1);
        edittext_style1.setText("0");
        edittext_style1.requestFocus();
    }

    private void initEditText() {
        final TextInputLayout textInputLayout1 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout1);
        EditText textInputLayout_et1 = (EditText) rootView.findViewById(R.id.textInputLayout_et1);
        textInputLayout_et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //检测错误输入，当输入错误时，hint会变成红色并提醒
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //检查实际是否匹配，由自己实现
                if (checkType(charSequence.toString())) {
                    textInputLayout1.setErrorEnabled(false);
                } else {
                    textInputLayout1.setErrorEnabled(true);
                    textInputLayout1.setError("必须以字母开头，请检查格式...");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //开头必须是字母
    private boolean checkType(String s) {
        return !StringUtil.isNotEmpty(s) || (s.charAt(0) <= 'Z' && s.charAt(0) >= 'A') || (s.charAt(0) <= 'z' && s.charAt(0) >= 'a');
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }
}
