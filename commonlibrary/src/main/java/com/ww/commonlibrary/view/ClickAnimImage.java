package com.ww.commonlibrary.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.ww.commonlibrary.R;
import com.ww.commonlibrary.base.AnimationClickListener;
import com.ww.commonlibrary.util.ScreenUtils;

public class ClickAnimImage extends AppCompatImageView {
    private Context context;
    public boolean isActive = false;
    PopupWindow popupWindow;
    private int normalSourceId;
    private int activeSourceId;
    boolean isShowPop, isActiving;
    ObjectAnimator scaleX, scaleY, alpha, scaleX2, scaleY2, alpha2;
    AnimatorSet animatorSet;
    AnimatorSet animatorSet2;

    public void setIsActive(boolean b) {
        isActive = b;
        if (isActive) {
            setImageResource(activeSourceId);
        }
    }

    public ClickAnimImage(Context context) {
        this(context, null);
    }

    public ClickAnimImage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickAnimImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClickAnimImage, defStyleAttr, 0);
        normalSourceId = a.getResourceId(R.styleable.ClickAnimImage_normalSource, 0);
        activeSourceId = a.getResourceId(R.styleable.ClickAnimImage_activeSource, 0);
        isShowPop = a.getBoolean(R.styleable.ClickAnimImage_isShowPop, false);
        a.recycle();
        this.context = context;
    }

    public void setAnimClickListener(final AnimationClickListener animClickListener) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
                animClickListener.onAnimClick(v);
            }
        });
    }


    public void showAddPopWindow() {
        if (!isShowPop) {
            return;
        }
        popupWindow = new PopupWindow();
        TextView textView = new TextView(context);
        textView.setText("+1");
        //加粗
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(true);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        int size = ScreenUtils.dip2px(context, 14);
        textView.setTextColor(getResources().getColor(R.color.main_red));
        popupWindow.setContentView(textView);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.popWindowBottom);
        popupWindow.showAsDropDown(this, -size / 2, -this.getHeight() * 2 - ScreenUtils.dip2px(context, 10));
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        }, 250);
    }

    public void init() {
        scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1, 0);
        scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1, 0);
        alpha = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        scaleX2 = ObjectAnimator.ofFloat(this, "scaleX", 0, 1);
        scaleY2 = ObjectAnimator.ofFloat(this, "scaleY", 0, 1);
        alpha2 = ObjectAnimator.ofFloat(this, "alpha", 0, 1);
        animatorSet = new AnimatorSet();
        animatorSet2 = new AnimatorSet();
        animatorSet.setDuration(100);
        animatorSet2.setDuration(100);
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet2.playTogether(scaleX2, scaleY2, alpha2);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isActive) {
                    showAddPopWindow();
                    ClickAnimImage.this.setImageResource(activeSourceId);
                } else {
                    ClickAnimImage.this.setImageResource(normalSourceId);
                }
                animatorSet2.start();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isActiving = true;
            }
        });
        animatorSet2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isActive = !isActive;
                isActiving = false;
            }
        });
    }

    public void startAnimation() {
        if (isActiving) {
            return;
        }
        if(animatorSet==null){
            init();
        }
        animatorSet.start();
    }
}
