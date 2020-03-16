package com.ww.commonlibrary.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;

import com.ww.commonlibrary.util.ScreenUtils;

/**
 * created by wang.wei on 2019-12-20
 */
public class AsideImage extends AppCompatImageView {
    private ViewConfiguration configuration;
    private boolean isMoving;
    private float downX, downY;
    private float currentX, currentY;
    private float lastTranslationX, lastTranslationY;
    private float currentTranslationX, currentTranslationY;
    private static final int revertDuration = 150;

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    private ClickListener mClickListener;

    public void setCanAsideLeft(boolean canAsideLeft) {
        this.canAsideLeft = canAsideLeft;
    }

    private boolean canAsideLeft = true;

    public AsideImage(Context context) {
        this(context, null);
    }

    public AsideImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AsideImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configuration = ViewConfiguration.get(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        currentX = event.getRawX();
        currentY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                // no move
                if (Math.abs(currentY - downY) < configuration.getScaledTouchSlop() && Math.abs(currentX - downX) < configuration.getScaledTouchSlop()) {
                    if (mClickListener != null) {
                        mClickListener.onClick(this);
                    }
                } else {
                    lastTranslationY = currentTranslationY;
                    revertWithAnimation();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                lastTranslationY = currentTranslationY;
                revertWithAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScreenRange()) {
                    if (isMoving || Math.abs(currentY - downY) > configuration.getScaledTouchSlop() || Math.abs(currentX - downX) > configuration.getScaledTouchSlop()) {
                        isMoving = true;
                        currentTranslationX = currentX - downX + lastTranslationX;
                        currentTranslationY = currentY - downY + lastTranslationY;
                        setTranslationX(currentTranslationX);
                        setTranslationY(currentTranslationY);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public boolean isScreenRange() {
        // screen out
        return !(currentY < (ScreenUtils.getStatusBarHeight(getContext()) + getHeight() / 2.0f)) && !(currentY > (ScreenUtils.getScreenHeight() - getHeight() / 2.0f)) && !(currentX < 0) && !(currentX > ScreenUtils.getScreenWidth());
    }

    public void revertData() {
        isMoving = false;
    }


    public void revertWithAnimation() {
        float endX = 0;
        // can stop left
        if (canAsideLeft && currentX < ScreenUtils.getScreenWidth() / 2.0f) {
            endX = ScreenUtils.getScreenWidth() - getWidth();
        }
        ValueAnimator animatorX = ValueAnimator.ofFloat(currentTranslationX, -endX);
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                currentTranslationX = currentValue;
                lastTranslationX = currentTranslationX;
                setTranslationX(currentValue);
            }
        });
        animatorX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                revertData();
            }
        });
        animatorX.setDuration(revertDuration);
        animatorX.start();
    }

    public interface ClickListener {
        void onClick(View view);
    }

}
