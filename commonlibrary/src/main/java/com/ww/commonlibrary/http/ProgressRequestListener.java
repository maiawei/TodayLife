package com.ww.commonlibrary.http;

/**
 * 请求体进度回调接口，用于文件上传进度回调
 */
public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
