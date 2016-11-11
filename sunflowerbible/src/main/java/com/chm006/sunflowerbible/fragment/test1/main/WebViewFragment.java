package com.chm006.sunflowerbible.fragment.test1.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.chm006.library.base.fragment.BaseBackFragment;
import com.chm006.sunflowerbible.R;

/**
 * WebView
 * Created by chenmin on 2016/11/1.
 */
public class WebViewFragment extends BaseBackFragment {

    private Toolbar toolbar;
    private ProgressBar progress_bar;
    private WebView webView;
    private String url = "http://www.baidu.com";

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
        webView.loadUrl(url);
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
