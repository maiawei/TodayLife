package com.ww.todaylife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.google.android.material.tabs.TabLayout;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.view.NoAnimationViewPager;
import com.ww.todaylife.R;
import com.ww.todaylife.SearchActivity;
import com.ww.todaylife.adapter.TabPagerAdapter;
import com.ww.todaylife.adapter.VideoTabPagerAdapter;
import com.ww.todaylife.base.BaseFragment;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.bean.ChanelCategory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import cn.jzvd.Jzvd;

public class VideoFragment extends BaseFragment {
    @BindView(R.id.tabLayout)
    XTabLayout tabLayout;
    @BindView(R.id.viewPage)
    NoAnimationViewPager viewPage;
    @BindView(R.id.toolbarLayout)
    LinearLayout toolbarLayout;
    @BindView(R.id.tabOperationImage)
    ImageView tabOperationImage;
    private ArrayList<ChanelCategory> liveCategories = new ArrayList<>();
    private VideoTabPagerAdapter tabPagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment;
    }
    @Override
    protected void initViews() {
        mBaseActivity.setStatusView(findViewById(R.id.statusBarView), R.color.main_grey);
        toolbarLayout.setVisibility(View.GONE);
        tabOperationImage.setImageResource(R.mipmap.ic_search);
        tabLayout.post(() -> {
            ViewGroup lastItem = (ViewGroup) tabLayout.getChildAt(tabLayout.getChildCount() - 1);
            lastItem.setPadding(0, 0, ScreenUtils.dip2px(mBaseActivity, 50), 0);
        });
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Jzvd.releaseAllVideos();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tabPagerAdapter = new VideoTabPagerAdapter(liveCategories, getChildFragmentManager());
        String[] videoCategory = getResources().getStringArray(R.array.video_category);
        String[] videoCategoryCode = getResources().getStringArray(R.array.videoCategory_code);
        for (int i = 0; i < videoCategory.length; i++) {
            ChanelCategory liveCategory = new ChanelCategory();
            liveCategory.name = videoCategory[i];
            liveCategory.typeCode = videoCategoryCode[i];
            liveCategories.add(liveCategory);
        }
        viewPage.setAdapter(tabPagerAdapter);
        tabLayout.setupWithViewPager(viewPage);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Optional
    @OnClick(R.id.tabOperationImage)
    public void onViewClicked() {
        startActivity(new Intent(mBaseActivity, SearchActivity.class));
    }
}
