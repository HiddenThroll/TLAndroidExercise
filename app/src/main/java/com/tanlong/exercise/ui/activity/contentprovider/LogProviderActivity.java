package com.tanlong.exercise.ui.activity.contentprovider;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityLogProviderBinding;
import com.tanlong.exercise.service.FileBusiness;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.PermissionUtil;
import com.wxlz.woasislog.entity.LogInfo;
import com.wxlz.woasislog.service.WoasisLogService;

import java.io.File;
import java.util.List;

/**
 * @author 龙
 */
public class LogProviderActivity extends BaseActivity {
    ActivityLogProviderBinding binding;
    WoasisLogService woasisLogService;
    FileBusiness fileBusiness;
    PermissionUtil permissionUtil;

    public static String DIR_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + "Exercise";
    public static String LOG_FILE_NAME = DIR_PATH + File.separator + "loginfo.txt";

    private final int PERMISSION_FILE_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_provider);
        binding.setActivity(this);

        woasisLogService = new WoasisLogService(this);
        fileBusiness = new FileBusiness();
        permissionUtil = new PermissionUtil();
    }

    public void requestFilePermission() {
        permissionUtil.requestPermissions(this, PermissionUtil.FILE_PERMISSIONS, PERMISSION_FILE_CODE, iRequestPermission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtil.handlePermissionResult(this, requestCode, permissions, grantResults);
    }

    private PermissionUtil.IRequestPermission iRequestPermission = new PermissionUtil.IRequestPermission() {
        @Override
        public void onSuccess() {
            getAllLog();
        }

        @Override
        public void onFailed(String permission) {

        }
    };

    public void addLogInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long userid = 1;
                for (int i = 0; i < 100; i++) {
                    String value = userid + " addLogInfo " + i;
                    LogInfo logInfo = new LogInfo(System.currentTimeMillis(), userid, "addLogInfo", value, 1, 1);
                    woasisLogService.addLog(logInfo);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long userid = 2;
                for (int i = 0; i < 100; i++) {
                    String value = userid + " addLogInfo " + i;
                    LogInfo logInfo = new LogInfo(System.currentTimeMillis(), userid, "addLogInfo", value, 1, 1);
                    woasisLogService.addLog(logInfo);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long userid = 3;
                for (int i = 0; i < 100; i++) {
                    String value = userid + " addLogInfo " + i;
                    LogInfo logInfo = new LogInfo(System.currentTimeMillis(), userid, "addLogInfo", value, 1, 1);
                    woasisLogService.addLog(logInfo);
                }
            }
        }).start();
    }

    public void getAllLog() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<LogInfo> infoList = woasisLogService.queryAllLog();
                StringBuffer stringBuffer = new StringBuffer();
                Gson gson = new Gson();
                for (LogInfo info : infoList) {
                    String temp = gson.toJson(info);
                    stringBuffer.append(temp).append("\n\r");
                }
                fileBusiness.writeFile(DIR_PATH, LOG_FILE_NAME, stringBuffer.toString());
            }
        }).start();
    }

    public void queryLog() {
        List<LogInfo> logInfoList = woasisLogService.queryLog(null, "userid = ?", new String[]{"2"}, null);
        Log.e("test", "logInfoList size is " + logInfoList.size());
    }
}
