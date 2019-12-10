package com.ww.todaylife.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ww.todaylife.bean.ChanelCategory;
import com.ww.todaylife.fragment.VideoListFragment;

import java.util.List;

public class VideoTabPagerAdapter extends FragmentStatePagerAdapter {

    private List<ChanelCategory> mChannels;

    @SuppressLint("WrongConstant")
    public VideoTabPagerAdapter(List<ChanelCategory> channelList, FragmentManager fm) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mChannels = channelList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return VideoListFragment.newInstance(mChannels.get(position).typeCode);
    }

    @Override
    public int getCount() {
        return mChannels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).name;
    }
}
