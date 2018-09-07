package com.wxlz.woasislog.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.wxlz.woasislog.entity.LogInfo;
import com.wxlz.woasislog.provider.LogProvider;

import java.util.ArrayList;
import java.util.List;

public class WoasisLogService {

    private WoasisLogService() {

    }

    private static WoasisLogService instance;


    public static WoasisLogService getInstance() {
        if (instance == null) {
            synchronized (WoasisLogService.class) {
                if (instance == null) {
                    instance = new WoasisLogService();
                }
            }
        }
        return instance;
    }

    /**
     * 添加日志
     * @param context
     * @param logInfo
     */
    public void addLog(Context context, LogInfo logInfo) {
        if (logInfo == null || context == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", logInfo.getTime());
        contentValues.put("userid", logInfo.getUserid());
        contentValues.put("name", logInfo.getName());
        contentValues.put("value", logInfo.getValue());
        contentValues.put("type", logInfo.getType());
        contentValues.put("level", logInfo.getLevel());

        context.getContentResolver().insert(LogProvider.LOG_CONTENT_URI, contentValues);
    }

    /**
     * 查询所有消息,请勿在主线程中调用该方法
     * @param context
     * @return
     */
    public List<LogInfo> queryAllLog(Context context) {
        List<LogInfo> logInfoList = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(LogProvider.LOG_CONTENT_URI, null,
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
}
