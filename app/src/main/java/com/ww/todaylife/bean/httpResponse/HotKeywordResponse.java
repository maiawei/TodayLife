package com.ww.todaylife.bean.httpResponse;

import com.ww.todaylife.bean.BaseResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HotKeywordResponse extends BaseResponse {

    public String message;
    public DataBean data;


    public static class DataBean implements Serializable {

        public List<String> suggest_words;
        public ArrayList<HotKeyword> suggest_word_list;

    }
}
