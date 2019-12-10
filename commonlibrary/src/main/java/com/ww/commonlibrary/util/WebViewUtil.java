package com.ww.commonlibrary.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class WebViewUtil {

    public static void initSettings(WebView webview) {
        final WebSettings webSettings = webview.getSettings();
        final Context context = webview.getContext();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDatabaseEnabled(true);

        // 缓存
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
       // webSettings.setDisplayZoomControls(false);

        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);

        String databasePath = webview.getContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(databasePath);
        webSettings.setGeolocationEnabled(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        webSettings.setGeolocationDatabasePath(databasePath);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        if (ApiCompatibleUtil.hasHoneycomb()) {
            webSettings.setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);//android 4.2以后 设置false 允许自动播放h5的音乐
        }
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){//支持混合模式 主要兼容https与http
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        webview.requestFocus();
        webview.requestFocusFromTouch();
        // 监听下载
        webview.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * 清除浏览器的cookie缓存
     *
     * @param context
     */
    public static void clearWebCache(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.removeSessionCookie();
        cm.removeAllCookie();
        CookieSyncManager.getInstance().startSync();
    }

    /**
     * 同步一下cookie
     */
    public static void synCookies(Context context, String url, String cookies) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, cookies);//指定要修改的cookies
        CookieSyncManager.getInstance().sync();

    }
}
