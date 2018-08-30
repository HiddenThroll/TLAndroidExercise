package com.tanlong.exercise.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.tanlong.exercise.model.entity.EntityFee;


/**
 * database
 * <p>
 * entity的生成
 * 在不指定主键的情况下，默认第一个字段为主键
 * 主键的类型不能是字符类型
 */
@Database(version = 1, entities = {EntityFee.class}, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "rented_car").setJournalMode(JournalMode.TRUNCATE).allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }

    public abstract OrderDao getOrderDap();

}
