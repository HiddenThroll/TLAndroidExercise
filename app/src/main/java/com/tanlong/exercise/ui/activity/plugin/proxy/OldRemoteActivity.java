package com.tanlong.exercise.ui.activity.plugin.proxy;

import android.os.Bundle;
import android.util.Log;

/**
 * 动态加载的插件Activity
 * @author 龙
 */
public class OldRemoteActivity implements DLPlugin {
    private final String TAG = "OldRemoteActivity";
    private final String ALFLASFKADFS = "OldRemoteActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "OldRemoteActivity onCreate");
    }

    @Override
    public void onStart() {
        Log.e(TAG, "OldRemoteActivity onStart");
    }

    @Override
    public void onRestart() {
        Log.e(TAG, "OldRemoteActivity onRestart");
    }

    @Override
    public void onResume() {
        Log.e(TAG, "OldRemoteActivity onResume");
    }

    @Override
    public void onPause() {
        Log.e(TAG, "OldRemoteActivity onPause");
    }

    @Override
    public void onStop() {
        Log.e(TAG, "OldRemoteActivity onStop");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "OldRemoteActivity onDestroy");
    }
}
