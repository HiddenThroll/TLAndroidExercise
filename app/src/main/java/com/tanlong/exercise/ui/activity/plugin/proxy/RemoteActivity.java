package com.tanlong.exercise.ui.activity.plugin.proxy;

import android.os.Bundle;
import android.util.Log;

/**
 * 动态加载的插件Activity
 * @author 龙
 */
public class RemoteActivity implements DLPlugin {
    private final String TAG = "RemoteActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "RemoteActivity onCreate");
    }

    @Override
    public void onStart() {
        Log.e(TAG, "RemoteActivity onStart");
    }

    @Override
    public void onRestart() {
        Log.e(TAG, "RemoteActivity onRestart");
    }

    @Override
    public void onResume() {
        Log.e(TAG, "RemoteActivity onResume");
    }

    @Override
    public void onPause() {
        Log.e(TAG, "RemoteActivity onPause");
    }

    @Override
    public void onStop() {
        Log.e(TAG, "RemoteActivity onStop");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "RemoteActivity onDestroy");
    }
}
