package com.ww.todaylife.presenter.Iview;

import com.ww.todaylife.bean.httpResponse.HsVideoRootBean;
import com.ww.todaylife.bean.httpResponse.NewsDetail;

import java.util.List;

public interface IHSVideoListView {
    void onGetHSVideoList(List<HsVideoRootBean> mList, int loadType);
    void onError(String msg);
}
