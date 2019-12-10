package com.ww.commonlibrary.view.imageWatcherView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ww.commonlibrary.R;

import java.util.ArrayList;
import java.util.List;

public class IndicatorView extends LinearLayout {
    TextView positionTv,saveTv;
    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public void init(Context context){
        inflate(getContext(), R.layout.image_watch_indicator_layout, this);
        positionTv=findViewById(R.id.positionTv);
        saveTv=findViewById(R.id.saveTv);
    }

}
