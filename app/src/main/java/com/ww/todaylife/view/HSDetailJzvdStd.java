package com.ww.todaylife.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.ww.commonlibrary.util.LogUtils;
import com.ww.todaylife.R;
import com.ww.todaylife.util.JZMediaIjk;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class HSDetailJzvdStd extends JzvdStd {
    RelatedCallBack callBack;
    private float downX, downY;

    public HSDetailJzvdStd(Context context) {
        super(context);
    }

    public HSDetailJzvdStd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRelatedCallBack(RelatedCallBack callBack) {
        this.callBack = callBack;
    }
    private boolean isCallBack;

    @Override
    public void init(Context context) {
        super.init(context);
        fullscreenButton.setVisibility(GONE);
        bottomProgressBar.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        startButton.setVisibility(GONE);
        thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        topContainer.setVisibility(topCon);
        bottomContainer.setVisibility(bottomCon);
        startButton.setVisibility(startBtn);
        loadingProgressBar.setVisibility(loadingPro);
        thumbImageView.setVisibility(thumbImg);
        mRetryLayout.setVisibility(retryLayout);
    }

    @Override
    public void dissmissControlView() {
        if (state != STATE_NORMAL
                && state != STATE_ERROR
                && state != STATE_AUTO_COMPLETE) {
            post(() -> {
                bottomContainer.setVisibility(View.INVISIBLE);
                topContainer.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.INVISIBLE);
                if (clarityPopWindow != null) {
                    clarityPopWindow.dismiss();
                }
            });
        }
    }

    @Override
    public void showWifiDialog() {
        //super.showWifiDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(cn.jzvd.R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(cn.jzvd.R.string.tips_not_wifi_confirm), (dialog, which) -> {
            dialog.dismiss();
            startVideo();
            WIFI_TIP_DIALOG_SHOWED = true;
        });
        builder.setNegativeButton(getResources().getString(cn.jzvd.R.string.tips_not_wifi_cancel), (dialog, which) -> {
            dialog.dismiss();
            clearFloatScreen();
            if (callBack != null)
                callBack.cancelPlay();
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();

        if (i == cn.jzvd.R.id.fullscreen) {
            Log.i(TAG, "onClick: fullscreen button");
        } else if (i == R.id.start) {
            Log.i(TAG, "onClick: start button");
        }
    }

    public void setSource(String videoUrl) {
        setUp(videoUrl, "", Jzvd.SCREEN_NORMAL, JZMediaIjk.class);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float currentX = event.getRawX();
        float currentY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isCallBack=true;
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                float offsetX = Math.abs(currentX - downX);
                float offsetY = Math.abs(currentY - downY);
                if (offsetY > offsetX && (currentY - downY) < -ViewConfiguration.get(this.getContext()).getScaledTouchSlop()) {
                    if (callBack != null && isCallBack) {
                        isCallBack=false;
                        callBack.showCommentDialog();
                    }

                }else {
                  getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.hs_jz_layout_std;
    }

    @Override
    public void startVideo() {
        super.startVideo();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
    }

    @Override
    public void gotoScreenFullscreen() {
        super.gotoScreenFullscreen();
    }

    @Override
    public void gotoScreenNormal() {
        super.gotoScreenNormal();
    }

    @Override
    public void autoFullscreen(float x) {
        super.autoFullscreen(x);
    }

    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
    }

    //onState 代表了播放器引擎的回调，播放视频各个过程的状态的回调
    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        //循环播放
        startVideo();
    }

    //changeUiTo 真能能修改ui的方法
    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
    }

    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();
    }

    @Override
    public void changeUiToComplete() {
        super.changeUiToComplete();
    }

    @Override
    public void changeUiToError() {
        super.changeUiToError();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }

    public interface RelatedCallBack {
        void cancelPlay();

        void showCommentDialog();
    }

}
