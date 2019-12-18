package com.ww.commonlibrary;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.ww.commonlibrary.view.widget.TLRefreshHeader;


/**
 * Created by wang.wei on 2019/10/27.
 */
public class MyApplication extends MultiDexApplication {
    private static MyApplication appContext;
    public boolean isRun = false;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new TLRefreshHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });

    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRun = true;
        appContext = this;
        init();
    }

    public static MyApplication getApp() {
        return appContext;
    }

    public void init() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        if (BuildConfig.LOG_DEBUG) {
            LeakCanary.install(this);
        }
        CrashReport.initCrashReport(this, "3c7e1a7844", BuildConfig.DEBUG);
    }

}
