package com.tanlong.updateversion.manager;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tanlong.updateversion.activity.AndroidOInstallPermissionActivity;
import com.tanlong.updateversion.impl.IAndroidOInstallPermissionListener;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * APK下载管理器,适配Android 6.0 7.0 8.0
 *
 * @author 龙
 */
public class AppDownloadManager {
    public static final String TAG = "AppDownloadManager";
    /**
     * 持有Activity的弱引用,防止内存泄露
     */
    private WeakReference<Activity> weakReference;
    /**
     * 下载管理器
     */
    private DownloadManager mDownloadManager;
    /**
     * 下载进度监听器
     */
    private DownloadChangeObserver mDownloadChangeObserver;
    /**
     * 下载完成广播接收者
     */
    private DownloadReceiver mDownloadReceiver;

    /**
     * 下载任务ID
     */
    private long mReqId;
    /**
     * 指定下载APK名称
     */
    private String apkName;

    private String fileProviderAuth;

    private IUpdateListener mUpdateListener;

    public AppDownloadManager(Activity activity, String apkName, String fileProviderAuth) {
        weakReference = new WeakReference<>(activity);
        mDownloadManager = (DownloadManager) weakReference.get().getSystemService(Context.DOWNLOAD_SERVICE);
        mDownloadChangeObserver = new DownloadChangeObserver(new Handler());
        mDownloadReceiver = new DownloadReceiver();
        this.apkName = apkName;
        this.fileProviderAuth = fileProviderAuth;
    }

    private File getApkFile() {
        File apkFile = new File(weakReference.get().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                apkName);
        Log.e(TAG, "apkFile path is " + apkFile.getAbsolutePath());
        return apkFile;
    }

    public void downloadApk(String apkUrl, String title, String desc) {
        File apkFile = getApkFile();
        //在下载之前应该删除已有文件
        if (apkFile.exists()) {
            Log.e(TAG, "删除已有文件" + apkFile.delete());
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setTitle(title);
        request.setDescription(desc);
        //设置在下载期间和下载完成后,下载通知均可见
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        //设置下载文件的存放位置
        request.setDestinationInExternalFilesDir(weakReference.get(), Environment.DIRECTORY_DOWNLOADS,
                apkName);
        request.setMimeType("application/vnd.android.package-archive");
        //开始下载
        Log.e(TAG, "开始下载");
        mReqId = mDownloadManager.enqueue(request);
    }

    /**
     * 取消下载
     */
    public void cancel() {
        if (mDownloadManager != null) {
            mDownloadManager.remove(mReqId);
        }
    }

    /**
     * 对应  Activity#onResume
     */
    public void onResume() {
        //设置监听Uri.parse("content://downloads/my_downloads")
        weakReference.get().getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"),
                true, mDownloadChangeObserver);
        // 注册广播，监听APK是否下载完成
        weakReference.get().registerReceiver(mDownloadReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 对应 Activity#onPause()
     */
    public void onPause() {
        weakReference.get().getContentResolver().unregisterContentObserver(mDownloadChangeObserver);
        weakReference.get().unregisterReceiver(mDownloadReceiver);
    }


    /**
     * 下载进度监听器
     */
    private class DownloadChangeObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            updateView();
        }
    }

    private void updateView() {
        int[] bytesAndStatus = new int[]{0, 0, 0};
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(mReqId);
        Cursor c = null;
        try {
            c = mDownloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                //已经下载的字节数
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //状态所在的列索引
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        if (mUpdateListener != null) {
            mUpdateListener.update(bytesAndStatus[0], bytesAndStatus[1]);
        }
    }

    /**
     * 下载完成广播接收者
     */
    private class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            //是否有安装权限, 适配Android 8.0
            boolean haveInstallPermission;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //适配Android 8.0
                //先判断是否有安装未知来源应用的权限
                haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                if (!haveInstallPermission) {
                    //请求权限
                    IAndroidOInstallPermissionListener iAndroidOInstallPermissionListener = new IAndroidOInstallPermissionListener() {
                        @Override
                        public void permissionSuccess() {
                            installApk(context, intent);
                        }

                        @Override
                        public void permissionFailed() {
                            Toast.makeText(context, "授权失败,无法安装", Toast.LENGTH_SHORT).show();
                        }
                    };
                    AndroidOInstallPermissionActivity.setListener(iAndroidOInstallPermissionListener);
                    startAndroidOInstallPermissionActivity(context);
                } else {
                    installApk(context, intent);
                }
            } else {
                //小于 8.0 直接安装
                installApk(context, intent);
            }

        }
    }

    private void startAndroidOInstallPermissionActivity(Context context) {
        context.startActivity(new Intent(context, AndroidOInstallPermissionActivity.class));
    }

    private void installApk(Context context, Intent intent) {
        long completeDownLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        Uri uri;
        Log.e(TAG, "安装APK");
        Intent intentInstall = new Intent();
        intentInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentInstall.setAction(Intent.ACTION_VIEW);

        if (completeDownLoadId == mReqId) {
            //下载的是需要的APK
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                //6.0以下机型
                uri = mDownloadManager.getUriForDownloadedFile(completeDownLoadId);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                // 6.0 - 7.0以下
                File apkFile = queryDownloadedApk(context, completeDownLoadId);
                uri = Uri.fromFile(apkFile);
            } else {
                //7.0及以上
                uri = FileProvider.getUriForFile(context, fileProviderAuth, getApkFile());
                intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intentInstall.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intentInstall);
        }
    }

    private File queryDownloadedApk(Context context, long downloadId) {
        File targetFile = null;
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
        Cursor cursor = mDownloadManager.query(query);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                if (!TextUtils.isEmpty(uriString)) {
                    targetFile = new File(Uri.parse(uriString).getPath());
                }
            }
            cursor.close();
        }
        return targetFile;
    }

    public interface IUpdateListener {
        void update(int current, int total);
    }

    public void setUpdateListener(IUpdateListener mUpdateListener) {
        this.mUpdateListener = mUpdateListener;
    }
}
