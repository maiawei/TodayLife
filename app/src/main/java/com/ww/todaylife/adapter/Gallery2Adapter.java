package com.ww.todaylife.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.fragment.GalleryItemFragment;

import java.util.ArrayList;

/**
 * created by wang.wei on 2019-11-27
 */
public class Gallery2Adapter extends FragmentStatePagerAdapter {
    private ArrayList<String> urls;
    public Gallery2Adapter(FragmentManager fm, ArrayList<String> urls, Context mContext) {
        super(fm);
        this.urls = urls;

    }

    @Override
    public Fragment getItem(int position) {
        return GalleryItemFragment.newsInstance(urls.get(position));
    }

    @Override
    public int getCount() {
        return urls.size();
    }
}
