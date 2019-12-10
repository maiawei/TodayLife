package com.ww.commonlibrary.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;

import com.ww.commonlibrary.BuildConfig;
import com.ww.commonlibrary.R;
import com.ww.commonlibrary.view.ToastView;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by wang.wei on 2018/2/6.
 */

public class SystemUtils {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static void fullStatusBarScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    //设置Activity对应的顶部状态栏的颜色
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(activity, colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowTranslucent(Activity activity,float alpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha = alpha;
        window.setAttributes(wl);
    }

    /**
     * 获取应用Android系统版本 getOSVersion
     *
     * @param
     * @return
     * @since 1.0
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备操作系统版本号 getCurrentOsVersionCode
     *
     * @return
     * @since 1.0
     */
    public static int getCurrentOsVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备型号 getPhoneModel
     *
     * @param
     * @return
     * @since 1.0
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    //打电话
    @SuppressLint("MissingPermission")
    public static void call(Context context, String phoneNumber) {
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        context.startActivity(intent);
    }

    //复制内容到粘贴板
    public static void copyContent(Context context, String message) {
        //：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("text", message);
        cm.setPrimaryClip(mClipData);
        //  mClipData.getItemAt(0).toString() 获取粘贴板内容
        UiUtils.showShortToast(context, context.getString(R.string.copy_finish), ToastView.TYPE_SUCCESS);
    }

    //系统浏览器打开url
    public static void openUrlWithBrowser(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            LogUtils.e(e.getMessage());
        }

    }

    public static boolean isTintStatusBarAvailable(Activity activity) {
        boolean isTintStatusBarAvailable = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                return true;
            }
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // check window flags
                int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                if ((winParams.flags & bits) != 0 && win.getDecorView().getSystemUiVisibility() == uiOptions) {
                    isTintStatusBarAvailable = true;
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // check theme attrs
                int[] attrs = {android.R.attr.windowTranslucentStatus};
                TypedArray a = activity.obtainStyledAttributes(attrs);
                try {
                    isTintStatusBarAvailable = a.getBoolean(0, false);
                } finally {
                    a.recycle();
                }

                // check window flags
                int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                if ((winParams.flags & bits) != 0) {
                    isTintStatusBarAvailable = true;
                }
            }
        }
        return isTintStatusBarAvailable;

    }

    //判断app通知选项是否被关闭
    public static boolean isOpenNotify(Context context) {
        if (Build.VERSION.SDK_INT >= 25) {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        } else {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            Class appOpsClass = null;
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);
                boolean boo = ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
                return boo;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    //跳转到app应用市场界面
    public static void openAppStore(Context context, String packageName) {
        try {
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            // Toast.makeText(MainActivity.this, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    /**
     * 安装新版本应用
     */
    public static void installApp(Context context, String fileName) {
        File appFile = new File(fileName);
        if (!appFile.exists()) {
            //  UIUtil.toastShort(context,"安装文件已丢失");
            return;
        }
        // 跳转到新版本应用安装页面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.ww.todaylife" + ".fileProvider", appFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            UiUtils.showShortToast(context, "安装失败，请到市场安装最新的版本", ToastView.TYPE_ERROR);
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }, 3000);

    }

    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    public static Intent getAppDetailSettingIntent(String packageName) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", packageName, null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", packageName);
        }
        return localIntent;
    }


    /**
     * 通过包名启动app
     *
     * @param context
     * @param packageName
     */
    public static void startActivityByPackageName(Context context, String packageName) {
        Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mainIntent == null) {
            UiUtils.showShortToast(context, "无法启动应用", ToastView.TYPE_ERROR);
            return;
        }
        context.startActivity(mainIntent);
    }

    /***
     * intent形式卸载app   会有对话框提示
     * @param context
     * @param packageName
     */
    public static void unInstallApp(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent it = new Intent(Intent.ACTION_DELETE);
        it.setData(uri);
        context.startActivity(it);

    }


}
