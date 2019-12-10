package com.ww.commonlibrary.util;

import android.text.TextUtils;
import android.util.Log;


/**
 * Log工具，类似android.util.Log。
 */
public class LogUtils {

    public static String customTagPrefix = "";

    private LogUtils() {
    }

    public static boolean allowLog = true;

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }



    public static void d(String content) {
        if (!allowLog) return;
        if (content==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, content);
    }

    public static void d(String content, Throwable tr) {
        if (!allowLog) return;
        if (content==null||tr==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, content, tr);
    }

    public static void e(String content) {
        if (!allowLog) return;
        if (content==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.e(tag, content);
    }

    public static void e(String content, Throwable tr) {
        if (!allowLog) return;
        if (content==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.e(tag, content, tr);
    }

    public static void i(String content) {
        if (!allowLog) return;
        if (content==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.i(tag, content);
    }

    public static void i(String content, Throwable tr) {
        if (!allowLog) return;
        if (content==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.i(tag, content, tr);
    }

    public static void v(String content) {
        if (!allowLog) return;
        if (content==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.v(tag, content);
    }

    public static void v(String content, Throwable tr) {
        if (!allowLog) return;
        if (content==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.v(tag, content, tr);
    }

    public static void w(String content) {
        if (!allowLog) return;
        if (content==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.w(tag, content);
    }

    public static void w(String content, Throwable tr) {
        if (!allowLog) return;
        if (content==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.w(tag, content, tr);
    }

    public static void w(Throwable tr) {
        if (!allowLog) return;
        if (tr==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.w(tag, tr);
    }


    public static void wtf(String content) {
        if (!allowLog) return;
        if (content==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.wtf(tag, content);
    }

    public static void wtf(String content, Throwable tr) {
        if (!allowLog) return;
        if (content==null||tr==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.wtf(tag, content, tr);
    }

    public static void wtf(Throwable tr) {
        if (!allowLog) return;
        if (tr==null) return;
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.wtf(tag, tr);
    }

}
