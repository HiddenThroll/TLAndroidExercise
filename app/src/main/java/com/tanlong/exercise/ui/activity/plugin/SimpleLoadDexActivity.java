package com.tanlong.exercise.ui.activity.plugin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivitySimpleLoadDexBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * @author 龙
 */
public class SimpleLoadDexActivity extends BaseActivity {
    ActivitySimpleLoadDexBinding binding;

    String loadContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_simple_load_dex);
        binding.setActivity(this);

        loadContent = "我是原始内容";
    }

    public void showContent() {
        binding.tvContent.setText(loadContent);
    }

    public void loadDex() {
        //找到要加载的dex文件
        File dexFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "wxlz" + File.separator + "LoadDex.dex");
        // 无法直接从外部路径加载.dex文件，需要指定APP内部路径作为缓存目录（.dex文件会被解压到此目录）
        File dexOutputDir = this.getDir("dex", 0);
        Logger.e("dexOutputDir is " + dexOutputDir.getAbsolutePath());
        DexClassLoader dexClassLoader = new DexClassLoader(dexFile.getAbsolutePath(),
                dexOutputDir.getAbsolutePath(), null, getClassLoader());

        //通过反射方式获取加载类
        Class loadDexClass = null;
        try {
            loadDexClass = dexClassLoader.loadClass("com.tanlong.exercise.ui.activity.plugin.content.LoadDex");
            Method getContent = loadDexClass.getMethod("getContent");
            getContent.setAccessible(true);
            loadContent = (String) getContent.invoke(loadDexClass.newInstance(), null);
            Logger.e("加载Dex成功, content is " + loadContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
