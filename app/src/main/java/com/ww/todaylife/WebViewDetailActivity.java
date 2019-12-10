package com.ww.todaylife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.util.AndroidBug5497Workaround;
import com.ww.commonlibrary.util.WebViewUtil;
import com.ww.commonlibrary.view.LoadStateView;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.base.BaseSwipeActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by wang.wei on 2018/2/6.
 */

public class WebViewDetailActivity extends BaseSwipeActivity {
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.webview_progressBar)
    ProgressBar mProgressBar;
    String url;
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.moreImg)
    ImageView moreImg;
    @BindView(R.id.loadingView)
    LoadStateView loadingView;
    @BindView(R.id.titleImg)
    ImageView titleImg;
    @Override
    public void initView() {
        WebViewUtil.initSettings(mWebView);
        AndroidBug5497Workaround.assistActivity(this);
        loadingView.hide();
        titleImg.setVisibility(View.GONE);
        loadingView.setRetryListener(() -> {
            loadingView.hide();
            mWebView.reload();
        });
    }

    @Override
    public int setContentId() {
        return R.layout.webview_detail_layout;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initData(Bundle savedInstanceState) {
        Intent it = getIntent();
        url = it.getStringExtra(CommonConstant.WEBVIEW_URL) == null ? "" : it.getStringExtra(CommonConstant.WEBVIEW_URL);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();// 接受所有网站的证书，忽略错误
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                loadingView.showErrorView();
                super.onReceivedError(view, errorCode, description, failingUrl);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!TextUtils.isEmpty(view.getTitle())) {
                    titleTv.setText(view.getTitle());
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            //拦截二级网页进行处理
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    titleTv.setText(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 10) {
                    mProgressBar.setProgress(newProgress);
                }
                if (newProgress == 100) {
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (mProgressBar != null) {
                                        mProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            }, 200
                    );

                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mWebView.addJavascriptInterface(new MyJavascriptInterface(this), "AppJavascriptInterface");
        mWebView.loadUrl(url);
    }


    @OnClick(R.id.backImg)
    public void onViewClicked() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    // js 通信接口，定义供 JavaScript 调用的交互接口
    private class MyJavascriptInterface {
        private Context context;

        public MyJavascriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void startShowImageActivity(String url) {

        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        mWebView.clearHistory();
        mWebView.destroy();
        super.onDestroy();
    }


}
