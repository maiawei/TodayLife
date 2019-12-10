package com.ww.todaylife.bean.eventBean;

import com.ww.todaylife.bean.httpResponse.NewsDetail;

import java.util.List;

public class NewsIntentEvent {

    public NewsIntentEvent(List<NewsDetail> newsList,int currentPosition) {
        this.newsList = newsList;
        this.currentPosition=currentPosition;
    }

    public List<NewsDetail> newsList;
    public int currentPosition;
}
