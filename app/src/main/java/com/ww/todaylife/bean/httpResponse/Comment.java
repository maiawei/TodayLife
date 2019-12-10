package com.ww.todaylife.bean.httpResponse;

import java.io.Serializable;

public class Comment implements Serializable {

    public String text;
    public int digg_count;
    public String user_name;
    public String user_profile_image_url;
    public long create_time;
    public long user_id;
}
