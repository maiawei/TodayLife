package com.ww.todaylife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;
import com.ww.commonlibrary.glideloader.GlideImageLoader;
import com.ww.commonlibrary.view.CircleProgressView;
import com.ww.todaylife.R;
import com.ww.todaylife.base.RecyclingPagerAdapter;

import java.util.ArrayList;

public class GalleryImageAdapter extends RecyclingPagerAdapter {

    private final LayoutInflater inflater;
    private Context mContext;
    private ArrayList<String> mList;

    public GalleryImageAdapter(Context context, ArrayList<String> urlList) {
        this.mContext = context;
        mList = urlList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        AdapterViewHolder holder;
        if (view != null) {
            holder = (AdapterViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.gallery_item_fragment_layout, container, false);
            holder = new AdapterViewHolder(view);
            view.setTag(holder);
        }
        new GlideImageLoader(holder.photoView,holder.scaleImageView,
                holder.progressBar,mContext).load(mList.get(position));
        return view;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public static class AdapterViewHolder {
        public final PhotoView photoView;
        public final CircleProgressView progressBar;
        public final SubsamplingScaleImageView scaleImageView;
        public AdapterViewHolder(View view) {
            photoView = view.findViewById(R.id.photoView);
            scaleImageView = view.findViewById(R.id.scaleView);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }
}
