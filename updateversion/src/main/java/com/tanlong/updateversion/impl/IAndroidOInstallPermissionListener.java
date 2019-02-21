package com.tanlong.updateversion.impl;

/**
 * Android 8.0 申请安装权限监听器
 */
public interface IAndroidOInstallPermissionListener {

    void permissionSuccess();

    void permissionFailed();
}
