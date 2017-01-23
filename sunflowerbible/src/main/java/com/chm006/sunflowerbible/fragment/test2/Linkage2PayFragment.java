package com.chm006.sunflowerbible.fragment.test2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.sunflowerbible.R;
import com.umpay.api.common.Base64;
import com.umpay.api.common.SunBase64;
import com.umpay.api.util.SignUtil;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by chenmin on 2017/1/20.
 */
public class Linkage2PayFragment extends BaseBackFragment {
    private String url = "https://pay.soopay.net/spay/pay/payservice.do";
    private ProgressBar progress_bar;
    private WebView webView;
    private Toolbar toolbar;

    private final String DEFAULT_PUBLIC_KEY_MY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkylW1mbMyvcn5cNVohTIecOObk5kfSXuv8Xr7qOsS3HwyCsisSp/wPlmsaxQvaKmh5tg75hwEOV1TTAd+s6PlMezSdW/zOOsrwxLE0o8kVy/D/j9F2L3fFT76vfLBs4NAzjtqNqHVjRAC09s9k2ZTe4YwuZz11mg8kKcKOA1Z+wIDAQAB";
    private final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGWAu4p6aK1SiQqNKT1nTgYwA8cz0Rde9fRtmLJAx1QxLqrerAUVl/uuXV7NQFSkTipouo3cwEEpae89267AeLJBzKPbKnUID6JYGbwnq7CiRR4E244zcgqE8jo8DnkbH3KkiWonoUMD1uHy6TUFv5W7zrhaz/E59MVmbzrp1TwwIDAQAB";
    private final String DEFAULT_PRIVATE_KEY = "MIICXAIBAAKBgQCkylW1mbMyvcn5cNVohTIecOObk5kfSXuv8Xr7qOsS3HwyCsisSp/wPlmsaxQvaKmh5tg75hwEOV1TTAd+s6PlMezSdW/zOOsrwxLE0o8kVy/D/j9F2L3fFT76vfLBs4NAzjtqNqHVjRAC09s9k2ZTe4YwuZz11mg8kKcKOA1Z+wIDAQABAoGAXAG4bwdc3RTIIyRTkuLjQ8nF2wRCtwxBKdAK2V3LuiEumoYY2tFjkTBOwwyaHYozQ/jufCsTM2yP5cUKXjyQYa7dFEuzPH4yOBmcIBYU0T/k9FVwLXv+vFxpmitEvVse0bFlpRBN2KblJt7iRF/5DRMYC7RejOh8RlWtC9zfsmkCQQDSZ1rQRcPWdFmVDXlPBMUnjIJ6iTSIj6XRZcgKOEeVFqBhL+GJGfGtqFcHUsl60u/giQFByJ+hejGdJnf00MAVAkEAyIB4jmUu5FERMHJh+jRmRnqkHCfyPBCVxoZWWxuEiYkooKdxNngbXJ5K4SvHfybgd3J6o9O9eKNqO8juCkMlzwJAaNz4JutOYpjUePQhLJ/M/xwJf7bYRuOJGibnHyjTjFudjTKG7oTOreVDkrPRFPUCdt7xkG7EGH/FMHSWvp43zQJBALh1YPP36KUKY9sDUFpEvNcYW0TAB131ECX9TxMwhMWXlvX+NeyboOibhF4VzcqP9LBPdL6lvUlwZfqshzqPvgcCQFlit77aRZ8II/jZC4SeTtpIiKmglqdfrPdjkpuQNEUJGeL0w1inUTEivcW9h9uRklyeYSYUlz0fEa0f4YL/jSk=";

    private Map<String, String> params = new HashMap<>();

