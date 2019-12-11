package com.ww.todaylife.presenter.Iview;

import com.ww.todaylife.bean.httpResponse.CommentData;
import com.ww.todaylife.bean.httpResponse.CommentReply;
import com.ww.todaylife.bean.httpResponse.CommentResponse;
import com.ww.todaylife.bean.httpResponse.NewsContentBean;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.bean.httpResponse.ReplyListResponse;
import com.ww.todaylife.bean.httpResponse.VideoContentBean;

import java.util.List;

public interface INewsDetailView extends IDetailBaseView{
    void onGetNews(String html,NewsContentBean newsContentBean);
    //void onLoadCommentError();
    void onGetCommentReply(ReplyListResponse commentReply, int loadType, boolean hasMore);

}
