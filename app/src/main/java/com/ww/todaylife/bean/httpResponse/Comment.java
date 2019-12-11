package com.ww.todaylife.bean.httpResponse;

import java.io.Serializable;
import java.util.ArrayList;

public class Comment implements Serializable {
    public long id;
    public String text;
    public int digg_count;
    public String user_name;
    public String user_profile_image_url;
    public long create_time;
    public long user_id;
    public long reply_count;
    public ArrayList<CommentReply> reply_list;
}
