package com.ww.todaylife.util;

import com.ww.todaylife.bean.httpResponse.NewsDetail;

import java.util.ArrayList;

/**
 * created by wang.wei on 2019-12-04
 */
public class IntentDataUtils {
    private static ArrayList<NewsDetail> news;

    public static void setNews(ArrayList<NewsDetail> list) {
        news = list;
    }

    public static ArrayList<NewsDetail> getNews() {
        return news;
    }

    public static void clearNews() {
        if (news != null) {
            news.clear();
            news = null;
        }

    }
}
