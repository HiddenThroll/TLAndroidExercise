package com.tanlong.exercise.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限请求工具
 *
 * @author Administrator
 */
public class PermissionUtil {
    private final String TAG = "PermissionUtil";

    private Map<String, IRequestPermission> permissionUtilMap = new HashMap<>();

    /**
     * 百度地图定位所需权限
     */
    public static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 拍照所需权限
     */
    public static final String[] CAMERA_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 文件读写权限
     */
    public static final String[] FILE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    /**
     * 读取手机状态所需权限
     */
    public static final String[] PHONE_STATE = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    /**
     * 读取通讯录
     */
    public static final String[] READ_CONTACTS = new String[]{
            Manifest.permission.READ_CONTACTS
    };
    /**
     * 读取短信
     */
    public static final String[] READ_SMS = new String[] {
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
    };

    /**
     * 联合权限
     * @param permissionArray
     * @return
     */
    public String[] combinePermission(String[]... permissionArray) {
        List<String> resultList = new ArrayList<>();
        for (String[] permissions : permissionArray) {
            for (String permission : permissions) {
                if (!resultList.contains(permission)) {
                    resultList.add(permission);
                }
            }
        }
        String[] resultArray = new String[resultList.size()];
        return resultList.toArray(resultArray);
    }

    /**
     * 获取传入权限列表中还未授权的权限
     *
     * @param context
     * @param requestPermissions
     * @return
     */
    public List<String> getWaitedPermission(Context context, String[] requestPermissions) {
        List<String> waitPermissionList = new ArrayList<>();
        for (String permission : requestPermissions) {
            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                waitPermissionList.add(permission);
            }
        }
        return waitPermissionList;
    }

    /**
     * 请求权限
     *
     * @param activity           -- 请求的Activity
     * @param permissions        -- 权限组
     * @param requestCode        -- 请求code
     * @param iRequestPermission -- 请求回调
     */
    public void requestPermissions(Activity activity, String[] permissions, int requestCode,
                                   IRequestPermission iRequestPermission) {
        permissionUtilMap.put(activity.getClass().getName() + requestCode, iRequestPermission);
        List<String> waitPermission = getWaitedPermission(activity, permissions);
        if (waitPermission == null || waitPermission.isEmpty()) {
            if (iRequestPermission != null) {
                iRequestPermission.onSuccess();
            }
        } else {
            String[] permissionArray = new String[waitPermission.size()];
            ActivityCompat.requestPermissions(activity, waitPermission.toArray(permissionArray),
                    requestCode);
        }
    }

    /**
     * 处理请求结果
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void handlePermissionResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        IRequestPermission iRequestPermission = permissionUtilMap.get(activity.getClass().getName() + requestCode);

        if (iRequestPermission != null) {
            for (int i = 0, size = grantResults.length; i < size; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //还有未允许权限
                    iRequestPermission.onFailed(permissions[i]);
                    return;
                }
            }
            //全部允许
            iRequestPermission.onSuccess();
            //处理完毕后map不再维护这个回调
            permissionUtilMap.remove(activity.getClass().getName() + requestCode);
        }
    }

    /**
     * 是否应该提示用户手动设置某权限
     * @param activity
     * @param permission
     * @return
     */
    public boolean shouleManualSettingPermission(Activity activity, String permission) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                && ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
            //当 不应该展示请求权限原因 并且 该权限被拒绝时, 提示用户手动设置权限
            Logger.e(permission + "需要手动设置");
            return true;
        } else {
            return false;
        }
    }

    public interface IRequestPermission {
        void onSuccess();

        void onFailed(String permission);
    }
}
