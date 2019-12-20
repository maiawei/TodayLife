package com.ww.commonlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by wang.wei on 2017/2/9.
 */

// vertical
public class CustomLinearLayout extends ViewGroup {
    private int widthSize, heightSize;


    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //子view layout进行位置确定
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int top = 0;
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            MarginLayoutParams layoutParams =
                    (MarginLayoutParams) childView.getLayoutParams();
            top += layoutParams.topMargin;
            childView.layout(layoutParams.leftMargin, top, layoutParams.leftMargin + childView.getMeasuredWidth(), top + childView.getMeasuredHeight());
            top += (layoutParams.topMargin + childView.getMeasuredHeight());
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // 计算出所有的childView的宽和高
       // measureChildren(widthMeasureSpec, heightMeasureSpec);
        //宽度不作处理  处理高度
        //EXACTLY：确切的大小，如：100dp或者march_parent
        //AT_MOST：大小不可超过某数值，如：wrap_content
        if (widthMode == MeasureSpec.EXACTLY) {
            this.widthSize = widthSize;
        } else {
            this.widthSize = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            this.heightSize = heightSize;
        } else {
            this.heightSize = measureChildHeight();
        }
        setMeasuredDimension(this.widthSize, this.heightSize);
    }

    public int measureChildHeight() {
        //获取子view的高度以及topmargin
        int count = getChildCount();
        int totalHeight = 0;
        if (count != 0) {
            for (int i = 0; i < count; i++) {
                View childView = getChildAt(i);
                MarginLayoutParams layoutParams =
                        (MarginLayoutParams) childView.getLayoutParams();
                int height = childView.getMeasuredHeight() + layoutParams.topMargin;
                totalHeight += height;
            }
        }

        return totalHeight;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
