package com.ww.commonlibrary.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ww.commonlibrary.R;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.ScreenUtils;

/**
 * by wang wei
 */

public class LoadingText extends View {
    private Paint mPaint = new Paint();
    public float percent = 0f;
    private int mMaskWidth = 200; //移动view宽度
    private int mMaskViewColor;
    private int textPaintWidth = 8;
    private Xfermode xfermode;
    private PorterDuff.Mode mPorterDuffMode = PorterDuff.Mode.SRC_ATOP;//绘制交汇处 其他的 显示目标图像
    private RectF textRect;
    private ValueAnimator valueAnimator;
    private boolean mIndeterminate;

    public LoadingText(@NonNull Context context) {
        this(context, null);
    }
    private LinearGradient mLinearGradient;

    public int textSize;
    public int textColor;
    public String text;
    private RectF maskRect;
    float distance, baseline;
    public LoadingText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingText(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingText, defStyleAttr, 0);
        mMaskWidth = a.getDimensionPixelSize(R.styleable.LoadingText_mask_view_width, mMaskWidth);
        textSize = a.getDimensionPixelSize(R.styleable.LoadingText_drawTextSize, ScreenUtils.dip2px(context, 30));
        mMaskViewColor = a.getColor(R.styleable.LoadingText_mask_view_color, 0);
        textColor = a.getColor(R.styleable.LoadingText_drawTextColor, getResources().getColor(R.color.divider_color));
        text = a.getString(R.styleable.LoadingText_drawText);
        a.recycle();
        init();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int resultW = widthSize;
        int resultH = heightSize;

        if (widthMode == MeasureSpec.AT_MOST) {
            resultW = (int) (text.length() * textSize);
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            resultH = textSize * 2;
        }
        setMeasuredDimension(resultW, resultH);
    }

    private void init() {
        int[] gradientColors = {0x00ffffff, 0x11000000, 0x20000000, 0x20000000, 0x00ffffff};
        xfermode = new PorterDuffXfermode(mPorterDuffMode);
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        textRect = new RectF();
        maskRect = new RectF();
        mLinearGradient = new LinearGradient(0, 0, mMaskWidth, 0, gradientColors, null, Shader.TileMode.MIRROR);

    }

    public void drawContentText(Canvas canvas) {
        mPaint.setShader(null);
        mPaint.setColor(textColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(textPaintWidth);
        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        textRect.set(0, 0, getWidth(), getHeight());
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        baseline = textRect.centerY() + distance;
        canvas.drawText(text, textRect.centerX(), baseline, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saveCount = canvas.saveLayer(textRect, mPaint, Canvas.ALL_SAVE_FLAG);
        //角度多大要精确算下高度
        canvas.rotate(-3);
        drawContentText(canvas);
        //设为混合模式 画蒙层
        mPaint.setXfermode(xfermode);
        if (mMaskViewColor == 0) {
            mPaint.setShader(mLinearGradient);
        } else {
            mPaint.setColor(mMaskViewColor);
        }
        mPaint.setStyle(Paint.Style.FILL);
        if (percent != 0)
            maskRect.set(percent * (getWidth() + getHeight()) - getHeight(), 0, percent * (getWidth() + getHeight()) - getHeight() + mMaskWidth, getHeight());
        // 错切 斜矩形
        canvas.skew(1, 0);
        canvas.drawRect(maskRect, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        textRect = new RectF(0, 0, getWidth(), getHeight());
    }


    private void startAnim() {
        if (getVisibility() != VISIBLE) {
            return;
        }
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000);
            valueAnimator.setRepeatCount(-1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    percent = (float) valueAnimator.getAnimatedValue();
                    postInvalidate();
                }
            });
        }
        valueAnimator.start();
        mIndeterminate = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (valueAnimator != null && getVisibility() == VISIBLE && getWindowVisibility() == VISIBLE
                && mIndeterminate) {
            startAnim();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    public void stopAnim() {
        mIndeterminate = false;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }

    }

    public void start() {
        startAnim();
    }

}
