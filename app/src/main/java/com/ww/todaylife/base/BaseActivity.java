package com.ww.todaylife.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.util.UiUtils;
import com.ww.todaylife.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    protected T mPresenter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.transparent));
                window.setNavigationBarColor(Color.TRANSPARENT);
            }
        }
        setContentView(setContentId());
        unbinder = ButterKnife.bind(this);
        mPresenter = createPresenter();
        initView();
        initData(savedInstanceState);
    }

    public void setToolbar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int topPadding = ScreenUtils.getStatusBarHeight(this);
            if (view != null)
                view.setPadding(0, topPadding, 0, 0);
        }
    }

    public boolean isShowToolBar() {
        return true;
    }

    public abstract int setContentId();

    protected abstract T createPresenter();

    public abstract void initData(Bundle savedInstanceState);

    public abstract void initView();

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && ((v instanceof EditText))) {
            int[] l = {0, 0};
            v.getLocationOnScreen(l);//获取v在窗口中的位置
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(event.getRawX() > left && event.getRawX() < right && event.getRawY() > top && event.getRawY() < bottom);
        }
        return false;
    }

    public void setWindowAlpha(float alpha) {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = alpha;
        window.setAttributes(params);
    }

    public void setStatusView(View view, int color) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = ScreenUtils.getStatusBarHeight(this);
        view.setLayoutParams(params);
        view.setBackgroundColor(getResources().getColor(color));
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                UiUtils.hideSoftInput(this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void startActivityWithAnimation(Intent it, int animation) {
        Bundle translateBundle = ActivityOptionsCompat.makeCustomAnimation(this, animation, 0).toBundle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(it, translateBundle);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        super.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.removeView();
        unbinder.unbind();
        super.onDestroy();
    }
}
