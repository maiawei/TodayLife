package com.ww.commonlibrary.util;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.ww.commonlibrary.base.BaseResponse;
import com.ww.commonlibrary.http.APIError;
import com.ww.commonlibrary.http.ServiceGenerator;

import java.lang.annotation.Annotation;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLHandshakeException;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by wang.wei on 2016/10/27.
 */
public class ErrorUtils {
    public static APIError parseError(Response<?> response) {
        Converter<ResponseBody, APIError> converter =
                ServiceGenerator.retrofit()
                        .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (Exception e) {
            return new APIError();
        }

        return error;
    }

    public static void handleException(Throwable e, Context context) {
        try {
            if (e instanceof UnknownHostException || e instanceof ConnectException) {
                UiUtils.showShortToast(context, "网络异常,请重试");
            } else if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
                UiUtils.showShortToast(context, "连接超时,请稍候重试");
            } else if (e instanceof SSLHandshakeException) {
                UiUtils.showShortToast(context, "安全证书异常");
            } else if (e instanceof HttpException) {
                HttpException httpException = (HttpException) e;
                int code = httpException.code();
                if (code == 500) {
                    UiUtils.showShortToast(context, "服务器维护中");
                } else if (code == 404) {
                    UiUtils.showShortToast(context, "请求的地址不存在");
                } else if (code == 401) {
                    UiUtils.showShortToast(context, "身份验证失败，请重新登录");
                } else {
                    String str = httpException.response().errorBody().string();
                    BaseResponse errorBody = new Gson().fromJson(new Gson().toJson(str), BaseResponse.class);
                    UiUtils.showShortToast(context, errorBody.message);
                }
            } else {
                UiUtils.showShortToast(context, e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            Log.e("OnSuccessAndFaultSub", "error:" + e.getMessage());
        }
    }

    public static String getErrorMessage(Throwable t) {
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            return "网络异常";
        }
        if (t instanceof SocketTimeoutException) {
            return "连接超时";
        }
        if (t instanceof TimeoutException) {
            return "连接超时";
        }
        if (t != null && t.getMessage() != null) {
            return t.getMessage();
        }
        return "未知错误";
    }


}
