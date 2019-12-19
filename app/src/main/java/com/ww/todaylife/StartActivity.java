package com.ww.todaylife;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.MyApplication;
import com.ww.commonlibrary.util.SystemUtils;
import com.ww.commonlibrary.util.UiUtils;

import java.util.ArrayList;

/**
 * created by wang.wei on 2019-12-06
 */
public class StartActivity extends AppCompatActivity {
    private ArrayList<String> needApplyPermissions = new ArrayList<>();
    /**
     * 需要进行检测的权限数组  外部存储必须授权 其他选择授权
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        applyPermissions();
    }

    public void applyPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < needPermissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(this,
                        needPermissions[i])
                        != PackageManager.PERMISSION_GRANTED) {
                    needApplyPermissions.add(needPermissions[i]);
                }
            }
            if (needApplyPermissions.contains(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                String[] toBeStored = new String[needApplyPermissions.size()];
                ActivityCompat.requestPermissions(this,
                        needApplyPermissions.toArray(toBeStored), CommonConstant.APPLY_PERMISSIONS_CODE
                );
                needApplyPermissions.clear();
                return;
            } else {
                dealConfig();
            }
        } else {
            dealConfig();
        }
    }

    public void dealConfig() {
        startActivity(new Intent(StartActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.main_activity_in, 0);
        StartActivity.this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CommonConstant.APPLY_PERMISSIONS_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //外部存储同意授权
                dealConfig();
            } else {
                UiUtils.showConfirmDialog(StartActivity.this, R.string.permissions_hint, new UiUtils.DialogClickListener() {
                    @Override
                    public void onClick(DialogAction type) {
                        if (type == DialogAction.POSITIVE) {
                            startActivity(SystemUtils.getAppDetailSettingIntent(getPackageName()));
                            StartActivity.this.finish();
                        } else {
                            StartActivity.this.finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//透明主题与activity动画不兼容
                        }
                    }
                });
            }

        }
    }

    @Override
    public void onBackPressed() {
        // no back
    }
}
