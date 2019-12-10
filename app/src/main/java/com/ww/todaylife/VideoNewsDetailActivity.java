package com.ww.todaylife;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.todaylife.bean.httpResponse.NewsContentBean;
import com.ww.todaylife.bean.httpResponse.NewsDisplayInfo;
import com.ww.todaylife.bean.httpResponse.VideoContentBean;
import com.ww.todaylife.view.DetailJzvdStd;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;

/**
 * created by wang.wei on 2019-12-06
 */
public class VideoNewsDetailActivity extends NewsDetailBaseActivity {
    @BindView(R.id.jzPlayer)
    DetailJzvdStd jzPlayer;
    @BindView(R.id.authorAvatar)
    CircleImageView authorAvatar;
    @BindView(R.id.authorName)
    TextView authorName;
    @BindView(R.id.authorInfoLayout)
    RelativeLayout authorInfoLayout;
    @BindView(R.id.onBackImg)
    ImageView onBackImg;


    @Override
    public int setContentId() {
        return R.layout.video_news_detail_activity_layout;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        // has play
        if (newsDetail.video_source != null) {
            jzPlayer.setSource(newsDetail.video_source);
            jzPlayer.startVideo();
        } else {
            if (newsDetail.video_detail_info != null) {
                Glide.with(this).load(newsDetail.video_detail_info.detail_video_large_image.url).into(jzPlayer.thumbImageView);
            } else if (newsDetail.middle_image_url != null) {
                Glide.with(this).load(newsDetail.middle_image_url).into(jzPlayer.thumbImageView);
            }
            if (newsDetail.video_id != null) {
                mPresenter.getVideoContent(newsDetail.video_id);
            } else if (newsDetail.display != null) {
                // display字段类型不确定
                Gson gson = new Gson();
                String jsonString = gson.toJson((LinkedHashMap) newsDetail.display);
                NewsDisplayInfo displayInfo = gson.fromJson(jsonString, NewsDisplayInfo.class);
                if (displayInfo != null && displayInfo.self_info != null)
                    mPresenter.getVideoContent(displayInfo.self_info.vid);
            }
        }
    }

    @Override
    public void initView() {
        super.initView();
        setStatusView(findViewById(R.id.statusBarView), R.color.black);
    }


    @OnClick({R.id.onBackImg, R.id.authorInfoLayout})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.onBackImg) {
            this.finish();
        } else {
            gotoUserDetail();
        }
    }

    @Override
    public void onGetNews(String html, NewsContentBean newsContentBean) {
        if (loadingView != null) {
            loadingView.hide();
        }
        newsDetail.htmlString=html;
        updateNewsDetail();
        authorInfoLayout.setVisibility(View.VISIBLE);
        userId = String.valueOf(newsContentBean.data.creator_uid);
        if (newsContentBean.data.media_user != null) {
            Glide.with(this).load(newsContentBean.data.media_user.avatar_url).into(authorAvatar);
            authorName.setText(newsContentBean.data.media_user.screen_name);
        }
    }

    @Override
    public void onGetVideoContent(VideoContentBean videoContent) {
        jzPlayer.setSource(videoContent.data.video_source);
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();

    }
}
