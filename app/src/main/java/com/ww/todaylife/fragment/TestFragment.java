package com.ww.todaylife.fragment;

import android.os.Bundle;
import android.util.Log;

import com.ww.todaylife.R;
import com.ww.todaylife.base.BaseFragment;
import com.ww.todaylife.base.BasePresenter;

public class TestFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        Log.e("testFragmnet","initViews");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e("testFragmnet","onDestroy");
        super.onDestroy();
    }
}
