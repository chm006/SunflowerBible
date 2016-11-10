package com.chm006.sunflowerbible.fragment.test1.main;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.library.utils.DensityUtil;
import com.chm006.library.utils.my_utils.ViewAlphaAnimationUtil;
import com.chm006.library.widget.ListenedScrollView;
import com.chm006.sunflowerbible.R;

/**
 * WebView
 * Created by chenmin on 2016/11/1.
 */
public class WebViewFragment extends BaseBackFragment {

    public static WebViewFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        WebViewFragment fragment = new WebViewFragment();
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
        return R.layout.fragment_webview;
    }

    @Override
    public void init() {
        initToolbar();
        initWebView();
    }

    private void initWebView() {
        WebView webView = (WebView) rootView.findViewById(R.id.webView);

        /**
         * WebSettings用来对WebView的配置进行配置和管理，
         * 比如是否可以进行文件操作、缓存的设置、页面是否支持放大和缩小、
         * 是否允许使用数据库api、字体及文字编码设置、是否允许js脚本运行、
         * 是否允许图片自动加载、是否允许数据及密码保存等等
         */
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSavePassword(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN : 适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setHorizontalScrollbarOverlay(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.requestFocus();

        /**
         * WebChromeClient会在一些影响浏览器ui交互动作发生时被调用，
         * 比如WebView关闭和隐藏、页面加载进展、js确认框和警告框、js加载前、js操作超时、webView获得焦点等等
         */
        //webView.setWebChromeClient(new MyWebChromeClient());

        /**
         * 设置当前网页的链接仍在WebView中跳转，而不是跳到手机浏览器里显示
         *
         * shouldOverrideUrlLoading表示当前webView中的一个新url需要加载时，
         * 给当前应用程序一个处理机会，如果没有重写此函数，webView请求ActivityManage选择合适的方式处理请求，
         * 就像弹出uc和互联网让用户选择浏览器一样。重写后return true表示让当前程序处理，return false表示让当前webView处理
         */
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        /**
         * 设置开始加载网页、加载完成、加载错误时处理
         */
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // 开始加载网页时处理 如：显示"加载提示" 的加载对话框
                //DialogManager.showLoadingDialog(this);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 网页加载完成时处理  如：让 加载对话框 消失
                //DialogManager.dismissLoadingDialog();
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 加载网页失败时处理  如：
                view.loadDataWithBaseURL(null,
                        "<span style=\"color:#FF0000\">网页加载失败</span>",
                        "text/html",
                        "utf-8",
                        null);
            }
        });

        /**
         * 处理https请求，为WebView处理ssl证书设置
         * WebView默认是不处理https请求的，页面显示空白，需要进行如下设置
         */
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受信任所有网站的证书
                // handler.cancel();   // 默认操作 不处理
                // handler.handleMessage(null);  // 可做其他处理
            }
        });

        /**
         * 显示页面加载进度
         *
         * onProgressChanged通知应用程序当前页面加载的进度
         * progress表示当前页面加载的进度，为1至100的整数
         */
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //setTitle("页面加载中，请稍候..." + progress + "%");
                //setProgress(progress * 100);
                if (progress == 100) {
                    //setTitle(R.string.app_name);
                }
            }
        });

        /**
         * back键控制网页后退
         * Activity默认的back键处理为结束当前Activity，WebView查看了很多网页后，希望按back键返回上一次浏览的页面，
         * 这个时候我们就需要覆盖WebView所在Activity的onKeyDown函数，告诉他如何处理
         */
        /*public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                webView.goBack();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }*/


    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        initToolbarNav(toolbar, R.mipmap.ic_arrow_back_white_24dp, mTitle);
    }
}
