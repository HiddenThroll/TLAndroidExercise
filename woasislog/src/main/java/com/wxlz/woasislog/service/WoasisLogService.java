package com.wxlz.woasislog.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.wxlz.woasislog.entity.LogInfo;
import com.wxlz.woasislog.provider.LogProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志服务
 * 多进程模式下,单例模式失效,线程同步交由SQLite处理
 */
public class WoasisLogService {

    private Context mContext;

    public WoasisLogService(Context context) {
        mContext = context;
    }

    /**
     * 添加日志
     *
     * @param logInfo
     */
    public void addLog(LogInfo logInfo) {
        if (logInfo == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", logInfo.getTime());
        contentValues.put("userid", logInfo.getUserid());
        contentValues.put("name", logInfo.getName());
        contentValues.put("value", logInfo.getValue());
        contentValues.put("type", logInfo.getType());
        contentValues.put("level", logInfo.getLevel());

        mContext.getContentResolver().insert(LogProvider.LOG_CONTENT_URI, contentValues);
    }

    /**
     * 查询所有消息,请勿在主线程中调用该方法
     *
     * @return
     */
    public List<LogInfo> queryAllLog() {
        List<LogInfo> logInfoList = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(LogProvider.LOG_CONTENT_URI, null,
                null, null, null);
        if (cursor == null) {
            return logInfoList;
        }
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            long time = cursor.getLong(1);
            long userid = cursor.getLong(2);
            String name = cursor.getString(3);
            String value = cursor.getString(4);
            int type = cursor.getInt(5);
            int level = cursor.getInt(6);

            LogInfo logInfo = new LogInfo(id, time, userid, name, value, type, level);
            logInfoList.add(logInfo);
        }
        cursor.close();
        return logInfoList;
    }

    /**
     * 根据条件查询日志
     * @param projection -- 要返回的列元素
     * @param selection -- 查询条件
     * @param selectionArgs -- 查询条件参数
     * @param sortOrder -- 查询结果排序方式
     * @return
     */
    public List<LogInfo> queryLog(@Nullable String[] projection, @Nullable String selection,
                                  @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        List<LogInfo> logInfoList = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(LogProvider.LOG_CONTENT_URI, projection,
                selection, selectionArgs, sortOrder);
        if (cursor == null) {
            return logInfoList;
        }
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            long time = cursor.getLong(1);
            long userid = cursor.getLong(2);
            String name = cursor.getString(3);
            String value = cursor.getString(4);
            int type = cursor.getInt(5);
            int level = cursor.getInt(6);

            LogInfo logInfo = new LogInfo(id, time, userid, name, value, type, level);
            logInfoList.add(logInfo);
        }
        cursor.close();
        return logInfoList;
    }
}
