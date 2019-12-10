package com.ww.todaylife;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.util.UiUtils;
import com.ww.commonlibrary.view.NoScrollViewpager;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.base.BaseSwipeActivity;
import com.ww.todaylife.base.LazyFragment;
import com.ww.todaylife.bean.eventBean.OnBackEvent;
import com.ww.todaylife.fragment.HSVideoFragment;
import com.ww.todaylife.fragment.HomeFragment;
import com.ww.todaylife.fragment.MeFragment;
import com.ww.todaylife.fragment.VideoFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import cn.jzvd.Jzvd;


public class MainActivity extends BaseSwipeActivity {

    @BindView(R.id.bottom_bar)
    BottomBarLayout bottomBar;
    @BindView(R.id.viewPage)
    NoScrollViewpager viewPage;
    private long backTime = 0;
    ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    public int setContentId() {
        return R.layout.main_layout;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        viewPage.setOffscreenPageLimit(1);
        viewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
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
        setBottomBar();
    }

    public void setBottomBar() {
        bottomBar.setViewPager(viewPage);
        bottomBar.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int i, int i1) {

            }
        });
    }

    @Override
    public void initView() {
        setSwipeBackEnable(false);
        fragments.add(new HomeFragment());
        fragments.add(new VideoFragment());
        fragments.add(new HSVideoFragment());
        fragments.add(new MeFragment());
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        if ((System.currentTimeMillis() - backTime) < 2000) {
            MainActivity.this.finish();
        } else {
            backTime = System.currentTimeMillis();
            Toast.makeText(this, getString(R.string.finish_hint), Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new OnBackEvent());
        }
    }
}
