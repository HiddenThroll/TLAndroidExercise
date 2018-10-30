package com.tanlong.exercise.app;

import android.app.Application;

import com.anupcowkur.reservoir.Reservoir;
import com.baidu.mapapi.SDKInitializer;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tanlong.exercise.util.CrashHandler;

import java.io.IOException;

/**
 *
 * Created by Administrator on 2016/8/31.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initBaiduMap();
        initLog();
        initReservoir();
        initCrashHandler();
        initLeakCanary();
    }

    private void initLog() {
        Logger.init();
        Logger.e("Logger init");
    }

    private void initBaiduMap() {
        SDKInitializer.initialize(getApplicationContext());
    }

    private void initReservoir() {
        try {
            //设置缓存大小为 2M
            Reservoir.init(this, 1024 * 1024 * 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCrashHandler() {
        CrashHandler.getInstance().init(this);
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        Logger.e("初始化 initLeakCanary");
        LeakCanary.install(this);
    }
}
