package com.ww.todaylife.presenter;

import com.google.gson.Gson;
import com.ww.commonlibrary.http.ServiceGenerator;
import com.ww.commonlibrary.base.BaseObserver;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.bean.BaseResponse;
import com.ww.todaylife.bean.httpResponse.HsVideoRootBean;
import com.ww.todaylife.bean.httpResponse.MultiNewsArticleBean;
import com.ww.todaylife.api.NewsApi;
import com.ww.todaylife.presenter.Iview.IHSVideoListView;
import com.ww.todaylife.util.PreUtils;

import java.util.ArrayList;
import java.util.List;

public class HsVideoListPresenter extends BasePresenter<IHSVideoListView, BaseResponse> {
    private long lastTime;

    public HsVideoListPresenter(IHSVideoListView mView) {
        super(mView);
    }

    public void getNewsList(int loadType) {
        lastTime = PreUtils.getLong("hotsoon_video", 0);
        if (lastTime == 0) {
            lastTime = System.currentTimeMillis() / 1000;
        }
        addDisposable(ServiceGenerator.createService(NewsApi.class).getHSVideoNewsList("hotsoon_video",  lastTime,System.currentTimeMillis() / 1000).doOnNext(responseData -> {
            List<MultiNewsArticleBean.DataBean> dataBeans = responseData.data;
            List<HsVideoRootBean> newsList = new ArrayList<>();
            if (dataBeans.size() != 0) {
                for (MultiNewsArticleBean.DataBean newsData : dataBeans) {
                    HsVideoRootBean news = new Gson().fromJson(newsData.content, HsVideoRootBean.class);
                    newsList.add(news);
                }
            }
            responseData.video = newsList;
        }), new BaseObserver<MultiNewsArticleBean>() {
            @Override
            public void success(MultiNewsArticleBean multiNewsArticleBean) {
                PreUtils.putLong("hotsoon_video", lastTime);
                mView.onGetHSVideoList(multiNewsArticleBean.video, loadType);
            }

            @Override
            public void failure() {
                mView.onError(null);
            }
        });
    }


}
