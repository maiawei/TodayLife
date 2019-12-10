package com.ww.todaylife.adapter;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.base.BaseAdapter;
import com.ww.commonlibrary.util.GlideUtils;
import com.ww.commonlibrary.util.TimeUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.todaylife.R;
import com.ww.todaylife.base.BaseViewHolder;
import com.ww.todaylife.bean.httpResponse.NewsDetail;

import java.util.List;

import butterknife.BindView;

public class SearchListAdapter extends BaseAdapter<NewsDetail, SearchListAdapter.NewsItemVh> {

    //无图片
    private static final int TEXT_NEWS = 0;
    //单张图片
    private static final int IMAGE_NEWS = 1;
    //多张
    private static final int IMAGE3_NEWS = 2;


    public SearchListAdapter(Context context, List<NewsDetail> list) {
        super(context, list);
    }

    @Override
    public void bindViewHolderData(NewsItemVh holder, int position) {
        NewsDetail item = mItems.get(position);
        if (item != null) {
            holder.newsDate.setText(TimeUtils.getShortTime(item.create_time*1000));
            holder.newsTitle.setText(item.title);
            holder.authorName.setText(item.media_name);
            holder.commentCount.setText(String.format("评论%d", item.comment_count));
            GlideUtils.loadCircleImage(mContext,item.media_avatar_url,holder.authorImg);
            if (holder instanceof TextNewsVh) {
                TextNewsVh vh = (TextNewsVh) holder;
                vh.abstractTv.setText(item.abstractX);
            } else if (holder instanceof ImageNewsVh) {
                ImageNewsVh vh = (ImageNewsVh) holder;
                vh.abstractTv.setText(item.abstractX);
                if (item.image_url != null) {
                    GlideUtils.loadImageList(mContext,item.image_url,vh.newsImg1);
                } else if (!item.image_list.isEmpty()) {
                    GlideUtils.loadImageList(mContext,item.image_list.get(0).url,vh.newsImg1);
                }
            } else {
                Images3NewsVh vh = (Images3NewsVh) holder;
                vh.abstractTv.setText(item.abstractX);
                GlideUtils.loadImageList(mContext,item.image_list.get(0).url,vh.newsImg1);
                GlideUtils.loadImageList(mContext,item.image_list.get(1).url,vh.newsImg2);
                GlideUtils.loadImageList(mContext,item.image_list.get(2).url,vh.newsImg3);

            }
        }
    }

    @NonNull
    @Override
    public NewsItemVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TEXT_NEWS) {
            view = mInflater.inflate(R.layout.search_news_text_item_layout, parent, false);
            return new TextNewsVh(view);
        } else if (viewType == IMAGE_NEWS) {
            view = mInflater.inflate(R.layout.search_news_image_item_layout, parent, false);
            return new ImageNewsVh(view);
        } else {
            view = mInflater.inflate(R.layout.search_news_image3_item_layout, parent, false);
            return new Images3NewsVh(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        NewsDetail newsDetail = mItems.get(position);
        if (newsDetail.image_count <=0 ) {
            return TEXT_NEWS;
        } else if(newsDetail.image_count < 8 && newsDetail.image_list != null &&newsDetail.image_list.size() >2){
            return IMAGE3_NEWS;
        } else {
            return IMAGE_NEWS;
        }
    }

    public class NewsItemVh extends BaseViewHolder {

        @BindView(R.id.newsTitle)
        TextView newsTitle;
        @BindView(R.id.authorImg)
        CircleImageView authorImg;
        @BindView(R.id.authorName)
        TextView authorName;
        @BindView(R.id.commentCount)
        TextView commentCount;
        @BindView(R.id.newsDate)
        TextView newsDate;


        public NewsItemVh(View itemView) {
            super(itemView);
        }
    }

    public class TextNewsVh extends NewsItemVh {

        TextView abstractTv;

        private TextNewsVh(View view) {
            super(view);
            abstractTv = view.findViewById(R.id.abstractTv);
        }
    }

    public class ImageNewsVh extends NewsItemVh {

        ImageView newsImg1;
        TextView abstractTv;

        private ImageNewsVh(View view) {
            super(view);
            abstractTv = view.findViewById(R.id.abstractTv);
            newsImg1 = view.findViewById(R.id.newsImg1);

        }
    }

    public class Images3NewsVh extends NewsItemVh {
        ImageView newsImg1;
        TextView abstractTv;
        ImageView newsImg2;
        ImageView newsImg3;

        private Images3NewsVh(View view) {
            super(view);
            abstractTv = view.findViewById(R.id.abstractTv);
            newsImg1 = view.findViewById(R.id.newsImg1);
            newsImg2 = view.findViewById(R.id.newsImg2);
            newsImg3 = view.findViewById(R.id.newsImg3);
        }
    }

}
