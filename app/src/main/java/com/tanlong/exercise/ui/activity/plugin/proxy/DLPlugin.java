package com.tanlong.exercise.ui.activity.plugin.proxy;

import android.os.Bundle;

/**
 * 插件Activity 实现该接口
 */
public interface DLPlugin {

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
