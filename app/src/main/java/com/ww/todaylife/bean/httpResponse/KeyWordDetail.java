package com.ww.todaylife.bean.httpResponse;

import com.ww.todaylife.bean.BaseResponse;

import java.io.Serializable;

public class KeyWordDetail extends BaseResponse {
    public String keyword;
    public Info info;

    public static class Info implements Serializable {
        String wordid;
    }

}
