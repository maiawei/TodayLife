package com.ww.todaylife;

import android.content.Intent;
import android.os.Bundle;

import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.base.BaseSwipeActivity;

/**
 * created by wang.wei on 2019-12-06
 */
public class TestActivity extends BaseSwipeActivity {
    @Override
    public int setContentId() {
        return R.layout.start_activity_layout;

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        //startActivity(new Intent(this, MainActivity.class));

    }
}
