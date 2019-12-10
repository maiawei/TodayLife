package com.ww.todaylife.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ww.todaylife.R;
import com.ww.todaylife.util.JZMediaIjk;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class ListJzVideo extends JzvdStd {
    OnStartListener onStartListener;
    public ListJzVideo(Context context) {
        super(context);
    }

    public ListJzVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnStartListener(OnStartListener onStartListener) {
        this.onStartListener = onStartListener;
    }
    public void setSource(String videoUrl){
        setUp(videoUrl, "", Jzvd.SCREEN_NORMAL, JZMediaIjk.class);
    }
    public void setSource(String videoUrl,String title){
        setUp(videoUrl, title, Jzvd.SCREEN_NORMAL, JZMediaIjk.class);
    }
    public void setSource(String videoUrl,String title,String duration){
        videoDurationTv.setText(duration);
        videoDurationTv.setVisibility(VISIBLE);
        setUp(videoUrl, title, Jzvd.SCREEN_NORMAL, JZMediaIjk.class);
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == cn.jzvd.R.id.start) {
            videoDurationTv.setVisibility(GONE);
            if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                if (onStartListener != null) {
                    onStartListener.startClick();
                    return;
                }
            }
        } else if (i == cn.jzvd.R.id.thumb) {
            videoDurationTv.setVisibility(GONE);

            if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                if (onStartListener != null) {
                    onStartListener.startClick();
                    return;
                }
            }
        }
        super.onClick(v);
    }
    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_std;
    }
    public interface OnStartListener {
        void startClick();
    }

}
