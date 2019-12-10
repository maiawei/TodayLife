package com.ww.todaylife.bean.httpResponse;


import com.ww.todaylife.bean.BaseResponse;

public class NewsContentBean extends BaseResponse {



    public CkBean _ck;
    public DataBean data;
    public boolean success;


    public static class CkBean {
    }

    public static class DataBean {

        public String detail_source;
        public MediaUserBean media_user;
        public int publish_time;
        public String title;
        public String url;
        public boolean is_original;
        public boolean is_pgc_article;
        public String content;
        public String source;
        public int video_play_count;
        public long creator_uid;


        public static class MediaUserBean {

            public String avatar_url;
            public long id;
            public String screen_name;


        }
    }
}
