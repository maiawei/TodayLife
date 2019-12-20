package com.ww.commonlibrary.view.newsRefreshHeader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import org.jetbrains.annotations.NotNull;

import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * created by wang.wei on 2019-12-20
 */

public class TodayLifeHeader extends LinearLayout implements RefreshHeader {

    public static String REFRESH_HEADER_PULL = "下拉推荐";
    public static String REFRESH_HEADER_REFRESHING = "推荐中...";
    public static String REFRESH_HEADER_RELEASE = "松开推荐";
    private NewRefreshView mNewRefreshView;
    private TextView releaseText;


    public TodayLifeHeader(Context context) {
        this(context, null);
    }

    public TodayLifeHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TodayLifeHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.setGravity(Gravity.CENTER);
        this.setOrientation(LinearLayout.VERTICAL);
        setPadding(0,15,0,15);
        mNewRefreshView = new NewRefreshView(context);

        LinearLayout.LayoutParams lpNewRefresh = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        this.addView(mNewRefreshView, lpNewRefresh);


        LinearLayout.LayoutParams lpReleaseText = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpReleaseText.setMargins(0, 20, 0, 0);

        releaseText = new TextView(context);
        releaseText.setText(REFRESH_HEADER_PULL);
        releaseText.setTextColor(Color.parseColor("#666666"));
        releaseText.setTextSize(COMPLEX_UNIT_SP, 12);

        addView(releaseText, lpReleaseText);

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            mNewRefreshView.setDragState();
            mHandler.sendEmptyMessageDelayed(0, 250);
        }
    };

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        mNewRefreshView.setFraction((percent - 0.9f) * 8f);
    }

    @Override
    public void onReleased(@NotNull RefreshLayout refreshLayout, int height, int extendHeight) {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        mHandler.removeCallbacksAndMessages(null);
        mNewRefreshView.setDrag();
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NotNull RefreshLayout refreshLayout, @NotNull RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullUpToLoad:
            case Loading:
                break;
            case PullDownToRefresh:
                releaseText.setText(REFRESH_HEADER_PULL);
                break;
            case ReleaseToRefresh:
                releaseText.setText(REFRESH_HEADER_RELEASE);
                break;
            case Refreshing:
                releaseText.setText(REFRESH_HEADER_REFRESHING);
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }
}
