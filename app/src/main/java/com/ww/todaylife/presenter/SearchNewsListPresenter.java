package com.ww.todaylife.presenter;

import android.text.TextUtils;

import com.ww.commonlibrary.http.ServiceGenerator;
import com.ww.commonlibrary.base.BaseObserver;
import com.ww.todaylife.bean.httpResponse.SearchResponse;
import com.ww.todaylife.presenter.Iview.ISearchListView;
import com.ww.todaylife.util.DataProcessUtils;
import com.ww.todaylife.api.NewsApi;

public class SearchNewsListPresenter extends NewsListPresenter {

    public SearchNewsListPresenter(ISearchListView mView) {
        super(mView);
        this.mView = mView;
    }

    public void getSearchNewsList(String keyword, String offset, String from, int loadType) {
        String pd;
        if (from.equals("news")) {
            pd = "information";
        } else if (from.equals("gallery")) {
            pd = "atlas";
        } else {
            pd = from;
        }
        addDisposable(ServiceGenerator.createService(NewsApi.class).getSearchContent(keyword, offset, from, pd).doOnNext(searchResponse -> {
            if (searchResponse.data != null) {
                if (!TextUtils.equals(from, "gallery")) {
                    DataProcessUtils.dealNewsData(searchResponse.data);
                }
            }

        }), new BaseObserver<SearchResponse>() {
            @Override
            public void success(SearchResponse searchResponse) {
                ((ISearchListView) mView).onSearchNewsList(searchResponse, loadType);
            }

            @Override
            public void failure() {
                mView.onError("");
            }
        });
    }
}
