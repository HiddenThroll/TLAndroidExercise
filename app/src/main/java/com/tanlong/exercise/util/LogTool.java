package com.tanlong.exercise.util;

import android.util.Log;

import com.tanlong.exercise.AppConfig;

/**
 * 日志工具类
 * Created by 龙 on 2015/11/12.
 */
public class LogTool {

    public static void e(String tag, String msg) {
        if (AppConfig.IS_DEBUG) {
            Log.e(tag, msg);
        }
    }
}
