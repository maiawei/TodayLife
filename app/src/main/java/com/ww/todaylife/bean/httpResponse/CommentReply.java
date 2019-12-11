package com.ww.todaylife.bean.httpResponse;

/**
 * created by wang.wei on 2019-12-10
 */
public class CommentReply {
    public long id;
    public long digg_count;
    public String text;
    public String user_name;
    public String user_id;
    public long create_time;
    public String user_profile_image_url;
    public int is_pgc_author;
    public ReplyUser user;
    public Comment reply_to_comment;
}
