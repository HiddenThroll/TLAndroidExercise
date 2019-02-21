package com.tanlong.updateversion.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tanlong.updateversion.R;
import com.tanlong.updateversion.impl.IAndroidOInstallPermissionListener;

/**
 * Android 8.0 APP在线更新, 权限申请界面
 * 使用一个没有界面的Activity来请求权限,避免对原Activity的侵入修改,提高适配性
 * 业务逻辑:
 * 1.请求权限
 * 1.1 请求成功,回调success方法,结束
 * 1.2 请求失败,显示对话框提醒用户
 * 2.对话框中提示用户为何需要授权,供用户选择
 * 2.1 选择设置,关闭对话框,跳转安装权限设置界面,接收设置结果
 * 2.2 选择取消,回调fail方法,关闭对话框,关闭界面,结束
 * 3.接收设置结果,根据结果回调success/fail方法,关闭界面,结束
 * @author 龙
 */
public class AndroidOInstallPermissionActivity extends AppCompatActivity {

    private final int PERMISSION_CODE_INSTALL_PACKAGE = 1;
    private final int REQUEST_CODE_INSTALL_PERMISSION_SETTING = 1;

    private static IAndroidOInstallPermissionListener listener;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //请求权限,触发弹出授权对话框
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INSTALL_PACKAGES}, PERMISSION_CODE_INSTALL_PACKAGE);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listener = null;
    }

    public static void setListener(IAndroidOInstallPermissionListener listener) {
        AndroidOInstallPermissionActivity.listener = listener;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE_INSTALL_PACKAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //请求权限成功
                    if (listener != null) {
                        listener.permissionSuccess();
                    }
                    finish();
                } else {
                    showFailDialog();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 显示授权失败对话框,提示用户为何需要授权
     */
    private void showFailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setMessage("为了正常升级APP,请点击设置按钮,允许安装未知来源应用");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startInstallPermissionSettingActivity();
                mAlertDialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.permissionFailed();
                }
                mAlertDialog.dismiss();
                finish();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    /**
     * 跳转安装权限设置界面
     */
    private void startInstallPermissionSettingActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_INSTALL_PERMISSION_SETTING);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_INSTALL_PERMISSION_SETTING) {
            if (listener != null) {
                listener.permissionSuccess();
            }
        } else {
            if (listener != null) {
                listener.permissionFailed();
            }
        }
        finish();
    }
}
