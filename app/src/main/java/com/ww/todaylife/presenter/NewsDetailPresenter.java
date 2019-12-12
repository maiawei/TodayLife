package com.ww.todaylife.presenter;

import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.http.ServiceGenerator;
import com.ww.commonlibrary.util.StringUtils;
import com.ww.commonlibrary.base.BaseObserver;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.bean.BaseResponse;
import com.ww.todaylife.bean.httpResponse.CommentResponse;
import com.ww.todaylife.bean.httpResponse.NewsContentBean;
import com.ww.todaylife.bean.httpResponse.ReplyListResponse;
import com.ww.todaylife.bean.httpResponse.VideoContentBean;
import com.ww.todaylife.presenter.Iview.IDetailBaseView;
import com.ww.todaylife.presenter.Iview.INewsDetailView;
import com.ww.todaylife.util.DataProcessUtils;
import com.ww.todaylife.api.NewsApi;

public class NewsDetailPresenter extends BasePresenter<IDetailBaseView, BaseResponse> {
    private long lastTime;

    public NewsDetailPresenter(IDetailBaseView mView) {
        super(mView);
    }

    public void getNewsCommentList(String groupId, String itemId, int pageNow, int loadType) {
        int offset = (pageNow - 1) * CommonConstant.COMMENT_PAGE_SIZE;
        addDisposable(ServiceGenerator.createService(NewsApi.class).getNewsCommentList(groupId, itemId, offset + "", String.valueOf(CommonConstant.COMMENT_PAGE_SIZE)), new BaseObserver<CommentResponse>() {
            @Override
            public void success(CommentResponse commentResponse) {
                mView.onGetNewsComment(commentResponse, loadType, commentResponse.has_more);
            }

            @Override
            public void failure() {
                mView.onGetNewsComment(null, loadType, true);
            }
        });
    }

    public void getNewsDetail(String url) {
        String api = "http://m.toutiao.com/i" + url + "/info/";
        addDisposable(ServiceGenerator.createService(NewsApi.class).getNewsContentHtml(api), new BaseObserver<NewsContentBean>() {
            @Override
            public void success(NewsContentBean contentBean) {
                String html = getNewsHTML(contentBean);
                ((INewsDetailView) mView).onGetNews(html, contentBean);
            }

            @Override
            public void failure() {
                mView.onError(null);
            }
        });
    }

    private String getNewsHTML(NewsContentBean bean) {
        String html = "";
        String title = bean.data.title;
        String content = bean.data.content;
        String avatar = "";
        String userId = "";
        String name = "";
        if (bean.data.media_user != null) {
            avatar = bean.data.media_user.avatar_url;
            name = bean.data.media_user.screen_name;

        }
        userId = String.valueOf(bean.data.creator_uid);

        if (content != null) {
            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/toutiao_light.css\" type=\"text/css\">";
            html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">" +
                    css +
                    "<body>\n" +
                    "<article class=\"article-container\">\n" +
                    "    <div class=\"article__content article-content\">" +
                    "<h1 class=\"article-title\">" +
                    title +
                    "</h1>" +
                    "<div id=\"" + userId + "\" class=\"avatar-view\">" +
                    "<img  class=\"header\" src=\"" + avatar + "\"/>" +
                    "<span class=\"name\">" +
                    name +
                    "</span>" +
                    "<button class=\"name\">" + "关注" +
                    "</button>" +
                    "</div>" +
                    content +
                    "    </div>\n" +
                    "</article>\n" +

                    "</body>\n" +
                    "</html>";

            return html;
        } else {
            return null;
        }
    }

    public void getVideoContent(String videoId) {
        String url = StringUtils.getVideoContentApi(videoId);
        addDisposable(ServiceGenerator.createService(NewsApi.class).getVideoContent(url).doOnNext(DataProcessUtils::getVideoRealUrl), new BaseObserver<VideoContentBean>() {
            @Override
            public void success(VideoContentBean videoContentBean) {
                if (videoContentBean.data.video_source == null) {
                    if (!videoContentBean.message.equals("success"))
                        mView.onError(videoContentBean.message);
                } else {
                    mView.onGetVideoContent(videoContentBean);
                }
            }

            @Override
            public void failure() {
                mView.onError("视频解析失败");
            }
        });
    }

    public void getCommentReplyList(String id, int size, int loadType) {
        addDisposable(ServiceGenerator.createService(NewsApi.class).getCommentReplyList(id, String.valueOf(size)), new BaseObserver<ReplyListResponse>() {
            @Override
            public void success(ReplyListResponse response) {
                ((INewsDetailView) mView).onGetCommentReply(response,loadType, response.data.has_more);
            }

            @Override
            public void failure() {
                ((INewsDetailView) mView).onGetCommentReply(null, loadType, true);
            }
        });
    }
}
