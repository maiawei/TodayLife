package com.ww.todaylife.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ww.commonlibrary.base.BaseAdapter;
import com.ww.commonlibrary.http.DealCallBack;
import com.ww.commonlibrary.util.StringUtils;
import com.ww.todaylife.R;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.bean.httpResponse.VideoContentBean;
import com.ww.todaylife.presenter.NewsListPresenter;
import com.ww.todaylife.view.ListJzVideo;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class VideoListAdapter extends BaseAdapter<NewsDetail, VideoListAdapter.VideoVh> {

    public VideoListAdapter(Context context, List<NewsDetail> list, NewsListPresenter newsListPresenter) {
        super(context, list);
        this.newsListPresenter = newsListPresenter;
    }

    NewsListPresenter newsListPresenter;

    @Override
    public VideoVh onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.video_item_ver_layout, parent, false);
        return new VideoVh(view);

    }

    @Override
    public void bindViewHolderData(VideoVh holder, final int position) {
        NewsDetail item = mItems.get(position);
        if (item != null) {

            holder.jzPlayer.setSource(item.video_source, item.title, StringUtils.timeToMS(item.video_duration));
            holder.commentCount.setText(String.valueOf(item.comment_count));
            holder.authorName.setText(item.media_name);
            if (item.video_detail_info != null) {
                Glide.with(mContext).load(item.video_detail_info.detail_video_large_image.url).into(holder.jzPlayer.thumbImageView);
            }
            holder.jzPlayer.setOnStartListener(() -> {
                if (item.video_source != null) {
                    holder.jzPlayer.startButton.performClick();
                    return;
                }
                //防止多次点击加载
                if (holder.jzPlayer.loadingProgressBar.getVisibility() == VISIBLE) {
                    return;
                }
                holder.jzPlayer.setAllControlsVisiblity(GONE, GONE, GONE, VISIBLE, VISIBLE, GONE, GONE);
                newsListPresenter.getVideoContent(item.video_id, new DealCallBack<VideoContentBean>() {
                    @Override
                    public void onSuccess(VideoContentBean videoContentBean) {
                        item.video_source = videoContentBean.data.video_source;
                        holder.jzPlayer.setSource(item.video_source, item.title);
                        holder.jzPlayer.startVideo();
                    }

                    @Override
                    public void onFailure(String msg) {
                        holder.jzPlayer.setAllControlsVisiblity(GONE, GONE, VISIBLE, GONE, VISIBLE, GONE, GONE);
                    }
                });
            });
        }
    }


    public class VideoVh extends RecyclerView.ViewHolder {
        public TextView commentCount, authorName;
        public ListJzVideo jzPlayer;

        public VideoVh(View itemView) {
            super(itemView);
            commentCount = itemView.findViewById(R.id.commentCount);
            authorName = itemView.findViewById(R.id.authorName);
            jzPlayer = itemView.findViewById(R.id.jzPlayer);
        }
    }


}
