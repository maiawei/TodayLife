package com.ww.todaylife.presenter.Iview;

import com.ww.todaylife.bean.httpResponse.CommentResponse;
import com.ww.todaylife.bean.httpResponse.NewsContentBean;
import com.ww.todaylife.bean.httpResponse.VideoContentBean;

public interface IDetailBaseView {
    void onError(String msg);
    void onGetVideoContent(VideoContentBean videoContent);
    void onGetNewsComment(CommentResponse commentResponse,int loadType,boolean hasMore);
}
