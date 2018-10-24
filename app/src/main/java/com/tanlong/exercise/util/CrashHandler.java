package com.tanlong.exercise.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private final String TAG = getClass().getSimpleName();

    private static CrashHandler instance;

    private static final String FILE_NAME = "crash_";
    private static final String FILE_NAME_SUFFIX = ".trace";

    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        //缓存之前的未捕获异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置线程默认使用的未捕获异常处理器,因为是静态变量,所以可以捕获所有线程的异常
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        //发送未捕获异常会调用该方法
        //导出异常信息
        dumpExceptionToSdCard(ex);

        ex.printStackTrace();
        //使用系统默认的异常处理器 处理异常
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(t, ex);
        } else {
            //没有系统默认的异常处理器,则杀死APP
            Process.killProcess(Process.myPid());
        }
    }

    /**
     * 导出异常信息到SD卡上
     * @param ex
     */
    private void dumpExceptionToSdCard(Throwable ex) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "SD卡不可用,无法记录Exception");
            return;
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "没有读写文件权限,无法记录Exception");
            return;
        }

        String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator
                + "Crash Log" + File.separator + mContext.getPackageName() + File.separator;
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        SimpleDateFormat.getDateTimeInstance();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(System.currentTimeMillis()));
        File file = new File(filePath + FILE_NAME + time + FILE_NAME_SUFFIX);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            printWriter.println(time);
            dumpPhoneInfo(printWriter);
            printWriter.println();
            ex.printStackTrace(printWriter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    /**
     * 将手机信息写入PrintWriter
     * @param pw
     * @throws PackageManager.NameNotFoundException
     */
    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        //1.记录版本信息
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        StringBuffer buffer = new StringBuffer();
        buffer.append("App Version: ")
                .append(packageInfo.versionName)
                .append("_")
                .append(packageInfo.versionCode);
        pw.println(buffer.toString());

        //2.记录Android版本号
        buffer = new StringBuffer();
        buffer.append("OS Version: ")
                .append(Build.VERSION.RELEASE)
                .append("_")
                .append(Build.VERSION.SDK_INT);
        pw.println(buffer.toString());

        //3. 记录手机信息
        buffer = new StringBuffer();
        buffer.append("Verdor: ")
                .append(Build.MANUFACTURER)
                .append("\n")
                .append("Model: ")
                .append(Build.MODEL)
                .append("\n")
                .append("CPU ABI: ")
                .append(getCPUInfo());
        pw.println(buffer.toString());
    }

    private String getCPUInfo() {
        StringBuffer buffer = new StringBuffer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (String info : Build.SUPPORTED_ABIS) {
                buffer.append(info).append("|");
            }
        } else {
            buffer.append(Build.CPU_ABI);
        }
        return buffer.toString();
    }
}
