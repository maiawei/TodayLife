package com.ww.todaylife.dataBase;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ww.todaylife.bean.httpResponse.ImageListBean;

import java.lang.reflect.Type;
import java.util.List;

public class ImageListBeanConverters {
    @TypeConverter
    public static List<ImageListBean> stringToList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ImageListBean>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String usersToString(List<ImageListBean> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ImageListBean>>() {}.getType();
        return gson.toJson(list, type);
    }
}