package com.ww.todaylife.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.base.BaseAdapter;
import com.ww.commonlibrary.util.StringUtils;
import com.ww.commonlibrary.util.TimeUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.todaylife.R;
import com.ww.todaylife.base.BaseViewHolder;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.view.DeletePopWindow;

import java.util.List;

import butterknife.BindView;


public class NewsList2Adapter extends BaseAdapter<NewsDetail, BaseViewHolder> {

    //无图片
    private static final int TEXT_NEWS = 0;
    //单张图片
    private static final int IMAGE_NEWS = 1;
    //多图图片
    private static final int IMAGES_NEWS = 2;
    //3张模式
    private static final int IMAGE3_NEWS = 3;
    //视频模式
    private static final int VIDEO_NEWS = 4;
    //被hidden
    private static final int HIDDEN_NEWS = 5;


    public NewsList2Adapter(Context context, List<NewsDetail> list) {
        super(context, list);
    }

    @Override
    public void bindViewHolderData(BaseViewHolder holder, int position) {
        NewsDetail item = mItems.get(position);
        if (item != null) {

            if (holder instanceof NewsItemVh) {
                NewsItemVh vh = (NewsItemVh) holder;
                bindCommonData(item, vh, position);
            }
            if (holder instanceof TextNewsVh) {
                TextNewsVh vh = (TextNewsVh) holder;
            } else if (holder instanceof ImageNewsVh) {
                ImageNewsVh vh = (ImageNewsVh) holder;
                if (item.middle_image != null) {
                    Glide.with(mContext).load(item.middle_image.url).into(vh.newsImg1);
                }
            } else if (holder instanceof Image3NewsVh) {
                Image3NewsVh vh = (Image3NewsVh) holder;
                Glide.with(mContext).load(item.image_list.get(0).url).into(vh.newsImg1);
                Glide.with(mContext).load(item.image_list.get(1).url).into(vh.newsImg2);
                Glide.with(mContext).load(item.image_list.get(2).url).into(vh.newsImg3);
            } else if (holder instanceof ImagesNewsVh) {
                ImagesNewsVh vh = (ImagesNewsVh) holder;
                if (item.middle_image != null) {
                    Glide.with(mContext).load(item.middle_image.url).into(vh.newsImg1);
                }
                vh.picCount.setText(String.format("%d图", item.gallary_image_count));
            } else if (holder instanceof VideoNewsVh) {
                VideoNewsVh vh = (VideoNewsVh) holder;
                if (item.middle_image != null) {
                    Glide.with(mContext).load(item.middle_image.url).into(vh.newsCover);
                }
                vh.durationTv.setText(StringUtils.timeToMS(item.video_duration));
            }
            if (holder instanceof HiddenVh) {
                HiddenVh hiddenVh = (HiddenVh) holder;
                if (item.isHidden && item.isFeedback) {
                    hiddenVh.contentView.setVisibility(View.GONE);
                } else {
                    hiddenVh.contentView.setVisibility(View.VISIBLE);
                }
                hiddenVh.hiddenTv.setTag(position);
                hiddenVh.cancelHidden.setOnClickListener(v -> {
                    item.isFeedback = false;
                    hiddenVh.hiddenTv.setTag(-1);
                    refreshItem(position);
                });
            }
        }
    }

    private void bindCommonData(NewsDetail item, NewsItemVh holder, int position) {
        holder.newsDate.setText(TimeUtils.getShortTime(item.behot_time * 1000));
        holder.newsTitle.setText(item.title);
        holder.abstractTv.setVisibility(View.GONE);
        holder.abstractTv.setText(item.abstractX);
        holder.authorName.setText(item.media_name == null ? item.source : item.media_name);
        holder.commentCount.setText(String.format("%d评论", item.comment_count));
        if (item.topping == 1) {
            holder.tag.setText("置顶");
            holder.tag.setVisibility(View.VISIBLE);
        } else {
            if (item.hot == 1) {
                holder.tag.setText("热点");
                holder.tag.setVisibility(View.VISIBLE);
            } else {
                holder.tag.setVisibility(View.GONE);
            }
        }
        if (item.isRead) {
            holder.newsTitle.setTextColor(mContext.getResources().getColor(R.color.black_777));
        } else {
            holder.newsTitle.setTextColor(mContext.getResources().getColor(R.color.black_222));
        }
        holder.deleteImage.setOnClickListener(v -> {
            DeletePopWindow deletePopWindow = new DeletePopWindow(mContext);
            deletePopWindow.showPop(holder.deleteImage, item, type -> {
                // type原因
                item.isFeedback = true;
                refreshItem(position);
            });
        });
    }

