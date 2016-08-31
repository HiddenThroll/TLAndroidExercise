package com.tanlong.exercise.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 *
 * Created by Administrator on 2016/8/31.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initBaiduMap();
    }

    private void initBaiduMap() {
        SDKInitializer.initialize(getApplicationContext());
    }
}
