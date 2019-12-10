package com.ww.todaylife.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.ww.commonlibrary.base.BaseAdapter;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.todaylife.GalleryDetailActivity;
import com.ww.todaylife.R;
import com.ww.todaylife.bean.httpResponse.NewsDetail;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * by wang wei on 2019 11/27
 */
public class GalleryAdapter extends PagerAdapter {
    private ArrayList<NewsDetail> mList;
    private Context mContext;
    private LinkedList<View> mLinkedList=new LinkedList<>();
    public GalleryAdapter(ArrayList<NewsDetail> mList, Context context) {
        this.mList = mList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View containerView;
        ViewHolder viewHolder;
        if(mLinkedList.size()==0){
            containerView=LayoutInflater.from(mContext).inflate(R.layout.gallery_image_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.photoView=containerView.findViewById(R.id.photoView);
            containerView.setTag(viewHolder);
        }else {
            containerView=mLinkedList.removeFirst();
            viewHolder= (ViewHolder) containerView.getTag();
        }
        Glide.with(mContext).load(mList.get(position).img_small_url).skipMemoryCache(true).into(viewHolder.photoView);
        container.addView(containerView);
        return containerView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View containerView= (View) object;
        PhotoView photoView=((ViewHolder)containerView.getTag()).photoView;
        Bitmap bitmap = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
        bitmap.recycle();
        photoView.setImageBitmap(null);
        container.removeView(containerView);
        mLinkedList.add(containerView);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public class ViewHolder {
        PhotoView photoView;
    }

}

