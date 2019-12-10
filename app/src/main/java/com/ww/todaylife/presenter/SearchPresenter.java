package com.ww.todaylife.presenter;

import com.ww.commonlibrary.http.ServiceGenerator;
import com.ww.commonlibrary.base.BaseObserver;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.bean.BaseResponse;
import com.ww.todaylife.bean.httpResponse.HotKeywordResponse;
import com.ww.todaylife.bean.httpResponse.RecommendKeyword;
import com.ww.todaylife.presenter.Iview.ISearchView;
import com.ww.todaylife.api.NewsApi;

public class SearchPresenter extends BasePresenter<ISearchView, BaseResponse> {
    public SearchPresenter(ISearchView mView) {
        super(mView);
    }
    public void getKeyword(String keyword){

        addDisposable(ServiceGenerator.createService(NewsApi.class).getSearchKeyword(keyword), new BaseObserver<RecommendKeyword>() {
            @Override
            public void success(RecommendKeyword recommendKeyword) {
                mView.onGetKeyword(recommendKeyword.data);
            }

            @Override
            public void failure() {
                mView.onError();
            }
        });
    }

    public void getHotKeyword(){

        addDisposable(ServiceGenerator.createService(NewsApi.class).getHotSearch(), new BaseObserver<HotKeywordResponse>() {
            @Override
            public void success(HotKeywordResponse hotKeywordResponse) {
                mView.OnGetHotKeyWord(hotKeywordResponse.data.suggest_words);
            }

            @Override
            public void failure() {
                mView.onError();
            }
        });
    }
}
