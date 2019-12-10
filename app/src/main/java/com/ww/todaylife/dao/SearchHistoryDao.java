package com.ww.todaylife.dao;

import android.content.Context;


import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.util.FileUtils;

import java.util.List;


/**
 * Created by wang.wei on 2018/2/28.
 */
//保存搜索记录string无需使用database
public class SearchHistoryDao {
    private static SearchHistoryDao instance;

    private SearchHistoryDao() {

    }

    public static SearchHistoryDao getInstance() {
        if (instance == null) {
            synchronized (SearchHistoryDao.class) {
                if (instance == null) {
                    instance = new SearchHistoryDao();
                }
            }
        }
        return instance;
    }

    public void saveSearchHistory(List<String> historyList, Context context) {
        FileUtils.saveObject(CommonConstant.SEARCH_HISTORY, historyList, context);
    }

    public void deleteSearchHistory(Context context) {
        FileUtils.deleteObject(CommonConstant.SEARCH_HISTORY, context);
    }

    public List<String> getSearchHistory(Context context) {
        return (List<String>) FileUtils.getObject(CommonConstant.SEARCH_HISTORY, context);
    }
}
