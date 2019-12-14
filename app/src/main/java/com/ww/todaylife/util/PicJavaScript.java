package com.ww.todaylife.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.ww.commonlibrary.util.FileUtils;
import com.ww.commonlibrary.view.imageWatcherView.ImageWatcherHelper;
import com.ww.todaylife.GalleryDetailActivity;
import com.ww.todaylife.R;
import com.ww.todaylife.UserDetailActivity;

import java.util.ArrayList;
import java.util.List;


public class PicJavaScript {
    private ArrayList<String> dataList = new ArrayList<>();
    private Context context;

    public PicJavaScript(Context context) {
        this.context = context;
    }


    @JavascriptInterface
    public void openImg(String url) {
        ((AppCompatActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataList.indexOf(url) != -1) {
                    Intent intent = new Intent(context, GalleryDetailActivity.class);
                    intent.putExtra("position", dataList.indexOf(url));
                    intent.putStringArrayListExtra("urls", dataList);
                    context.startActivity(intent);
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.search_window_in, 0);
                }
            }
        });
    }

    @JavascriptInterface
    public void getImgArray(String arrayStr) {
        String[] urls = arrayStr.split("#todayLife#");
        for (String url : urls) {
            dataList.add(url);
        }
        if (dataList.size() > 0) {
            dataList.remove(0);
        }
    }

    @JavascriptInterface
    public void gotoUserDetail(String userId) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);

    }

}
