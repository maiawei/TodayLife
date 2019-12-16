package com.ww.todaylife.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.ww.commonlibrary.base.BaseObserver;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.view.NoAnimationViewPager;
import com.ww.todaylife.R;
import com.ww.todaylife.SearchActivity;
import com.ww.todaylife.adapter.TabPagerAdapter;
import com.ww.todaylife.base.BaseFragment;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.base.LazyFragment;
import com.ww.todaylife.bean.ChanelCategory;
import com.ww.todaylife.bean.eventBean.NewsTabEvent;
import com.ww.todaylife.dao.ChanelDao;
import com.ww.todaylife.fragment.dialogfragment.ChanelDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import cn.jzvd.Jzvd;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.tabLayout)
    XTabLayout tabLayout;
    @BindView(R.id.viewPage)
    NoAnimationViewPager viewPage;
    @Nullable
    @BindView(R.id.tabOperationImage)
    ImageView tabOperationImage;
    @BindView(R.id.searchTV)
    TextView searchTV;
    private ArrayList<ChanelCategory> chanelList = new ArrayList<>();
    private TabPagerAdapter tabPagerAdapter;
    private ChanelDialogFragment chanelDialog;

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        mBaseActivity.setStatusView(findViewById(R.id.statusBarView), R.color.main_red);
        tabLayout.post(() -> {
            ViewGroup lastItem = (ViewGroup) tabLayout.getChildAt(tabLayout.getChildCount() - 1);
            if (lastItem != null)
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
        tabPagerAdapter = new TabPagerAdapter(chanelList, getChildFragmentManager());
        viewPage.setAdapter(tabPagerAdapter);
        tabLayout.setupWithViewPager(viewPage);
        initChanel();
    }


    private void initChanel() {
        chanelList = (ArrayList<ChanelCategory>) ChanelDao.getInstance().getSelectedChanel(mBaseActivity);
        if (chanelList==null) {
            chanelList = new ArrayList<>();
            String[] newsCategory = getResources().getStringArray(R.array.news_category);
            String[] newsCategoryCode = getResources().getStringArray(R.array.category_code);
            for (int i = 0; i < newsCategory.length; i++) {
                ChanelCategory liveCategory = new ChanelCategory();
                liveCategory.name = newsCategory[i];
                liveCategory.isSelected = true;
                liveCategory.typeCode = newsCategoryCode[i];
                chanelList.add(liveCategory);
            }
            ChanelDao.getInstance().saveSelectedChanel(chanelList, mBaseActivity);
        }
        tabPagerAdapter.refreshData(chanelList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NewsTabEvent event) {
        LogUtils.e(Thread.currentThread().getName() + "mainThread");
        chanelList.clear();
        chanelList.addAll(event.list);
        tabPagerAdapter.refreshData(chanelList);
        ChanelDao.getInstance().saveSelectedChanel(chanelList, mBaseActivity);

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Optional
    @OnClick({R.id.tabOperationImage, R.id.searchTV})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.searchTV) {
            mBaseActivity.startActivityWithAnimation(new Intent(mBaseActivity, SearchActivity.class), R.anim.main_activity_in);
        } else if (view.getId() == R.id.tabOperationImage) {
            if (chanelDialog == null)
                chanelDialog = new ChanelDialogFragment();
            chanelDialog.show(getChildFragmentManager(), "ChanelDialogFragment");

        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
