package com.ww.todaylife.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
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

        Glide.with(mContext).load(mList.get(position)).error(R.mipmap.error_picture).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.photoView);
        return view;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public static class AdapterViewHolder {
        public final PhotoView photoView;
        public final ProgressBar progressBar;
        public AdapterViewHolder(View view) {
            photoView = view.findViewById(R.id.photoView);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }
}
