package com.ww.todaylife.presenter.Iview;

import com.ww.todaylife.bean.httpResponse.VideoContentBean;

public interface IVideoListView extends INewsListView {
    void onGetVideoContent(VideoContentBean videoContentBean, int position);
    void OnErrorMsg(String str);
}
