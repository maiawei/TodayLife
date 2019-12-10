package com.ww.todaylife.util;

import android.util.Base64;

import com.ww.todaylife.bean.httpResponse.FilterWords;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.bean.httpResponse.VideoContentBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class DataProcessUtils {

    //处理 item.id无效的新闻
    public static void dealNewsData(List<NewsDetail> newsList) {
        Iterator<NewsDetail> it = newsList.iterator();
        while (it.hasNext()) {
            NewsDetail detail = it.next();
            if (detail.item_id == 0) {
                it.remove();
            }
        }
    }

    //获取屏蔽的关键字
    public static ArrayList<String> dealHiddenWords(List<FilterWords> newsList) {
        ArrayList<String> words = new ArrayList<>();
        Iterator<FilterWords> it = newsList.iterator();
        while (it.hasNext()) {
            FilterWords filterWords = it.next();
            if (filterWords.name.contains("不想看")) {
                words.add(filterWords.name.replace("不想看:", ""));
            }
        }
        return words;
    }

    //处理 重复历史记录最多保存前12条数据
    public static ArrayList<String> dealRepeatSearchHistoryData(List<String> newsList) {
        LinkedHashSet linkedHashSet = new LinkedHashSet(newsList);
        ArrayList<String> strList = new ArrayList<String>(linkedHashSet);
        if (strList.size() > 12) {
            strList.subList(0, 13);
        }
        return strList;
    }

    //获取视频三种清晰度url
    public static void getVideoRealUrl(VideoContentBean bean) {
        VideoContentBean.DataBean.VideoListBean videoListBean = bean.data.video_list;
        if (videoListBean.video_3 != null) {
            String base64 = videoListBean.video_3.main_url;
            String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
            bean.data.video_source = url;

        } else if (videoListBean.video_2 != null) {
            String base64 = videoListBean.video_2.main_url;
            String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
            bean.data.video_source = url;

        } else if (videoListBean.video_1 != null) {
            String base64 = videoListBean.video_1.main_url;
            String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
            bean.data.video_source = url;
        }
    }

    public static <T> void swap(List<T> list, int oldPosition, int newPosition) {

        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        }
        if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
    }
    //处理 重复历史news
    public static ArrayList<NewsDetail> dealRepeatNews(List<NewsDetail> newsList) {
        LinkedHashSet linkedHashSet = new LinkedHashSet(newsList);
        return new ArrayList<NewsDetail>(linkedHashSet);
    }
}
