package com.ww.todaylife.presenter;

import android.text.TextUtils;

import com.ww.commonlibrary.http.ServiceGenerator;
import com.ww.todaylife.api.MediaUserApi;
import com.ww.commonlibrary.base.BaseObserver;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.bean.BaseResponse;
import com.ww.todaylife.bean.httpResponse.mediaUser.MediaUserBean;
import com.ww.todaylife.presenter.Iview.IUserDetailView;

/**
 * created by wang.wei on 2019-11-28
 */
public class UserPresenter extends BasePresenter<IUserDetailView, BaseResponse> {
    public UserPresenter(IUserDetailView mView) {
        super(mView);
    }

    public void getUserInfo(String userId){
        addDisposable(ServiceGenerator.createService(MediaUserApi.class).getMediaUser(userId), new BaseObserver<MediaUserBean>() {
            @Override
            public void success(MediaUserBean userBean) {
                if(TextUtils.equals(userBean.message,"success")){
                    mView.onGetNews(userBean);
                }else {
                    mView.onError(userBean.message);
                }
            }

            @Override
            public void failure() {
                mView.onError(null);
            }
        });
    }

}
