package com.ww.commonlibrary.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ww.commonlibrary.R;


public class ToastView extends Toast {
    private TextView content;
    private ImageView stateImage;
    public static final int TYPE_SUCCESS = 1;
    public static final int TYPE_ERROR = 2;
    private Context context;

    public ToastView(Context context) {
        super(context);
        this.context=context;
        init();
    }


    public void init() {

        setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        content = view.findViewById(R.id.content);
        stateImage = view.findViewById(R.id.stateImage);
        setView(view);
    }

    public void show(String msg, int state) {
        content.setText(msg);
        switch (state) {
            case TYPE_SUCCESS:
                stateImage.setImageResource(R.mipmap.hud_success);
                break;
            case TYPE_ERROR:
                stateImage.setImageResource(R.mipmap.hud_error);
                break;
        }
        show();
    }

}
