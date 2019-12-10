package com.ww.todaylife.dataBase;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ww.todaylife.bean.httpResponse.FilterWords;
import com.ww.todaylife.bean.httpResponse.ImageListBean;

import java.lang.reflect.Type;
import java.util.List;

public class FilterWordsConverters {
    @TypeConverter
    public static List<FilterWords> stringToList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<FilterWords>>() {}.getType();
        List<FilterWords> list = gson.fromJson(json, type);
        return list;
    }

    @TypeConverter
    public static String listToString(List<FilterWords> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<FilterWords>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}