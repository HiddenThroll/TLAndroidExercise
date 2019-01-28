package com.tanlong.exercise.ui.activity.plugin;

import android.os.Bundle;
import android.os.Environment;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.plugin.proxy.DLPlugin;

import java.io.File;
import java.lang.reflect.Constructor;

import dalvik.system.DexClassLoader;

/**
 * 插件化 代理Activity
 * @author 龙
 */
public class ProxyActivity extends BaseActivity {

    private DLPlugin mRemoteActivity;

    private void loadDex() {
        //找到要加载的dex文件
        File dexFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "wxlz" + File.separator + "target.dex");
        // 无法直接从外部路径加载.dex文件，需要指定APP内部路径作为缓存目录（.dex文件会被解压到此目录）
        File dexOutputDir = this.getDir("dex", 0);
        Logger.e("dexOutputDir is " + dexOutputDir.getAbsolutePath());
        DexClassLoader dexClassLoader = new DexClassLoader(dexFile.getAbsolutePath(),
                dexOutputDir.getAbsolutePath(), null, getClassLoader());

        try {
            Class loadDexClass = dexClassLoader.loadClass("com.tanlong.exercise.ui.activity.plugin.proxy.RemoteActivity");
            //RemoteActivity就是一个普通类,通过反射调用其构造方法 新建实例
            Constructor<?> constructor = loadDexClass.getConstructor(new Class[]{});
            mRemoteActivity = (DLPlugin) constructor.newInstance(new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
            mRemoteActivity = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDex();

        if (mRemoteActivity != null) {
            mRemoteActivity.onCreate(savedInstanceState);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRemoteActivity != null) {
            mRemoteActivity.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRemoteActivity != null) {
            mRemoteActivity.onResume();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mRemoteActivity != null) {
            mRemoteActivity.onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mRemoteActivity != null) {
            mRemoteActivity.onRestart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRemoteActivity != null) {
            mRemoteActivity.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRemoteActivity != null) {
            mRemoteActivity.onDestroy();
        }
    }
}
