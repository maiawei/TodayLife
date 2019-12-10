package com.ww.todaylife.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ww.todaylife.bean.httpResponse.NewsDetail;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface NewsDao {
    @Insert
    public void insertNews(NewsDetail... newsDetails);

    @Insert
    public void insertNewsList(List<NewsDetail> NewsAList);

    @Update
    public void updateNews(NewsDetail... newsDetails);

    @Query("UPDATE tb_news SET isRead= :value WHERE item_id = :item_id")
    public void updateIsReadByItemId(boolean value, long item_id);

    @Query("UPDATE tb_news SET htmlString= :str WHERE item_id = :item_id")
    public void updateHtmlStrByItemId(String str, long item_id);

    @Query("UPDATE tb_news SET isStar= :value WHERE item_id = :item_id")
    public void updateIsStarByItemId(boolean value, long item_id);

    @Delete
    public void deleteProduct(NewsDetail... newsDetails);

    @Query("select * from tb_news")
    public List<NewsDetail> searchAllNews();

    @Query("SELECT * FROM tb_news WHERE item_id = (:item_id)")
    public NewsDetail searchNewsById(long item_id);


    @Query("select * from tb_news where typeCode = (:type)")
    public List<NewsDetail> searchNews(String type);

    @Query("select * from tb_news where typeCode = (:type) order by tId desc limit :startIndex,20")
    public List<NewsDetail> searchNewsPage(String type, long startIndex);

    @Query("select * from tb_news where isRead = (:b) order by tId desc limit :startIndex,20")
    public List<NewsDetail> searchNewsHistory(boolean b, long startIndex);

    @Query("select * from tb_news where isStar = (:b) order by tId desc limit :startIndex,20")
    public List<NewsDetail> searchNewsStar(boolean b, long startIndex);
}
