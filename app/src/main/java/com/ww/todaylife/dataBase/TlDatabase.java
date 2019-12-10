package com.ww.todaylife.dataBase;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ww.commonlibrary.MyApplication;
import com.ww.todaylife.bean.httpResponse.NewsDetail;
import com.ww.todaylife.bean.httpResponse.StarNews;
import com.ww.todaylife.dao.NewsDao;
import com.ww.todaylife.dao.StarNewsDao;

@Database(entities = {NewsDetail.class, StarNews.class}, version = 1, exportSchema = false)
public abstract class TlDatabase extends RoomDatabase {

    private static TlDatabase instance;


    public abstract NewsDao newsDao();

    public abstract StarNewsDao starNewsDao();

    static final Migration MIGRATION_8_9 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tb_product "
                    + " ADD COLUMN bankName3 TEXT");
        }
    };


    //升级数据库考虑数据迁移情况 fallbackToDestructiveMigration会清空数据库或migration
    public static TlDatabase getInstance() {
        if (instance == null) {
            synchronized (TlDatabase.class) {
                if (instance == null) {
                    // instance = Room.databaseBuilder(MyApplication.getApp(), TlDatabase.class, "room.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                    instance = Room.databaseBuilder(MyApplication.getApp(), TlDatabase.class, "room.db").allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }

}