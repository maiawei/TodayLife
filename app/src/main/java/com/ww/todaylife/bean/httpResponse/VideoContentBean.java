package com.ww.todaylife.bean.httpResponse;

import androidx.room.Embedded;

import com.ww.todaylife.bean.BaseResponse;

import java.util.List;


public class VideoContentBean extends BaseResponse {


    public DataBean data;
    public String message;
    public int code;
    public int total;
    public int group_flags;
    public int video_type;
    public int video_preloading_flag;
    public int direct_play;
    public ImageListBean detail_video_large_image;
    public int show_pgc_subscribe;
    public String video_third_monitor_url;
    public String video_id;
    public int video_watching_count;
    public int video_watch_count;

    public static class DataBean {

        public String video_source;
        public int status;
        public String user_id;
        public String video_id;
        public double video_duration;
        public VideoListBean video_list;
        public List<BigThumbsBean> big_thumbs;


        public static class VideoListBean {


            public Video1Bean video_1;
            public Video2Bean video_2;
            public Video3Bean video_3;


            public static class Video1Bean {


                public String backup_url_1;
                public int bitrate;
                public String definition;
                public String main_url;
                public int preload_interval;
                public int preload_max_step;
                public int preload_min_step;
                public int preload_size;
                public int size;
                public int socket_buffer;
                public int user_video_proxy;
                public int vheight;
                public String vtype;
                public int vwidth;

            }

            public static class Video2Bean {

                public String backup_url_1;
                public int bitrate;
                public String definition;
                public String main_url;
                public int preload_interval;
                public int preload_max_step;
                public int preload_min_step;
                public int preload_size;
                public int size;
                public int socket_buffer;
                public int user_video_proxy;
                public int vheight;
                public String vtype;
                public int vwidth;

            }

            public static class Video3Bean {

                public String backup_url_1;
                public int bitrate;
                public String definition;
                public String main_url;
                public int preload_interval;
                public int preload_max_step;
                public int preload_min_step;
                public int preload_size;
                public int size;
                public int socket_buffer;
                public int user_video_proxy;
                public int vheight;
                public String vtype;
                public int vwidth;


            }
        }

        public static class BigThumbsBean {


            public int img_num;
            public String img_url;
            public int img_x_size;
            public int img_y_size;
            public int img_x_len;
            public int img_y_len;

        }
    }
}
