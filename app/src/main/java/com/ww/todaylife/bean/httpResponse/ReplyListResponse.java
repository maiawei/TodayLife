package com.ww.todaylife.bean.httpResponse;

import com.ww.todaylife.bean.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * created by wang.wei on 2019-12-10
 */
public class ReplyListResponse extends BaseResponse {


    public String message;
    public int err_no;
    public boolean stable;
    public DataBean data;


    public static class DataBean {

        public List<CommentReply> data;
        public long id;
        public long total_count;
        public long offset;
        public boolean has_more;
    }
}
