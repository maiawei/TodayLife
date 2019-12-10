package com.ww.todaylife.bean.httpResponse;

import com.ww.todaylife.bean.BaseResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchResponse extends BaseResponse {
    public int count;
    public int return_count;
    public String query_id;
    public int has_more;
    public String request_id;
    public String search_id;
    public long cur_ts;
    public int offset;
    public String message;
    public String pd;
    public int show_tabs;
    public String keyword;
    public String city;
    public List<String> tokens;
    public List<NewsDetail> data;
    public int latency;
    public int search_type;
    public int temp_type;
    public ArrayList<TabBean> tab_list;

  public static class TabBean implements Serializable  {
      public String from;
      public String key;
      public String value;
  }



}
