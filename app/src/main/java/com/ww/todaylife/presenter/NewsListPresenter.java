package com.ww.todaylife.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.ww.commonlibrary.http.DealCallBack;
import com.ww.commonlibrary.http.ServiceGenerator;
import com.ww.commonlibrary.util.StringUtils;
import com.ww.commonlibrary.base.BaseObserver;
import com.ww.todaylife.bean.BaseResponse;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.bean.httpResponse.VideoContentBean;
import com.ww.todaylife.dataBase.TlDatabase;
import com.ww.todaylife.util.DataProcessUtils;
import com.ww.commonlibrary.util.PreUtils;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.bean.httpResponse.NewsListResponse;
import com.ww.todaylife.presenter.Iview.INewsListView;
import com.ww.todaylife.api.NewsApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class NewsListPresenter extends BasePresenter<INewsListView, BaseResponse> {
    private long lastTime;

    public NewsListPresenter(INewsListView mView) {
        super(mView);
    }

    public void getNewsList(String typeCode, int loadType) {
        lastTime = PreUtils.getLong(typeCode, 0);
        if (lastTime == 0) {
            lastTime = System.currentTimeMillis() / 1000;
        }
        Observable<NewsListResponse> observable;
        if (TextUtils.equals("hotsoon_video", typeCode)) {
            observable = ServiceGenerator.createService(NewsApi.class).getHSVideoNewsList(typeCode, lastTime, System.currentTimeMillis() / 1000);
        } else {
            observable = ServiceGenerator.createService(NewsApi.class).getNewsList(typeCode, lastTime, System.currentTimeMillis() / 1000);
        }
        addDisposable(observable.doOnNext(responseData -> {
            //可做数据处理 2条数据置顶
            List<NewsListResponse.DataBean> dataBeans = responseData.data;
            List<NewsDetail> newsList = new ArrayList<>();
            if (dataBeans.size() != 0) {
                for (NewsListResponse.DataBean newsData : dataBeans) {
                    NewsDetail news = new Gson().fromJson(newsData.content, NewsDetail.class);
                    news.typeCode = typeCode;
                    newsList.add(news);
                }
                TlDatabase.getInstance().newsDao().insertNewsList(newsList);
                if (typeCode.equals("") && newsList.size() > 1) {
                    newsList.get(0).topping = 1;
                    newsList.get(1).topping = 1;
                }
            }
            DataProcessUtils.dealNewsData(newsList);
            responseData.news = newsList;
        }), new BaseObserver<NewsListResponse>() {
            @Override
            public void success(NewsListResponse multiNewsArticleBean) {
                PreUtils.putLong(typeCode, lastTime);
                mView.onGetNewsList(multiNewsArticleBean.news, loadType);
            }

            @Override
            public void failure() {
                mView.onError(null);
            }
        });
    }


    public void getNewsLoadMore(String typeCode, int loadType, long startIndex) {
        addDatabaseDisposable(Observable.create((ObservableOnSubscribe<List<NewsDetail>>) emitter -> {
            List<NewsDetail> newsDetailList = new ArrayList<>();
            newsDetailList = TlDatabase.getInstance().newsDao().searchNewsPage(typeCode, startIndex);
            Collections.reverse(newsDetailList);
            emitter.onNext(newsDetailList);
        }), new BaseObserver<List<NewsDetail>>() {
            @Override
            public void success(List<NewsDetail> newsDetails) {
                mView.onGetNewsLoadMoreList(newsDetails, loadType);
            }

            @Override
            public void failure() {
                mView.onError(null);
            }
        });
    }

    public void getNewsHistoryOrStar(String typeCode, int loadType, long startIndex) {
        addDatabaseDisposable(Observable.create((ObservableOnSubscribe<List<NewsDetail>>) emitter -> {
            List<NewsDetail> list = new ArrayList<>();
            if (TextUtils.equals(typeCode, "history")) {
                list = TlDatabase.getInstance().newsDao().searchNewsHistory(true, startIndex);
            } else {
                list = TlDatabase.getInstance().newsDao().searchNewsStar(true, startIndex);
            }
            emitter.onNext(DataProcessUtils.dealRepeatNews(list));
        }), new BaseObserver<List<NewsDetail>>() {
            @Override
            public void success(List<NewsDetail> newsDetails) {
                mView.onGetNewsLoadMoreList(newsDetails, loadType);
            }

            @Override
            public void failure() {
                mView.onError(null);
            }
        });
    }

    public void getVideoContent(String videoId, DealCallBack<VideoContentBean> callBack) {
        String url = StringUtils.getVideoContentApi(videoId);
        addDisposable(ServiceGenerator.createService(NewsApi.class).getVideoContent(url).doOnNext(new Consumer<VideoContentBean>() {
            @Override
            public void accept(VideoContentBean bean) throws Exception {
                DataProcessUtils.getVideoRealUrl(bean);
            }
        }), new BaseObserver<VideoContentBean>() {
            @Override
            public void success(VideoContentBean videoContentBean) {
                if (callBack == null) {
                    return;
                }
                if (videoContentBean.data.video_source == null) {
                    mView.onError("视频解析失败");
                    callBack.onFailure("");
                } else {
                    callBack.onSuccess(videoContentBean);
                }

            }

            @Override
            public void failure() {
                if (callBack == null) {
                    return;
                }
                callBack.onFailure("");
                mView.onError("视频解析失败");
            }
        });
    }

}
