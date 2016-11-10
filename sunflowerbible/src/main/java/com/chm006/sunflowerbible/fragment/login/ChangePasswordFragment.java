package com.chm006.sunflowerbible.fragment.login;

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
import com.chm006.library.utils.ToastUtil;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.http.Consts;
import com.chm006.sunflowerbible.sql.UserInfoDao;

/**
 * 修改密码
 * Created by chenmin on 2016/10/10.
 */

public class ChangePasswordFragment extends BaseBackFragment {

    private UserInfoDao dao;
    private String username;
    private TextInputLayout textInputLayout1, textInputLayout2, textInputLayout3;
    private EditText textInputLayout_et1, textInputLayout_et2, textInputLayout_et3;

    public static ChangePasswordFragment newInstance(Bundle args) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(ARG_TITLE);
            username = bundle.getString("username");
        }
        dao = new UserInfoDao(getActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_change_password;
    }

    @Override
    public void init() {
        initToolbar();
        initEditText();
        initView();
    }

    private void initView() {
        View affirm = findViewById(R.id.affirm);
        affirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(textInputLayout_et2.getText().toString().trim())
                        || StringUtil.isEmpty(textInputLayout_et3.getText().toString().trim())) {
                    ToastUtil.showShort(getActivity(), "密码不能为空...");
                } else {
                    if (!textInputLayout_et2.getText().toString().trim().equals(textInputLayout_et3.getText().toString().trim())) {
                        ToastUtil.showShort(getActivity(), "密码输入不一致...");
                    } else {
                        if (dao.changePassword(username, textInputLayout_et2.getText().toString().trim())) {
                            ToastUtil.showShort(getActivity(), "密码修改成功");
                            Bundle bundle = new Bundle();
                            bundle.putString("username", username);
                            setFramgentResult(Consts.ResultCode.RESULT2, bundle);
                            pop();
                        } else {
                            ToastUtil.showShort(getActivity(), "密码修改失败");
                        }
                    }
                }
            }
        });
    }

    private void initEditText() {
        textInputLayout1 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout1);
        textInputLayout2 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout2);
        textInputLayout3 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout3);

        textInputLayout_et1 = (EditText) rootView.findViewById(R.id.textInputLayout_et1);
        textInputLayout_et2 = (EditText) rootView.findViewById(R.id.textInputLayout_et2);
        textInputLayout_et3 = (EditText) rootView.findViewById(R.id.textInputLayout_et3);

        //设置不可编辑
        textInputLayout_et1.setText(username);
        textInputLayout_et1.setFocusable(false);
        textInputLayout_et1.setEnabled(false);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }
}
