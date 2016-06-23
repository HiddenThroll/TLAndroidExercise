package com.tanlong.exercise.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具
 * Created by 龙 on 2015/11/12.
 */
public class ToastHelp {

    public static void showShortMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShortMsg(Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
