package com.ww.todaylife.api;

import com.ww.todaylife.bean.httpResponse.mediaUser.MediaUserBean;
import com.ww.todaylife.util.UrlConstant;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * created by wang.wei on 2019-11-28
 */
public interface MediaUserApi {

    @GET(UrlConstant.GET_MEDIA_USER_INFO)
    Observable<MediaUserBean> getMediaUser(@Query("user_id") String userId);
}
