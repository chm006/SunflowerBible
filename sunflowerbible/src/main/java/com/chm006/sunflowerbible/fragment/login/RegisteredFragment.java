package com.chm006.sunflowerbible.fragment.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
 * 注册用户
 * Created by chenmin on 2016/9/28.
 */

public class RegisteredFragment extends BaseBackFragment {

    private boolean var = true;//当前能否发短信
    private TimeListener listener;
    private Button send;
    private EditText textInputLayout_et1, textInputLayout_et2, textInputLayout_et3, textInputLayout_et4, textInputLayout_et5;
    private TextInputLayout textInputLayout1, textInputLayout2, textInputLayout3, textInputLayout4, textInputLayout5;
    private UserInfoDao dao;
    private CheckBox checkBox;

    public static RegisteredFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        RegisteredFragment fragment = new RegisteredFragment();
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
        return R.layout.fragment_registered;
    }

    @Override
    public void init() {
        initToolbar();
        initView();
    }

    private void initView() {
        initEditText();
        checkBox = findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(false);
                showDialog();
            }
        });
        //注册
        View registered = findViewById(R.id.registered);
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputLayout1.isErrorEnabled()) {
                    ToastUtil.showShort(getActivity(), "请检查格式是否正确...");
                } else {
                    if (StringUtil.isEmpty(textInputLayout_et1.getText().toString().trim()) ||
                            StringUtil.isEmpty(textInputLayout_et2.getText().toString().trim())) {
                        ToastUtil.showShort(getActivity(), "用户名或者密码不能为空...");
                    } else {
                        if (dao.findUserName(textInputLayout_et1.getText().toString().trim())) {
                            textInputLayout1.setErrorEnabled(true);
                            textInputLayout1.setError("账号已经存在，请换个账号...");
                        }else {
                            if (!textInputLayout_et2.getText().toString().trim().equals(textInputLayout_et3.getText().toString().trim())) {
                                ToastUtil.showShort(getActivity(), "密码输入不一致...");
                            } else {
                                if (checkBox.isChecked()) {
                                    //验证短信
                                    verificationSMSSDK(textInputLayout_et4.getText().toString().trim(), textInputLayout_et5.getText().toString());
                                } else {
                                    ToastUtil.showShort(getActivity(), "请先阅读并同意本软件条款才能继续...");
                                }
                            }
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
                if (StringUtil.isEmpty(textInputLayout_et4.getText().toString().trim())) {
                    ToastUtil.showShort(getActivity(), "手机号码不能为空...");
                } else if (textInputLayout_et4.getText().toString().trim().length() != 11) {
                    ToastUtil.showShort(getActivity(), "请输入正确的11位手机号码...");
                } else {
                    //发送验证码
                    sendSMSSDK(textInputLayout_et4.getText().toString().trim());
                }
            }
        });
    }

    /**
     * 这是兼容的 AlertDialog
     */
    private void showDialog() {
      /*
      这里使用了 android.support.v7.app.AlertDialog.Builder
      可以直接在头部写 import android.support.v7.app.AlertDialog
      那么下面就可以写成 AlertDialog.Builder
      */
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("《Sunflower Bible用户服务协议》请仔细阅读以下条款");
        builder.setMessage("Sunflower Bible在此依法做出特别声明如下：\n" +
                "\n" +
                "    用户在使用本软件前必须仔细阅读以下条款，用户一旦注册成功，即表示接受并同意以下所有条款，不愿接受的，不得注册。用户还应不时注意本条款修改情况，在本条款修改后用户继续使用本软件或接受本软件服务时，视为用户同意该修改。\n" +
                "\n" +

                "一、计费标准和付款方式\n" +
                "\n" +
                "二、特别条款\n" +
                "\n" +
                "1、用户一旦在指定手机卸载本软件，即清除掉在该手机注册过的用户信息。\n" +
                "\n" +
                "2、用户在指定手机上注册一个新用户，该用户只能在该手机上登陆本软件。\n" +
                "\n" +

                "三、保密义务和知识产权\n" +
                "\n" +
                "1、非Sunflower Bible书面授权，不得披露本软件的信息，不得在任何时间以任何直接或间接方式使用或披露您使用本软件/本系统的有关隐私信息、保密信息、商业秘密或知识产权，包括但不限于（1）任何项目或将要进行的项目的内容；（2）有关本软件/本系统或客户的任何其它保密信息，包括本软件的业务流程。\n" +
                "\n" +
                "2、本软件上的所有资料以及您作为本软件会员期间收到的所有其它的本软件的资料均为本软件的知识产权。\n" +
                "\n" +
                "四、隐私条款\n" +
                "\n" +
                "1、您同意并授权本软件可以按照本条款的约定无偿使用您的会员信息，且不得以Sunflower Bible未经授权使用该等信息为由而要求Sunflower Bible承担侵权责任。\n" +
                "\n" +
                "2、本软件可以为管理您在本软件的会员身份、为您提供推介、改进服务、咨询服务等会员服务而收集并保留、分析、使用有关您的信息，包括您的个人信息、工作信息。\n" +
                "\n" +
                "3、您授权或许可本软件向您的电话、邮箱发送信息、邮件，以及直接或间接通过该等电话、邮箱联系您。\n" +
                "\n" +
                "4、您同意本软件根据司法部门、政府部门的要求披露您的会员信息。\n" +
                "\n" +
                "5、您同意本软件由于经营、管理、融资，而向股东或投资人披露您的信息。\n" +
                "\n" +
                "6、您同意本软件保留您的隐私信息，包括您本软件会员期间及以后。\n" +
                "\n" +
                "7、本软件承诺不会向第三方出售您的个人信息。\n" +
                "\n" +
                "五、违约责任\n" +
                "\n" +
                "1、如果您违反本条款的规定，本软件将有权采取任何法律措施。\n" +
                "\n" +
                "2、因您违反本条款给他人造成损害的，由您自行承担法律责任；因您违反本条款给本软件造成损害的，本软件有权要求您承担赔偿责任，并直接从应退还的款项中扣除。前述所称损害包括直接损害和间接损害。\n" +
                "\n" +
                "六、纠纷解决\n" +
                "\n" +
                "任何因本条款及履行本条款产生的纠纷、争议均应提交Sunflower Bible所在地有管辖权人民法院管辖。\n" +
                "\n" +
                "七、适用法律\n" +
                "\n" +
                "本条款以及所有因本条款产生的权利主张，包括侵权、合同或其它方面，均应适用中华人民共和国法律。\n" +
                "\n" +
                "八、其他\n" +
                "\n" +
                "1、如本条款中任何条款无效或不可执行，不影响本条款中其它条款的有效性和可执行性。\n" +
                "\n" +
                "2、本条款中关于隐私条款，保密条款，知识产权的，在本条款终止或期满后继续有效。\n" +
                "\n" +
                "3、您有权通知本软件随时终止加入本软件/本系统；本软件亦有权随时撤销、终止、限制您的会员身份。\n" +
                "\n" +
                "4、您点击“已阅读并同意”后，您应遵守本条款的全部内容及本软件法律声明中的《Sunflower Bible用户服务协议》、《知识产权规则》、《隐私规则》及不时制定的其他规则。本条款及相关协议及规则的全部内容均对您具有法律约束力。\n" +
                "\n" +
                "5、“本软件”指Sunflower Bible软件。");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkBox.setChecked(false);
            }
        });
        builder.setPositiveButton("已阅读并同意以上条款", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkBox.setChecked(true);
            }
        });
        builder.show();
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
                String password = textInputLayout_et2.getText().toString().trim();
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("password", password);
                bundle.putString("phoneNumber", phoneNum);
                setFramgentResult(Consts.ResultCode.RESULT, bundle);
                pop();
            }

            @Override
            public void error(Throwable error) {
                //ToastUtil.showShort(getActivity(), "验证码错误：" + error);
                textInputLayout5.setError("验证码错误...");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //注意解除通知避免内存泄露
        SMSManager.getInstance().unRegisterTimeListener(listener);
    }

    private void initEditText() {
        textInputLayout1 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout1);
        textInputLayout2 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout2);
        textInputLayout3 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout3);
        textInputLayout4 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout4);
        textInputLayout5 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout5);

        textInputLayout_et1 = (EditText) rootView.findViewById(R.id.textInputLayout_et1);
        textInputLayout_et2 = (EditText) rootView.findViewById(R.id.textInputLayout_et2);
        textInputLayout_et3 = (EditText) rootView.findViewById(R.id.textInputLayout_et3);
        textInputLayout_et4 = (EditText) rootView.findViewById(R.id.textInputLayout_et4);
        textInputLayout_et5 = (EditText) rootView.findViewById(R.id.textInputLayout_et5);

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
