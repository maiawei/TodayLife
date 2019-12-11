package com.ww.todaylife.presenter.Iview;

import com.ww.todaylife.bean.httpResponse.NewsDetail;

import java.util.List;

public interface INewsListView {
    void onGetNewsList(List<NewsDetail> mList, int loadType);
    void onError(String msg);
    void onGetNewsLoadMoreList(List<NewsDetail> mList, int loadType);
}
