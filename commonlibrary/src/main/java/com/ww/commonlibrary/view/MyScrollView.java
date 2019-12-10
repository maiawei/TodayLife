package com.ww.commonlibrary.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * created by wang.wei on 2019-11-28
 */
public class MyScrollView extends NestedScrollView {
    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    private ScrollListener scrollListener;

    public MyScrollView(@NonNull Context context) {
        super(context);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (scrollListener != null) {
            scrollListener.onScrollCallback(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public interface ScrollListener {
        void onScrollCallback(int l, int t, int oldl, int oldt);
    }

}
