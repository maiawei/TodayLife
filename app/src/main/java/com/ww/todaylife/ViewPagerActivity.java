package com.ww.todaylife;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.base.BaseSwipeActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class ViewPagerActivity extends BaseSwipeActivity {
    @BindView(R.id.viewPage)
    ViewPager viewPage;
    ArrayList<Fragment> mListFragment=new ArrayList<>();
    @Override
    public int setContentId() {
        return R.layout.viewpager_layout;
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

    }

}
