package com.ww.commonlibrary.view;





import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.widget.AppCompatEditText;

import com.ww.commonlibrary.R;


public class ClearEditText extends AppCompatEditText implements
        OnFocusChangeListener, TextWatcher {
    public boolean isCanClear() {
        return canClear;
    }

    public void setCanClear(boolean canClear) {
        this.canClear = canClear;
    }

    private boolean canClear=true;
    private Drawable mClearDrawable;
 
    public ClearEditText(Context context) {
    	this(context, null); 
    }

    private OnFocusListener onFocusListener;
    public interface OnFocusListener{
        void onFocusChange(View v, boolean hasFocus);
    }
    public void setFocusChangeListener(OnFocusListener onFocusListener){
        this.onFocusListener = onFocusListener;
    }

    public ClearEditText(Context context, AttributeSet attrs) {

        this(context, attrs, android.R.attr.editTextStyle);

    } 
    
    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    
    private void init() { 

    	mClearDrawable = getCompoundDrawables()[2]; 
        if (mClearDrawable == null) { 
        	mClearDrawable = getResources() 
                    .getDrawable(R.mipmap.ic_close_circle);
        } 
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this); 
        addTextChangedListener(this); 
    } 
 
 

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null&&canClear) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
            	boolean touchable = event.getX() > (getWidth() 
                        - getPaddingRight() - mClearDrawable.getIntrinsicWidth()) 
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) { 
                    this.setText(""); 
                } 
            } 
        } 
 
        return super.onTouchEvent(event); 
    } 
 

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(onFocusListener!=null){
            onFocusListener.onFocusChange(v,hasFocus);
        }
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0); 
        } else { 
            setClearIconVisible(false); 
        } 
    } 
 
 

    public void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], 
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    } 
     
    

    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        setClearIconVisible(s.length() > 0); 
    } 
 
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
         
    } 
 
    @Override
    public void afterTextChanged(Editable s) {
         
    } 
    
   

    public void setShakeAnimation(){
    	this.setAnimation(shakeAnimation(5));
    }
    
    

    public static Animation shakeAnimation(int counts){
    	Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
    	translateAnimation.setInterpolator(new CycleInterpolator(counts));
    	translateAnimation.setDuration(1000);
    	return translateAnimation;
    }
 
 
}
