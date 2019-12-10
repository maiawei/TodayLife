package com.ww.todaylife.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.bean.httpResponse.StarNews;

import java.util.List;

@Dao
public interface StarNewsDao {
    @Insert
    public void insertNews(StarNews... newsDetails);

    @Insert
    public void insertNewsList(List<StarNews> NewsAList);

    @Update
    public void updateNews(StarNews... newsDetails);

    @Delete
    public void deleteProduct(StarNews... newsDetails);

    @Query("select * from tb_star_news")
    public List<StarNews> searchAllNews();


    @Query("select * from tb_star_news order by starId desc limit :startIndex,20")
    public List<StarNews> searchNewsPage( long startIndex);

}
