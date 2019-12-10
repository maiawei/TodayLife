package com.ww.todaylife.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ww.todaylife.bean.ChanelCategory;

import java.util.List;

/**
 * created by wang.wei on 2019-11-28
 */
public class FixedTabPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> titles;

    public FixedTabPagerAdapter(List<Fragment> fragmentList, List<String> titleList, FragmentManager fm) {
        super(fm);
        mFragments = fragmentList;
        titles = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}
