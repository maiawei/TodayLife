package com.ww.todaylife.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ww.commonlibrary.MyApplication;


public class PreUtils {

    public static final String CONFIG_FILE_NAME = "config";


    public static void putBoolean(String key, boolean value){
        SharedPreferences sp = MyApplication.getApp().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue){
        SharedPreferences sp = MyApplication.getApp().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    public static void putString(String key, String value){
        SharedPreferences sp = MyApplication.getApp().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    public static String getString(String key, String defValue){
        SharedPreferences sp = MyApplication.getApp().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }

    public static void putInt(String key, int value){
        SharedPreferences sp = MyApplication.getApp().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    public static int getInt(String key, int defValue){
        SharedPreferences sp = MyApplication.getApp().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);
    }

    public static void putLong(String key, long value){
        SharedPreferences sp = MyApplication.getApp().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putLong(key,value).commit();
    }

    public static long getLong(String key, long defValue){
        SharedPreferences sp = MyApplication.getApp().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key,defValue);
    }

}
