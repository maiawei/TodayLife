package com.ww.todaylife.bean.httpResponse;

import com.ww.todaylife.bean.BaseResponse;

import java.util.List;

public class CommentResponse extends BaseResponse {
    public int total_number;
    public boolean has_more;
    public List<CommentData> data;
}
