package com.ww.commonlibrary.http;

import android.webkit.JavascriptInterface;


public abstract class AppJavaScript {
    @JavascriptInterface
   public abstract String getAuthorization();
    @JavascriptInterface
    public abstract   void doContent(String json);
    @JavascriptInterface
    public abstract  void doLogin();
    @JavascriptInterface
    public  abstract  void openImage(String str);
}
