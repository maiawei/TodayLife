
package com.ww.commonlibrary.http;


public interface DealCallBack<T> {


    void onSuccess(T t);

    void onFailure(String msg);

}