    @Override
    public boolean isSetItemBackground() {
        return true;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case IMAGE_NEWS:
                view = mInflater.inflate(R.layout.news_image_item_layout, parent, false);
                return new ImageNewsVh(view);
            case IMAGES_NEWS:
                view = mInflater.inflate(R.layout.news_images_item_layout, parent, false);
                return new ImagesNewsVh(view);
            case IMAGE3_NEWS:
                view = mInflater.inflate(R.layout.news_image3_item_layout, parent, false);
                return new Image3NewsVh(view);
            case VIDEO_NEWS:
                view = mInflater.inflate(R.layout.news_item_ver_layout, parent, false);
                return new VideoNewsVh(view);
            case HIDDEN_NEWS:
                view = mInflater.inflate(R.layout.hidden_news_item_layout, parent, false);
                return new HiddenVh(view);
            default:
                view = mInflater.inflate(R.layout.news_text_item_layout, parent, false);
                return new TextNewsVh(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        NewsDetail newsDetail = mItems.get(position);
        if (newsDetail.isFeedback) {
            return HIDDEN_NEWS;
        } else if (newsDetail.has_video) {
            // pic middle
            return VIDEO_NEWS;
        } else if (!newsDetail.has_image) {
            return TEXT_NEWS;
        } else if (newsDetail.gallary_image_count == 1) {
            //pic middle
            return IMAGE_NEWS;
        } else if (newsDetail.gallary_image_count < 6 && newsDetail.image_list != null && newsDetail.image_list.size() == 3) {
            // pic image_list
            return IMAGE3_NEWS;
        } else if (newsDetail.gallary_image_count > 3) {
            //pic middle
            return IMAGES_NEWS;
        } else {
            //pic middle
            return IMAGE_NEWS;
        }
    }

    public class HiddenVh extends BaseViewHolder {

        TextView hiddenTv;
        TextView cancelHidden;
        LinearLayout contentView;

        private HiddenVh(View view) {
            super(view);
            contentView = view.findViewById(R.id.contentView);
            hiddenTv = view.findViewById(R.id.hiddenContent);
            cancelHidden = view.findViewById(R.id.cancelHidden);
        }
    }

    public class NewsItemVh extends BaseViewHolder {

        @BindView(R.id.newsTitle)
        TextView newsTitle;
        @BindView(R.id.authorName)
        TextView authorName;
        @BindView(R.id.commentCount)
        TextView commentCount;
        @BindView(R.id.tag)
        TextView tag;
        @BindView(R.id.newsDate)
        TextView newsDate;
        @BindView(R.id.deleteImage)
        ImageView deleteImage;
        @BindView(R.id.abstractTv)
        TextView abstractTv;
        private NewsItemVh(View itemView) {
            super(itemView);
        }
    }

    public class TextNewsVh extends NewsItemVh {

        private TextNewsVh(View view) {
            super(view);
        }
    }

    public class ImageNewsVh extends NewsItemVh {

        ImageView newsImg1;

        private ImageNewsVh(View view) {
            super(view);
            newsImg1 = view.findViewById(R.id.newsImg1);

        }
    }

    public class Image3NewsVh extends NewsItemVh {
        ImageView newsImg1;
        ImageView newsImg2;
        ImageView newsImg3;

        private Image3NewsVh(View view) {
            super(view);
            newsImg1 = view.findViewById(R.id.newsImg1);
            newsImg2 = view.findViewById(R.id.newsImg2);
            newsImg3 = view.findViewById(R.id.newsImg3);
        }
    }

    public class ImagesNewsVh extends NewsItemVh {
        ImageView newsImg1;
        TextView picCount;

        private ImagesNewsVh(View view) {
            super(view);
            newsImg1 = view.findViewById(R.id.newsImg1);
            picCount = view.findViewById(R.id.picCount);
        }
    }

    public class VideoNewsVh extends NewsItemVh {
        ImageView newsCover;
        TextView durationTv;

        private VideoNewsVh(View view) {
            super(view);
            durationTv = view.findViewById(R.id.durationTv);
            newsCover = view.findViewById(R.id.news_cover);
        }
    }
}
