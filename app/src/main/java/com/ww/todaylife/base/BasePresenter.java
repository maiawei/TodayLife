package com.ww.todaylife.base;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class BasePresenter<V,T> {
    private CompositeDisposable mCompositeDisposable;
    public V mView;

    public BasePresenter(V mView) {
        this.mView = mView;
    }

    public void removeView() {
        mView = null;
        clearDisposable();
    }

    // 网络请求
    public void addDisposable(Observable<? extends T> observable, DisposableObserver mDisposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(mDisposable);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mDisposable);
    }
    // 读取本地数据库
    public void addDatabaseDisposable(Observable observable, DisposableObserver mDisposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(mDisposable);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mDisposable);
    }

    /**
     * 取消所有订阅
     */
    private void clearDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
    }

}
