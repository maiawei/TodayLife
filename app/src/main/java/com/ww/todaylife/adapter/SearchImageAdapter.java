package com.ww.todaylife.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.base.BaseAdapter;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.todaylife.GalleryDetailActivity;
import com.ww.todaylife.R;
import com.ww.todaylife.StartActivity;
import com.ww.todaylife.bean.httpResponse.NewsDetail;

import java.util.List;

public class SearchImageAdapter extends BaseAdapter<NewsDetail, SearchImageAdapter.ImageVh> {

    public SearchImageAdapter(Context context, List<NewsDetail> list) {
        super(context, list);
    }


    @Override
    public void bindViewHolderData(ImageVh holder, int position) {
        NewsDetail item = mItems.get(position);
        if (item != null) {
            ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
            float itemWidth = (ScreenUtils.getScreenWidth()-5) / 2;
            layoutParams.width = (int) itemWidth;
            layoutParams.height = layoutParams.width*item.height/item.width;
            holder.imageView.setLayoutParams(layoutParams);
            Glide.with(mContext).load(item.img_small_url).into(holder.imageView);
        }
    }

    @NonNull
    @Override
    public ImageVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageVh(mInflater.inflate(R.layout.search_image_item_layout, parent, false));
    }

    public static class ImageVh extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageVh(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageContent);
        }
    }

}
