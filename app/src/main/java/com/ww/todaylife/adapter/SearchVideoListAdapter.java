package com.ww.todaylife.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.base.BaseAdapter;
import com.ww.commonlibrary.util.TimeUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.todaylife.R;
import com.ww.todaylife.base.BaseViewHolder;
import com.ww.todaylife.bean.httpResponse.NewsDetail;

import java.util.List;

import butterknife.BindView;

public class SearchVideoListAdapter extends BaseAdapter<NewsDetail, SearchVideoListAdapter.VideoItemVh> {


    public SearchVideoListAdapter(Context context, List<NewsDetail> list) {
        super(context, list);
    }

    @Override
    public void bindViewHolderData(VideoItemVh holder, int position) {
        NewsDetail item = mItems.get(position);
        if (item != null) {
            holder.newsDate.setText(TimeUtils.getShortTime(item.create_time*1000));
            holder.abstractTv.setText(item.title);
            holder.authorName.setText(item.media_name);
            holder.commentCount.setText("评论" + item.comment_count);
            holder.videoDuration.setText(item.video_duration_str);
            Glide.with(mContext).load(item.media_avatar_url).into(holder.authorImg);
            if (item.image_url != null) {
                Glide.with(mContext).load(item.image_url).into(holder.newsImg1);
            } else if (!item.image_list.isEmpty()) {
                Glide.with(mContext).load(item.image_list.get(0).url).into(holder.newsImg1);
            }
            holder.abstractTv.setText(item.abstractX);

        }
    }

    @NonNull
    @Override
    public VideoItemVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.search_video_item_layout, parent, false);
        return new VideoItemVh(view);
    }


    public class VideoItemVh extends BaseViewHolder {


        @BindView(R.id.authorImg)
        CircleImageView authorImg;
        @BindView(R.id.authorName)
        TextView authorName;
        @BindView(R.id.commentCount)
        TextView commentCount;
        @BindView(R.id.abstractTv)
        TextView abstractTv;
        @BindView(R.id.videoDuration)
        TextView videoDuration;
        @BindView(R.id.newsImg1)
        ImageView newsImg1;
        @BindView(R.id.newsDate)
        TextView newsDate;
        public VideoItemVh(View itemView) {
            super(itemView);
        }
    }


}
