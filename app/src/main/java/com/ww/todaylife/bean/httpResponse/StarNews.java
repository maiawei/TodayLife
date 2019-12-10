package com.ww.todaylife.bean.httpResponse;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;
import androidx.room.TypeConverters;

import com.ww.todaylife.dataBase.FilterWordsConverters;
import com.ww.todaylife.dataBase.ImageListBeanConverters;

/**
 * created by wang.wei on 2019-12-09
 */
@Entity(tableName = "tb_star_news")
@TypeConverters({ImageListBeanConverters.class, FilterWordsConverters.class})
public class StarNews  {
   @PrimaryKey(autoGenerate = true)
   public long starId;

   @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
   @Embedded()
   public NewsDetail newsDetail;
}
