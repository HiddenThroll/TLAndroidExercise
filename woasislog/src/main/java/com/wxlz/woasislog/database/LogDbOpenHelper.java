package com.wxlz.woasislog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 日志模块使用的DbHelper
 */
public class LogDbOpenHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "log_provider.db";

    public static final String LOG_TABLE_NAME = "log";

    private static final int DB_VERSION = 1;

    private static final String CREATE_LOG_TABLE = "CREATE TABLE IF NOT EXISTS " + LOG_TABLE_NAME
            + "(id INTEGER PRIMARY KEY AUTOINCREMENT," + "time INTEGER," + "userid INTEGER," +
            "name TEXT," + "value TEXT," + "type INTEGER," + "level INTEGER)";

    public LogDbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
