package com.ww.todaylife.fragment;

import android.os.Bundle;

import com.ww.todaylife.R;
import com.ww.todaylife.base.BaseFragment;
import com.ww.todaylife.base.BasePresenter;

/**
 * created by wang.wei on 2019-11-28
 */
public class DynamicFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.dynamic_fragment_layout;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
