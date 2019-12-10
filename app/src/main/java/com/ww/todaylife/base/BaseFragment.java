package com.ww.todaylife.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by wang.wei on 2016/1/18.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements LifecycleOwner {
    public View mRootView;
    public BaseSwipeActivity mBaseActivity;
    public T mPresenter;
    private Unbinder unbinder;
    public abstract int getLayoutId();

    protected abstract void initViews();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity = (BaseSwipeActivity) getActivity();
        mPresenter = createPresenter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            unbinder = ButterKnife.bind(this, mRootView);
        } else {
            mRootView = addView(inflater);
            unbinder = ButterKnife.bind(this,mRootView);
            this.initViews();
            this.initData(savedInstanceState);
        }
        return mRootView;
    }

    private View addView(LayoutInflater inflater) {
        return inflater.inflate(getLayoutId(), null, false);
    }

    @Override
    public void startActivity(Intent intent) {
        mBaseActivity.startActivity(intent);
    }


    @Override
    public void onDestroy() {
        if(mPresenter!=null)
        mPresenter.removeView();
        unbinder.unbind();
        super.onDestroy();
    }

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract T createPresenter();

    public <V extends View> V findViewById(int id) {
        return mRootView.findViewById(id);
    }
}
