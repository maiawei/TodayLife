package com.ww.commonlibrary.base;

import android.content.Context;

import com.ww.commonlibrary.MyApplication;
import com.ww.commonlibrary.util.ErrorUtils;

import io.reactivex.observers.DisposableObserver;

public abstract class BaseObserver<T> extends DisposableObserver<T> {
    private Context context;

    public BaseObserver(Context content, boolean showProgress) {
        this.context = content;
    }

    public BaseObserver() {
    }

    @Override
    public void onNext(T t) {
        success(t);
    }

    @Override
    public void onError(Throwable e) {
        ErrorUtils.handleException(e, MyApplication.getApp());
        failure();
    }

    @Override
    public void onComplete() {
//        if(!isDisposed()){
//            this.dispose();
//        }

    }

    @Override
    protected void onStart() {
        //发生在subscribeOn指定的线程
    }

    public abstract void success(T t);

    public abstract void failure();
}
