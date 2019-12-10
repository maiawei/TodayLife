package com.ww.todaylife;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.ww.commonlibrary.util.SystemUtils;
import com.ww.todaylife.base.BaseSwipeActivity;
import com.ww.todaylife.fragment.HistoryOrStarNewsFragment;
import com.ww.todaylife.presenter.NewsListPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by wang.wei on 2019-12-09
 */
public class HistoryAndStarNewsActivity extends BaseSwipeActivity {
    @BindView(R.id.tabLayout)
    XTabLayout tabLayout;
    @BindView(R.id.viewPage)
    ViewPager viewPage;
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.toolbarLayout)
    LinearLayout toolbarLayout;
    private String[] type = {"我的收藏", "阅读历史"};
    private int currentPosition;

    @Override
    public int setContentId() {
        return R.layout.history_star_activity_layout;
    }

    @Override
    protected NewsListPresenter createPresenter() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        currentPosition = getIntent().getIntExtra("position", 0);
        viewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return HistoryOrStarNewsFragment.newInstance("star");
                } else {
                    return HistoryOrStarNewsFragment.newInstance("history");
                }
            }

            @Override
            public int getCount() {
                return type.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return type[position];
            }
        });
        tabLayout.setupWithViewPager(viewPage);
        viewPage.setCurrentItem(currentPosition);
    }

    @Override
    public void initView() {
        setToolbar(toolbarLayout);
        SystemUtils.setWindowStatusBarColor(this, R.color.toolbar_bg);
        findViewById(R.id.titleImg).setVisibility(View.GONE);
        titleTv.setText("收藏/历史");
    }


    @OnClick(R.id.backImg)
    public void onViewClicked() {
        this.finish();
    }
}
