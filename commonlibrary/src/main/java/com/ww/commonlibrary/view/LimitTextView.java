package com.ww.commonlibrary.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.ww.commonlibrary.R;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.StringUtils;
import com.ww.commonlibrary.view.widget.LinkTouchMovementMethod;
import com.ww.commonlibrary.view.widget.TouchableSpan;

import org.jetbrains.annotations.NotNull;

/**
 * created by wang.wei on 2019-12-12
 */
public class LimitTextView extends AppCompatTextView {
    public int maxLines = 6;
    public String realContent;
    public String showContent;
    private Context mContext;
    private String ellipsizeText = "...全文";

    public void setCanExpand(Boolean canExpand) {
        this.canExpand = canExpand;
    }

    private boolean canExpand;

    public LimitTextView(Context context) {
        this(context, null);
    }

    public LimitTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LimitTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setContent(String s, int maxWidth) {
        realContent = s.replaceAll(" ", "");
        if (StringUtils.strCount(realContent, "\n") > maxLines-1) {
            showContent = realContent.substring(0, realContent.length() - maxLines);
            showContent();
            return;
        }
        if (getLineMaxNumber(realContent, maxWidth) == 0 || realContent.length() < getLineMaxNumber(realContent, maxWidth) * maxLines) {
            setText(realContent);
        } else {
            showContent = realContent.substring(0, getLineMaxNumber(realContent, maxWidth) * maxLines - maxLines);
            showContent();
        }
    }

    public void showContent() {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(showContent + ellipsizeText);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.blue));
        stringBuilder.setSpan(foregroundColorSpan, stringBuilder.length() - 2, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new TouchableSpan(ContextCompat.getColor(mContext, R.color.blue),
                ContextCompat.getColor(mContext, R.color.main_red), ContextCompat.getColor(mContext, R.color.translucent)) {
            @Override
            public void onClick(@NotNull View view) {
                if (canExpand) {
                    setText(realContent);
                }
            }
        };
        stringBuilder.setSpan(clickableSpan, stringBuilder.length() - 2, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.setMovementMethod(new LinkTouchMovementMethod());
        setText(stringBuilder);
    }


    private int getLineMaxNumber(String text, float maxWidth) {
        if (null == text || "".equals(text)) {
            return 0;
        }
        float textWidth = getPaint().measureText(text);
        float width = textWidth / text.length();
        return (int) (maxWidth / width);
    }

}
