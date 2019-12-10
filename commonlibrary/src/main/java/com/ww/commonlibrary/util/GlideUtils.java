package com.ww.commonlibrary.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ww.commonlibrary.R;

public class GlideUtils {
    public static void loadImageList(Context context,String url, ImageView target){
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_default)
                .dontAnimate()
                .centerCrop()
                .into(target);
    }
    //圆形
    public static void  loadCircleImage(Context context,String url, ImageView target){
        Glide.with(context).load(url).placeholder(R.mipmap.ic_default)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(target);
    }

}
