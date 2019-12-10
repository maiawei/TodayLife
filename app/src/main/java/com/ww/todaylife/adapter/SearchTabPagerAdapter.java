package com.ww.todaylife.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ww.todaylife.bean.ChanelCategory;
import com.ww.todaylife.fragment.VideoListFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchTabPagerAdapter extends FragmentStatePagerAdapter {

    private List<ChanelCategory> mChannels;
    private ArrayList<Fragment> fragments;
    @SuppressLint("WrongConstant")
    public SearchTabPagerAdapter(ArrayList<Fragment> fragments,List<ChanelCategory> channelList, FragmentManager fm) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mChannels = channelList;
        this.fragments=fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
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
