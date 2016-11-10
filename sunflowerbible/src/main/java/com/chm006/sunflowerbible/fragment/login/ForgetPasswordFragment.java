package com.chm006.sunflowerbible.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.library.utils.StringUtil;
import com.chm006.library.utils.ToastUtil;
import com.chm006.library.utils.my_utils.SMSManager;
import com.chm006.sunflowerbible.R;
import com.chm006.sunflowerbible.http.Consts;
import com.chm006.sunflowerbible.sql.UserInfoDao;
import com.jude.smssdk_mob.Callback;
import com.jude.smssdk_mob.TimeListener;

/**
 * 忘记密码
 * Created by chenmin on 2016/9/28.
 */

public class ForgetPasswordFragment extends BaseBackFragment {

    private boolean var = true;//当前能否发短信
    private TimeListener listener;
    private EditText textInputLayout_et1, textInputLayout_et2, textInputLayout_et3;
    private TextInputLayout textInputLayout1, textInputLayout2, textInputLayout3;
    private Button send;
    private UserInfoDao dao;

    public static ForgetPasswordFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
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
    protected int getLayoutId() {
        return R.layout.fragment_forget_password;
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (requestCode == Consts.RequestCode.REQUEST2 && resultCode == Consts.ResultCode.RESULT2) {
            setFramgentResult(Consts.ResultCode.RESULT2, data);
            pop();
        }
    }

    @Override
    public void init() {
        initToolbar();
        initView();
    }

    private void initView() {
        initEditText();
        //下一页
        View next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputLayout1.isErrorEnabled()) {
                    ToastUtil.showShort(getActivity(), "请检查格式是否正确...");
                } else {
                    if (StringUtil.isEmpty(textInputLayout_et1.getText().toString().trim())) {
                        ToastUtil.showShort(getActivity(), "用户名不能为空...");
                    } else {
                        if (StringUtil.isEmpty(textInputLayout_et2.getText().toString().trim())) {
                            ToastUtil.showShort(getActivity(), "手机号码不能为空...");
                        } else {
                            verificationSMSSDK(textInputLayout_et2.getText().toString().trim(), textInputLayout_et3.getText().toString().trim());
                        }
                    }
                }
            }
        });
        //发送
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(textInputLayout_et2.getText().toString().trim())) {
                    ToastUtil.showShort(getActivity(), "手机号码不能为空...");
                } else if (textInputLayout_et2.getText().toString().trim().length() != 11) {
                    ToastUtil.showShort(getActivity(), "请输入正确的11位手机号码...");
                } else {
                    if (dao.findExists2(textInputLayout_et1.getText().toString().trim(), textInputLayout_et2.getText().toString().trim())) {
                        //发送验证码
                        sendSMSSDK(textInputLayout_et2.getText().toString().trim());
                    } else {
                        ToastUtil.showLong(getActivity(), "输入的手机号码与账号不匹配...");
                    }
                }
            }
        });
    }

    //发送短信
    private void sendSMSSDK(String phoneNum) {
        if (var && SMSManager.getInstance().sendMessage(getActivity(), "86", phoneNum)) {
            SMSManager.getInstance().setDefaultDelay(60);
            //重发与等待时间
            listener = new TimeListener() {
                @Override
                public void onLastTimeNotify(int lastSecond) {
                    //ToastUtil.showShort(getActivity(), "剩余时间通知：" + lastSecond);
                    if (lastSecond == 0) {
                        send.setText("发送");
                    }else {
                        send.setText(lastSecond + "s");
                    }
                }

                @Override
                public void onAbleNotify(boolean valuable) {
                    //ToastUtil.showShort(getActivity(), "当前能否发短信的通知：" + valuable);
                    var = valuable;
                }
            };
            SMSManager.getInstance().registerTimeListener(listener);
        }
    }

    //验证短信
    private void verificationSMSSDK(final String phoneNum, String verificationCode) {
        SMSManager.getInstance().verifyCode(getActivity(), "86", phoneNum, verificationCode, new Callback() {
            @Override
            public void success() {
                //ToastUtil.showShort(getActivity(), "验证成功");
                String username = textInputLayout_et1.getText().toString().trim();
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString(ARG_TITLE, "修改密码");
                startForResult(ChangePasswordFragment.newInstance(bundle), Consts.RequestCode.REQUEST2);
            }

            @Override
            public void error(Throwable error) {
                //ToastUtil.showShort(getActivity(), "验证码错误：" + error);
                textInputLayout3.setError("验证码错误...");
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
