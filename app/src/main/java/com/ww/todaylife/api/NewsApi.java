package com.ww.todaylife.api;

import com.ww.todaylife.bean.httpResponse.CommentResponse;
import com.ww.todaylife.bean.httpResponse.HotKeywordResponse;
import com.ww.todaylife.bean.httpResponse.NewsListResponse;
import com.ww.todaylife.bean.httpResponse.NewsContentBean;
import com.ww.todaylife.bean.httpResponse.RecommendKeyword;
import com.ww.todaylife.bean.httpResponse.ReplyListResponse;
import com.ww.todaylife.bean.httpResponse.SearchResponse;
import com.ww.todaylife.bean.httpResponse.VideoContentBean;
import com.ww.todaylife.util.UrlConstant;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface NewsApi {
    @GET(UrlConstant.GET_NEWS_LIST)
    Observable<NewsListResponse> getNewsList(@Query("category") String category,
                                             @Query("min_behot_time") long lastTime, @Query("last_refresh_sub_entrance_interval") long currentTime);


    @GET(UrlConstant.GET_NEWS_COMMENT_LIST)
    Observable<CommentResponse> getNewsCommentList(@Query("group_id") String groupId, @Query("item_id") String itemId, @Query("offset") String offset, @Query("count") String count);

    @GET
    Observable<NewsContentBean> getNewsContentHtml(@Url String url);



    @GET(UrlConstant.GET_SEARCH_KEYWORD)
    Observable<RecommendKeyword> getSearchKeyword(@Query("keyword") String keyword);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Accept: text/javascript, text/html, application/xml, text/xml, */*"
    })
    @GET(UrlConstant.GET_SEARCH_CONTENT)
    Observable<SearchResponse> getSearchContent(@Query("keyword") String keyword, @Query("offset") String offset, @Query("from") String from,@Query("pd") String pd);

    @GET(UrlConstant.GET_HOT_SEARCH_KEYWORD)
    Observable<HotKeywordResponse> getHotSearch();


    @GET
    Observable<VideoContentBean> getVideoContent(@Url String url);


    @GET(UrlConstant.GET_HSVIDEO_LIST)
    Observable<NewsListResponse> getHSVideoNewsList(@Query("category") String category,
                                                    @Query("min_behot_time") long lastTime,
                                                    @Query("last_refresh_sub_entrance_interval") long currentTime);
    @GET(UrlConstant.GET_REPLY_LIST)
    Observable<ReplyListResponse> getCommentReplyList(@Query("id") String id,  @Query("offset") String offset);
}
