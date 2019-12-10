package com.ww.todaylife.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.ww.todaylife.bean.ChanelCategory;
import com.ww.todaylife.fragment.NewsListFragment;
import com.ww.todaylife.fragment.VideoListFragment;

import java.util.ArrayList;
import java.util.List;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private List<ChanelCategory> mChannels;

    @SuppressLint("WrongConstant")
    public TabPagerAdapter(List<ChanelCategory> channelList, FragmentManager fm) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mChannels = channelList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (mChannels.get(position).typeCode.equals("video")) {
            return VideoListFragment.newInstance(mChannels.get(position).typeCode);
        } else {
            return NewsListFragment.newInstance(mChannels.get(position).typeCode);
        }
    }

    @Override
    public int getCount() {
        return mChannels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).name;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void refreshData( List<ChanelCategory> titleList) {
        this.mChannels = titleList;
        notifyDataSetChanged();
    }

}