    public static Linkage2PayFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        Linkage2PayFragment fragment = new Linkage2PayFragment();
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
        return R.layout.fragment_linkage_2_pay;
    }

    @Override
    public void init() {
        initToolbar();
        initParams();
        params.put("identity_type", "");//证件类型（身份证取值：1（证件类型、证件号码需同时传有值或者同时不传））
        params.put("identity_code", "");
        params.put("card_holder", "");
        initURL();
        //encryption();//加密
        initWebView();
    }

    private void encryption() {
        try {
            String source = "331021199111141050shign更";
            String encodedData = RSAUtils.encryptByPublicKey(source, DEFAULT_PUBLIC_KEY_MY);
            System.out.println("加密后文字：\r\n" + encodedData);
            byte[] decodedData = RSAUtils.decryptByPrivateKey(encodedData, DEFAULT_PRIVATE_KEY);
            String target = new String(decodedData);
            System.out.println("解密后文字: \r\n" + target);
            //签名
            source = "这是一行测试RSA数字签名的无意义文字";
            String sign = RSAUtils.sign(source, DEFAULT_PRIVATE_KEY);
            System.err.println("签名:\r" + sign);
            boolean status = RSAUtils.verify(source, DEFAULT_PUBLIC_KEY_MY, sign);
            System.err.println("验证结果:\r" + status);
            // "331021199111141050" "沉迷"
            String identity_code = RSAUtils.encryptByPublicKey(params.get("identity_code"), DEFAULT_PUBLIC_KEY);
            params.put("identity_code", identity_code);
            //params.put("identity_code", URLEncoder.encode(identity_code, "GBK"));
            String card_holder = RSAUtils.encryptByPublicKey(params.get("card_holder"), DEFAULT_PUBLIC_KEY);
            params.put("card_holder", card_holder);
            //params.put("card_holder", URLEncoder.encode(card_holder, "GBK"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initURL() {
        //待签名的参数集合
        List<String> param_sign = new ArrayList<>();
        param_sign.addAll(params.keySet());
        //除去 sign、sign_type 两个字段
        for (int i = 0; i < param_sign.size(); i++) {
            String s = param_sign.get(i);
            if ("sign".equals(s) || "sign_type".equals(s)) {
                param_sign.remove(s);
            }
        }
        Collections.sort(param_sign);//排序
        List<String> param_sign2 = new ArrayList<>();
        for (int i = 0; i < param_sign.size(); i++) {
            String s = param_sign.get(i);
            if (!TextUtils.isEmpty(params.get(s))) {
                param_sign2.add(s);
            }
        }
        param_sign = param_sign2;
        //待签名的字符串
        String content = "";
        for (int i = 0; i < param_sign.size(); i++) {
            String s = param_sign.get(i);
            if (i == param_sign.size() - 1) {
                content = content + s + "=" + params.get(s);
            } else {
                content = content + s + "=" + params.get(s) + "&";
            }
        }
        try {
            //RSA签名
            String sign = RSAUtils.sign(content, DEFAULT_PRIVATE_KEY);
            params.put("sign", URLEncoder.encode(sign, "UTF-8"));//签名
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initParams() {
        Date date = new Date();
        params.put("service", "pay_req_h5_frontpage");
        params.put("charset", "UTF-8");
        params.put("mer_id", "8589");//商户编号
        params.put("sign_type", "RSA");
        params.put("notify_url", "www1");
        params.put("version", "4.0");
        params.put("ret_url", "www2");
        params.put("res_format", "HTML");
        params.put("order_id", /*"20170121843314"*/new SimpleDateFormat("yyyyMMddHHmmss").format(date)/* +
                "userId" + "goodsId"*/);//商户唯一订单号
        params.put("order_id", "20170123143652");
        params.put("mer_date", new SimpleDateFormat("yyyyMMdd").format(date));//商户订单日期
        params.put("amount", "1");//付款金额（人民币，以分为单位）
        params.put("amt_type", "RMB");
        params.put("goods_id", "001");//商品号（该参数为唯一一个对账文件返回的商户保留字段）
        params.put("goods_inf", "这是商品描述信息");//商品描述信息
        params.put("card_id", "");//卡号（使用联动公钥进行RSA加密后，BASE64(GBK编码)加密该字段）
        params.put("identity_type", "1");//证件类型（身份证取值：1（证件类型、证件号码需同时传有值或者同时不传））
        params.put("identity_code", "331021199111141050");//证件号（使用联动公钥进行RSA加密后，BASE64(GBK编码)加密该字段（证件类型、证件号码需同时传有值或者同时不传））
        params.put("card_holder", "dsaf");//持卡人姓名（使用联动公钥进行RSA加密后，BASE64(GBK编码)加密该字段）
        /*
        用户在商户端的唯一标识。
        首次支付时：如果输入该值，且用户在联动的收银台上同意快捷服务协议，则会注册快捷用户业务协议；
        如果没有输入，平台不会注册用户业务协议，也不会返回相应的注册签约信息，用户再次进行支付时，则还需要走首次支付。
        如使用了银联前台页面完成支付，则支付成功后无法在联动优势进行绑卡，同时结果通知中也不会返回支付协议号
        */
        params.put("mer_cust_id", "");//商户用户标识
        //联动优势支付平台原样返回，用于商户的私有信息。
        params.put("mer_priv", "");//商户私有域
        //用户在创建交易时，该用户当前所使用机器的 IP。 用作防钓鱼校验
        params.put("user_ip", "");//用户IP地址
        //主要保存扩展字段的信息,详见附录
        params.put("expand", "");//业务扩展信息
        //单位为分钟，默认1440分钟（24小时）
        params.put("expire_time", "");//订单过期时长
        //格式：代码:内容#代码:内容……如：A0001:二级商户号#A0002:二级商户名称……(此字段要使用UTF-8 urlencode编码)
        params.put("risk_expand", "");//风控扩展信息
        //如果传入card_id、card_holder和卡号，是否允许用户在联动平台上修改。0：不允许修改
        params.put("can_modify_flag", "");//是否允许用户修改支付要素
    }

    private void initWebView() {
        progress_bar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        webView = (WebView) rootView.findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);//设置启用或禁止访问文件数据
        settings.setBuiltInZoomControls(true);//设置是否支持缩放（出现缩放工具）
        settings.setUseWideViewPort(true);//扩大比例的缩放
        settings.setJavaScriptEnabled(true);//设置是否支持JavaScript
        settings.setSupportZoom(true);//设置是否支持变焦（设置可以支持缩放 ）
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        combineURL();
        webView.loadUrl(url);
    }

    private boolean isOne = true;
    private void combineURL() {
        for (String s : params.keySet()) {
            if (params.get(s) != null && !"".equals(params.get(s))) {
                if (isOne) {
                    isOne = false;
                    url = url + "?" + s + "=" + params.get(s);
                } else {
                    url = url + "&" + s + "=" + params.get(s);
                }
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        //重写父类方法，让新打开的网页在当前的WebView中显示
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progress_bar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progress_bar.setVisibility(View.VISIBLE);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        //获得网页的加载进度，显示在右上角的TextView控件中
        public void onProgressChanged(WebView view, int newProgress) {
            progress_bar.setProgress(newProgress);
        }

        //获得网页的标题，作为应用程序的标题进行显示
        public void onReceivedTitle(WebView view, String title) {
            toolbar.setTitle(title);
        }
    }

    //回退键监听
    @Override
    public void pop() {
        if (webView.canGoBack()) {
            webView.goBack(); // goBack()表示返回WebView的上一页面
        } else {
            super.pop();
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }
}
