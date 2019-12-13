package com.ww.todaylife.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ww.commonlibrary.util.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class LazyFragment<T extends BasePresenter> extends Fragment {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    public abstract int getLayoutId();

    private View mRootView;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    protected BaseSwipeActivity mBaseActivity;
    private Unbinder unbinder;
    public T mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity = (BaseSwipeActivity) getActivity();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = createPresenter();
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, mRootView);
            this.initViews();
            this.initData(savedInstanceState);
            isPrepared = true;
            return mRootView;
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            unbinder = ButterKnife.bind(this, mRootView);
            return mRootView;
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            try {
                unbinder.unbind();
            } catch (Exception e) {
            }
        }
        if (mPresenter != null) {
            mPresenter.removeView();
        }
        super.onDestroy();
    }

    public void startActivity(Intent intent, boolean useDefaultFlag) {
        if (useDefaultFlag) {
            super.startActivity(intent);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        super.startActivity(intent);
    }

    public void startActivityWithNewTask(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        super.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        if (mHasLoadedOnce) {
            return;
        }
        lazyLoad();
        mHasLoadedOnce = ismHasLoadedOnce();
    }

    @Override
    public void onPause() {
        isVisible = false;
        super.onPause();
    }

    protected boolean ismHasLoadedOnce() {
        return true;
    }

    protected abstract T createPresenter();


    /**
     * 初始化view
     * initViews
     *
     * @since 1.0
     */
    protected abstract void initViews();

    /**
     * 初始化数据
     * initData
     *
     * @since 1.0
     */
    protected abstract void initData(Bundle savedInstanceState);

    protected View findViewById(int id) {
        return mRootView.findViewById(id);
    }

    /**
     * F
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}
