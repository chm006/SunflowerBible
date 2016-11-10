package com.chm006.sunflowerbible.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import com.chm006.sunflowerbible.MainDrawerActivity;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.http.Consts;
import com.chm006.sunflowerbible.sql.UserInfoDao;

import java.util.HashMap;
import java.util.Map;

/**
 * 登陆页面
 * Created by chenmin on 2016/9/28.
 */

public class LoginWelcomeFragment extends BaseBackFragment {

    private EditText textInputLayout_et1, textInputLayout_et2;
    private TextInputLayout textInputLayout1;
    private UserInfoDao dao;//操作数据库类

    public static LoginWelcomeFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        LoginWelcomeFragment fragment = new LoginWelcomeFragment();
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
        dao = new UserInfoDao(getActivity());
    }

    @Override
    public void pop() {
        getActivity().onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login_welcome;
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (requestCode == Consts.RequestCode.REQUEST && resultCode == Consts.ResultCode.RESULT) {
            //注册用户回调
            String username = data.getString("username", "");
            String password = data.getString("password", "");
            String phoneNumber = data.getString("phoneNumber", "");
            textInputLayout_et1.setText(username);
            if (dao.addUserInfo(phoneNumber, username, password)) {
                ToastUtil.showShort(getActivity(), "注册成功");
            } else {
                ToastUtil.showShort(getActivity(), "注册失败");
            }
        } else if (requestCode == Consts.RequestCode.REQUEST2 && resultCode == Consts.ResultCode.RESULT2){
            //忘记密码回调
            String username = data.getString("username", "");
            textInputLayout_et1.setText(username);
        }
    }

    @Override
    public void init() {
        initToolbar();
        initView();
    }

    private void initView() {
        initEditText();
        //登陆
        View login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputLayout1.isErrorEnabled()) {
                    ToastUtil.showShort(getActivity(), "请检查格式是否正确...");
                } else {
                    if (StringUtil.isEmpty(textInputLayout_et1.getText().toString().trim()) ||
                            StringUtil.isEmpty(textInputLayout_et2.getText().toString().trim())) {
                        ToastUtil.showShort(getActivity(), "用户名或者密码不能为空...");
                    } else {
                        String username = textInputLayout_et1.getText().toString().trim();
                        String password = textInputLayout_et2.getText().toString().trim();
                        //验证数据库账号密码
                        if (dao.findExists(username, password)) {
                            ToastUtil.showShort(getActivity(), "登陆成功");
                            Message message = new Message();
                            Map<String, String> data = new HashMap<String, String>();
                            data.put("username", username);
                            message.what = 1;
                            message.obj = data;
                            MainDrawerActivity.handler.sendMessage(message);
                        } else {
                            ToastUtil.showShort(getActivity(), "登陆失败");
                        }
                    }
                }
            }
        });
        //注册用户
        View registeredUser = findViewById(R.id.registeredUser);
        registeredUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(RegisteredFragment.newInstance("注册账户"), Consts.RequestCode.REQUEST);
            }
        });
        //忘记密码
        View forgetPassword = findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(ForgetPasswordFragment.newInstance("忘记密码"), Consts.RequestCode.REQUEST2);
            }
        });
    }

    private void initEditText() {
        textInputLayout1 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout1);
        textInputLayout_et1 = (EditText) rootView.findViewById(R.id.textInputLayout_et1);
        textInputLayout_et2 = (EditText) rootView.findViewById(R.id.textInputLayout_et2);
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
                    textInputLayout1.setError("账号必须以字母开头，请检查格式...");
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
