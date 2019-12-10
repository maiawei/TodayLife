package com.ww.todaylife.presenter.Iview;

import com.ww.todaylife.bean.httpResponse.NewsContentBean;
import com.ww.todaylife.bean.httpResponse.mediaUser.MediaUserBean;

/**
 * created by wang.wei on 2019-11-28
 */
public interface IUserDetailView {
    void onGetNews(MediaUserBean userBean);
    void onError(String msg);
}
