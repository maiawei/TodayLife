package com.ww.todaylife;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.base.BaseObserver;
import com.ww.commonlibrary.util.FileUtils;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.util.SystemUtils;
import com.ww.commonlibrary.util.UiUtils;
import com.ww.commonlibrary.view.CircleImageView;
import com.ww.commonlibrary.view.LimitTextView;
import com.ww.commonlibrary.view.ToastView;
import com.ww.todaylife.base.BasePresenter;
import com.ww.todaylife.base.BaseSwipeActivity;
import com.ww.todaylife.bean.eventBean.RefreshNewsList;
import com.ww.todaylife.util.PreUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ww.commonlibrary.CommonConstant.WLAN_SETTING;

/**
 * created by wang.wei on 2019-12-10
 */
public class SystemSettingActivity extends BaseSwipeActivity {
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.titleImg)
    CircleImageView titleImg;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.cacheTv)
    TextView cacheTv;
    @BindView(R.id.abstractSt)
    Switch abstractSt;
    @BindView(R.id.returnRefreshSt)
    Switch returnRefreshSt;
    @BindView(R.id.versionCodeTv)
    TextView versionCodeTv;
    @BindView(R.id.toolbarLayout)
    LinearLayout toolbarLayout;
    @BindView(R.id.wLanSettingTv)
    TextView wLanSettingTv;
    String[] wLanSettingItems;
    @BindView(R.id.limitText)
    LimitTextView limitText;
    @Override
    public int setContentId() {
        return R.layout.setting_activity_layout;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        abstractSt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PreUtils.putBoolean(CommonConstant.SHOW_ABSTRACT, isChecked);
            EventBus.getDefault().post(new RefreshNewsList(true));
        });
        returnRefreshSt.setChecked(PreUtils.getBoolean(CommonConstant.RETURN_REFRESH, false));
        returnRefreshSt.setOnCheckedChangeListener((buttonView, isChecked) -> PreUtils.putBoolean(CommonConstant.RETURN_REFRESH, isChecked));
        versionCodeTv.setText(BuildConfig.VERSION_NAME);
        wLanSettingItems = getResources().getStringArray(R.array.wlan_setting);
        wLanSettingTv.setText(wLanSettingItems[PreUtils.getInt(WLAN_SETTING, 0)]);
        getCaCheSize();
    }

    @Override
    public void initView() {
        setToolbar(toolbarLayout);
        SystemUtils.setWindowStatusBarColor(this, R.color.toolbar_bg);
        findViewById(R.id.titleImg).setVisibility(View.GONE);
        titleTv.setText("系统设置");
    }

    public void clearCache() {
        UiUtils.showAlertDialog(this, getString(R.string.clear_cache_hint), new UiUtils.DialogClickListener() {
            @Override
            public void okClick() {
                UiUtils.showProgressDialog(SystemSettingActivity.this, "删除中");
                Observable.create(emitter -> {
                    Glide.get(SystemSettingActivity.this).clearDiskCache();
                    emitter.onNext(new Object());
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<Object>() {
                            @Override
                            public void success(Object o) {
                                new Handler().postDelayed(() -> {
                                    UiUtils.hideProgressDialog();
                                    getCaCheSize();
                                }, 500);
                            }

                            @Override
                            public void failure() {
                            }
                        });
            }

            @Override
            public void cancelClick() {

            }
        });

    }

    @SuppressLint("DefaultLocale")
    public void getCaCheSize() {
        abstractSt.setChecked(PreUtils.getBoolean(CommonConstant.SHOW_ABSTRACT, false));
        long size = FileUtils.getFileSize(new File(getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR));
        cacheTv.setText(String.format("%.1fMB", (float) size / 1024f / 1024f));
    }

    public void wLanSetting() {
        UiUtils.showSelectedDialog(this, PreUtils.getInt(WLAN_SETTING, 0), getString(R.string.wlan_setting), wLanSettingItems, position -> {
            PreUtils.putInt(WLAN_SETTING, position);
            wLanSettingTv.setText(wLanSettingItems[position]);
        });
    }
    public void checkVersion(){
        UiUtils.showProgressDialog(SystemSettingActivity.this, "检查中");
        new Handler().postDelayed(() -> {
            UiUtils.hideProgressDialog();
            UiUtils.showShortToast(this,"这是最新版本了", ToastView.TYPE_SUCCESS);
        },1500);

    }


    @OnClick({R.id.clearCacheLayout, R.id.checkVersionLayout, R.id.wLanSettingLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clearCacheLayout:
                clearCache();
                break;
            case R.id.checkVersionLayout:
                checkVersion();
                break;
            case R.id.wLanSettingLayout:
                wLanSetting();
                break;
        }
    }

}
