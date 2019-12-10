package com.ww.todaylife.presenter.Iview;

import com.ww.todaylife.bean.httpResponse.SearchResponse;

public interface ISearchListView extends INewsListView {
    void onSearchNewsList(SearchResponse response, int loadType);
}
