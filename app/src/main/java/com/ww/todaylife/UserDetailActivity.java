package com.ww.todaylife;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.bumptech.glide.Glide;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.util.StringUtils;
import com.ww.commonlibrary.util.SystemUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.commonlibrary.view.MyScrollView;
import com.ww.todaylife.base.BaseSwipeActivity;
import com.ww.todaylife.bean.httpResponse.mediaUser.MediaUserBean;
import com.ww.todaylife.bean.httpResponse.mediaUser.MediaUserData;
import com.ww.todaylife.bean.httpResponse.mediaUser.TopTab;
import com.ww.todaylife.presenter.Iview.IUserDetailView;
import com.ww.todaylife.presenter.UserPresenter;

import butterknife.BindView;

/**
 * created by wang.wei on 2019-11-28
 */
public class UserDetailActivity extends BaseSwipeActivity<UserPresenter> implements IUserDetailView {
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.titleImg)
    CircleImageView titleImg;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.moreImg)
    ImageView moreImg;
    @BindView(R.id.toolbarLayout)
    LinearLayout toolbarLayout;
    @BindView(R.id.headerImage)
    ImageView headerImage;
    @BindView(R.id.mediaAvatar)
    CircleImageView mediaAvatar;
    @BindView(R.id.mediaName)
    TextView mediaName;
    @BindView(R.id.mediaSureInfo)
    TextView mediaSureInfo;
    @BindView(R.id.tabLayout)
    XTabLayout tabLayout;
    @BindView(R.id.scrollView)
    MyScrollView mScrollview;
    @BindView(R.id.followCount)
    TextView followCount;
    @BindView(R.id.fansCount)
    TextView fansCount;
    @BindView(R.id.tabLayout2)
    XTabLayout tabLayout2;
    @BindView(R.id.divider)
    View divider;
    int[] tabLocation = new int[2];
    int[] headerLocation = new int[2];
    int currentTabPosition = 0;

    @Override
    public int setContentId() {
        return R.layout.media_detail_layout;
    }

    @Override
    protected UserPresenter createPresenter() {
        return new UserPresenter(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        String userId = getIntent().getStringExtra("userId");
        mPresenter.getUserInfo(userId);
    }

    @Override
    public void initView() {
        SystemUtils.setWindowStatusBarColor(this,R.color.toolbar_bg);
        toolbarLayout.setVisibility(View.GONE);
        setToolbar(toolbarLayout);
        mScrollview.setScrollListener((l, t, oldl, oldt) -> {
            headerImage.getLocationOnScreen(headerLocation);
            tabLayout.getLocationOnScreen(tabLocation);
            dealHeight();
        });
    }

    public void dealHeight() {
        int currentH = headerLocation[1] + headerImage.getMeasuredHeight();
        //标题栏高度
        int limitH = ScreenUtils.dip2px(this, 45) + ScreenUtils.getStatusBarHeight(this);
        if (currentH <= limitH) {
            toolbarLayout.setVisibility(View.VISIBLE);
        } else {
            toolbarLayout.setVisibility(View.GONE);
        }
        if (tabLocation[1] <= limitH) {
            divider.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.GONE);
            tabLayout2.setVisibility(View.VISIBLE);
        } else {
            divider.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetNews(MediaUserBean userBean) {
        initTab(userBean.data);
        Glide.with(this).load(userBean.data.avatar_url).into(titleImg);
        Glide.with(this).load(userBean.data.big_avatar_url).into(mediaAvatar);
        titleTv.setText(userBean.data.name);
        mediaName.setText(userBean.data.name);
        if (userBean.data.user_verified) {
            mediaSureInfo.setText(userBean.data.verified_agency + "：" + userBean.data.verified_content);
        } else {
            mediaSureInfo.setText(userBean.data.description);
        }
        followCount.setText(StringUtils.getCountStr(userBean.data.followings_count));
        fansCount.setText(StringUtils.getCountStr(userBean.data.followers_count));

    }

    public void initTab(MediaUserData data) {
        for (TopTab tabItem : data.top_tab) {
            tabLayout.addTab(tabLayout.newTab().setText(tabItem.show_name));
            tabLayout2.addTab(tabLayout2.newTab().setText(tabItem.show_name));
        }
        tabLayout.getTabAt(currentTabPosition).select();
        tabLayout2.getTabAt(currentTabPosition).select();

        tabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
                tabLayout.getTabAt(currentTabPosition).select();
                tabLayout2.getTabAt(currentTabPosition).select();
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
        tabLayout2.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
                tabLayout.getTabAt(currentTabPosition).select();
                tabLayout2.getTabAt(currentTabPosition).select();
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onError(String msg) {

    }

}
