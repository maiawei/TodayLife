package com.ww.todaylife.dao;

import android.content.Context;

import com.ww.commonlibrary.CommonConstant;
import com.ww.commonlibrary.util.FileUtils;
import com.ww.todaylife.bean.ChanelCategory;

import java.util.List;


/**
 * Created by wang.wei on 2018/2/28.
 */
public class ChanelDao {
    private static ChanelDao instance;

    private ChanelDao() {

    }

    public static ChanelDao getInstance() {
        if (instance == null) {
            synchronized (ChanelDao.class) {
                if (instance == null) {
                    instance = new ChanelDao();
                }
            }
        }
        return instance;
    }

    public void saveSelectedChanel(List<ChanelCategory> list, Context context) {
        FileUtils.saveObject(CommonConstant.SELECTED_CHANEL, list, context);
    }

    public List<ChanelCategory> getSelectedChanel(Context context) {
        return (List<ChanelCategory>) FileUtils.getObject(CommonConstant.SELECTED_CHANEL, context);
    }
}
