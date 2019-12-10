package com.ww.commonlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ww.commonlibrary.R;
import com.ww.commonlibrary.view.LoadingText;


public class LoadStateView extends RelativeLayout {
    private Context context;
    public LoadingText loadingText;
    private LinearLayout errorLayout;
    private RetryListener retryListener;
    private ImageView stateImage ;
    private TextView stateTv;
    public LoadStateView(Context context) {
        this(context, null);
    }

    public LoadStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.load_state_layout, this);
        loadingText = view.findViewById(R.id.loadingText);
        stateImage=findViewById(R.id.stateImage);
        errorLayout = findViewById(R.id.errorView);
        stateTv=findViewById(R.id.stateTv);
        errorLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (retryListener != null) {
                    show();
                    retryListener.onRetry();
                }
            }
        });
    }

    public void setRetryListener(RetryListener retryListener) {
        this.retryListener = retryListener;
    }

    public void showErrorView() {
        this.setVisibility(VISIBLE);
        loadingText.setVisibility(GONE);
        loadingText.stopAnim();
        errorLayout.setVisibility(VISIBLE);
        stateImage.setImageResource(R.mipmap.img_net_error);
        stateTv.setText(getResources().getText(R.string.network_retry));
    }

    public void show() {
        //开始loading动画
        this.setVisibility(VISIBLE);
        loadingText.setVisibility(VISIBLE);
        loadingText.start();
        errorLayout.setVisibility(GONE);
    }
    public void setEmpty(){
        this.setVisibility(VISIBLE);
        loadingText.setVisibility(GONE);
        loadingText.stopAnim();
        errorLayout.setVisibility(VISIBLE);
        errorLayout.setOnClickListener(null);
        stateImage.setImageResource(R.mipmap.ic_empty);
        stateTv.setText(getResources().getText(R.string.empty_content));
    }

    public void hide() {
        loadingText.stopAnim();
        this.setVisibility(GONE);
    }

    public interface RetryListener {
        void onRetry();
    }

}
