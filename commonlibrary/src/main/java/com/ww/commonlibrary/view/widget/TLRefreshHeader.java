package com.ww.commonlibrary.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.internal.ArrowDrawable;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;
import com.scwang.smartrefresh.layout.util.SmartUtil;
import com.ww.commonlibrary.R;
import com.ww.commonlibrary.util.LogUtils;

import java.util.Date;

/**
 * created by wang.wei on 2019-12-17
 */
public class TLRefreshHeader extends ClassicsHeader {

    public TLRefreshHeader(Context context) {
        super(context);
        setFinishDuration(10);

    }

    public TLRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFinishDuration(10);
    }

}
