package com.ww.todaylife.presenter.Iview;

import com.ww.todaylife.bean.httpResponse.CommentResponse;
import com.ww.todaylife.bean.httpResponse.KeyWordDetail;
import com.ww.todaylife.bean.httpResponse.NewsContentBean;
import com.ww.todaylife.bean.httpResponse.NewsDetail;

import java.util.List;

public interface ISearchView {
    void onGetKeyword(List<KeyWordDetail> mList);
    void onError();
    void OnGetHotKeyWord(List<String> mList);
}
