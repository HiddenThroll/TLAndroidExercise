package com.tanlong.exercise.ui.activity.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.plugin.proxy.DLPlugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import dalvik.system.DexClassLoader;

/**
 * 插件化 代理Activity
 *
 * @author 龙
 */
public class ProxyActivity extends BaseActivity {
    private final String APK_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "wxlz" + File.separator + "target.apk";

    private DLPlugin mRemoteActivity;

    private Resources mResources;
    private AssetManager mAssetManager;

    private void loadDex() {
        //找到要加载的dex文件
        DexClassLoader dexClassLoader = createDexClassLoader(APK_FILE_PATH);
        loadResource();

        // 使用PackageInfo获取到类名
        PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(APK_FILE_PATH, PackageManager.GET_ACTIVITIES);
        if (packageInfo.activities == null) {
            Log.d(TAG, "onCreate: activities are null!!!");
            return;
        }
        Log.e(TAG, "onCreate: packageInfo activities " + Arrays.toString(packageInfo.activities));

        try {
            Class loadDexClass = dexClassLoader.loadClass(packageInfo.activities[0].name);
            Log.e(TAG,"getClass " + loadDexClass.toString());

            //RemoteActivity就是一个普通类,通过反射调用其构造方法 新建实例
            Constructor<?> constructor = loadDexClass.getConstructor();
            Method[] methods = loadDexClass.getMethods();
            for (Method method : methods) {
                if ("setDelegate".equals(method.getName())) {
                    Log.e(TAG,"find method " + method.getName());
                }
            }

            //新建示例
            Object remote = constructor.newInstance();

            //设置代理
            final Method setDelegate = loadDexClass.getMethod("setDelegate", Activity.class);
            setDelegate.setAccessible(true);
            setDelegate.invoke(remote, this);
            //强转对象
            mRemoteActivity = (DLPlugin)remote;
        } catch (Exception e) {
            e.printStackTrace();
            mRemoteActivity = null;
        }
    }

    private void loadResource() {
        mAssetManager = createAssetManager(APK_FILE_PATH);
        if (mAssetManager != null) {
            mResources = createResources(mAssetManager);
        }
    }

    @Override
    public AssetManager getAssets() {
        if (mAssetManager == null) {
            return super.getAssets();
        } else {
            return mAssetManager;
        }
    }

    @Override
    public Resources getResources() {
        if (mResources == null) {
            return super.getResources();
        } else {
            return mResources;
        }
    }

    /**
     * 创建DexClassLoader
     *
     * @param dexPath -- 外部dex文件/apk绝对路径
     * @return
     */
    private DexClassLoader createDexClassLoader(String dexPath) {
        File dexOutputDir = getDir("dex", Context.MODE_PRIVATE);
        String dexOutputPath = dexOutputDir.getAbsolutePath();
        DexClassLoader loader = new DexClassLoader(dexPath, dexOutputPath, null, getClassLoader());
        return loader;
    }

    /**
     * 创建AssetManager
     *
     * @param dexPath -- 外部dex文件/apk绝对路径
     * @return
     */
    private AssetManager createAssetManager(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建Resources
     *
     * @param assetManager
     * @return
     */
    private Resources createResources(AssetManager assetManager) {
        Resources superRes = getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        return resources;
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
