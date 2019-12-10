package com.ww.todaylife.bean.httpResponse;

import androidx.room.Embedded;

import java.io.Serializable;

/**
 * created by wang.wei on 2019-11-27
 */
public class VideoInfoBean implements Serializable {

    public int video_type;
    public int video_preloading_flag;
    public int direct_play;
    @Embedded(prefix = "video_thumb")
    public ImageListBean detail_video_large_image;
    public int show_pgc_subscribe;
    public String video_third_monitor_url;
    public String video_id;
    public int video_watching_count;
    public int video_watch_count;
}
