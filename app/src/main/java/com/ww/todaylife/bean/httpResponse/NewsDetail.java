package com.ww.todaylife.bean.httpResponse;

import android.view.Display;

import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.ww.todaylife.dataBase.FilterWordsConverters;
import com.ww.todaylife.dataBase.ImageListBeanConverters;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "tb_news")
@TypeConverters({ImageListBeanConverters.class, FilterWordsConverters.class})

public class NewsDetail implements Serializable {
    public int cell_type;
    @PrimaryKey(autoGenerate = true)
    public long tId;
    public String id_str;
    public String ala_src;
    //        public DataBean data;
    public long id;
    public boolean isFeedback;//用户反馈
    public boolean isHidden;//显示
    public boolean isRead;//阅读
    public boolean isStar;//收藏

    //        public DisplayBean display;
    public String media_name;
    public int repin_count;
    public int ban_comment;
    public int show_play_effective_count;
    @SerializedName("abstract")
    public String abstractX;
    public String display_title;
    public String datetime;
    public int article_type;
    public boolean more_mode;
    public long create_time;
    public boolean has_m3u8_video;
    public String keywords;
    public int has_mp4_video;
    public int favorite_count;
    public int aggr_type;
    public int article_sub_type;
    public int bury_count;
    public String title;
    public boolean has_video;
    public String share_url;
    public String source;
    public int comment_count;
    public String article_url;
    public boolean middle_mode;
    public boolean large_mode;
    public String item_source_url;
    public String media_url;
    public int display_time;
    public int publish_time;
    public int go_detail_count;
    public String item_seo_url;
    public long tag_id;
    public String source_url;
    public long item_id;
    public int natant_level;
    public String seo_url;
    public String display_url;
    public String url;
    public int level;
    public int digg_count;
    public long behot_time;
    public String tag;
    public boolean has_gallery;
    public boolean has_image;
    public long group_id;
    public int hot;
    public int topping;
    public String play_effective_count;
    public int video_duration;
    public String image_url;
    public int height;
    public int width;
    public String image_small_url;
    public String img_url;
    public String img_small_url;
    public String video_duration_str;
    public int group_flags;
    public String content;
    public String video_id;
    public String video_url;
    public String video_source;
    public String valid_video_url;
    public int gallery_pic_count;
    public int gallary_image_count;
    public int image_count;
    public String media_avatar_url;
    public String large_image_url;
    public String middle_image_url;
    public List<FilterWords> filter_words;
    public String htmlString;
    @Embedded()
    public Object display;

    //        public List<String> tokens;
    // public List<SearchNewsResultBean.DataBeanX.QueriesBean> queries;
    public List<ImageListBean> large_image_list = null;
    public List<ImageListBean> image_list = null;
    @Embedded(prefix = "m_image")
    public ImageListBean middle_image;
    public String typeCode;
    @Embedded(prefix = "video_info")
    public VideoInfoBean video_detail_info;

    @Override
    public int hashCode() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        NewsDetail newsDetail = (NewsDetail) obj;
        return newsDetail.item_id == this.item_id;
    }
}
