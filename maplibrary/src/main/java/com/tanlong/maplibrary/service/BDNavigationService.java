package com.tanlong.maplibrary.service;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.tanlong.maplibrary.BaiduMapService;
import com.tanlong.maplibrary.MapUtils;
import com.tanlong.maplibrary.R;
import com.tanlong.maplibrary.baiduImpl.OnLocationListener;
import com.tanlong.maplibrary.model.LatLngData;

import java.net.URISyntaxException;

/**
 * 百度地图导航服务
 * Created by 龙 on 2016/11/22.
 */

public class BDNavigationService extends BDBaseService {
    private Context mContext;
    private MapUtils mMapUtils;
    private BaiduMapService mapService;

    public BDNavigationService(Context context) {
        mContext = context;
        mMapUtils = new MapUtils();
        mapService = new BaiduMapService(mContext);
    }
    /**
     * 使用百度地图进行导航
     *
     * @param latLng     -- 终点坐标
     * @param srcContent -- 调用来源，规则：companyName|appName
     */
    public void useBaiduNavigation(final LatLngData latLng, final String srcContent) {
        if (!isBaiduMapInstall()) {
            return;
        }

        final LatLng bdLatLng = mMapUtils.changeCoordinateToBaidu(latLng);
        mapService.startLocation(new OnLocationListener() {
            @Override
            public void onLocation(BDLocation bdLocation) {
                String url = "intent://map/direction?origin=latlng:" + bdLocation.getLatitude()
                        + "," + bdLocation.getLongitude() + "|name:我的位置&destination="
                        + bdLatLng.latitude + "," + bdLatLng.longitude
                        + "&mode=driving&src=" + srcContent + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
                try {
                    Intent intent = Intent.parseUri(url, 0);
                    mContext.startActivity(intent);
                } catch (Exception e) {// 根据Bugtags记录，有时找不到百度地图
                    e.printStackTrace();
                    Toast.makeText(mContext, "启动百度地图失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLocationFailed() {
                Toast.makeText(mContext, R.string.location_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 使用高德地图进行导航
     *
     * @param latLng -- 终点坐标
     */
    public void useAMapNavigation(LatLngData latLng) {
        if (!isAMapInstall()) {
            return;
        }

        // 将终点坐标转换为GCJ-02坐标
        LatLngData endLatLng = mMapUtils.changeCoordinateToGCJ02(latLng);

        // 判断坐标是否已偏移
        int dev = 0;
        if (latLng.getmType().equals(LatLngData.LatLngType.GPS)) {//GPS坐标，需要加密
            dev = 1;
        } else if (latLng.getmType().equals(LatLngData.LatLngType.BAIDU) ||
                latLng.getmType().equals(LatLngData.LatLngType.GCJ_02)) {// 百度坐标和国测局坐标已经加密
            dev = 0;
        }

        try {
            Intent intent = new Intent("com.autonavi.minimap");
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            // dev是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
            // style指定导航方式，2代表路程短
            intent.setData(Uri.parse("androidamap://navi?sourceApplication=amap&poiname=fangheng&lat="
                    + endLatLng.getmLatitude() + "&lon=" + endLatLng.getmLongitude() + "&dev=" + dev +
                    "&style=2"));
            mContext.startActivity(intent);
        } catch (Exception e) {// 根据Bugtags记录，有时找不到高德地图
            Toast.makeText(mContext, "启动高德地图失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 是否已安装百度地图
     *
     * @return
     */
    public boolean isBaiduMapInstall() {
        boolean result = isAppInstalled(mContext, "com.baidu.BaiduMap");
        if (!result) {
            Toast.makeText(mContext, R.string.no_baidu_map_installed, Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    /**
     * 是否已安装高德地图
     *
     * @return
     */
    public boolean isAMapInstall() {
        boolean result = isAppInstalled(mContext, "com.autonavi.minimap");
        if (!result) {
            Toast.makeText(mContext, R.string.no_a_map_installed, Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public void openBaiduMap() {
        if (!isBaiduMapInstall()) {
            return;
        }
        try {
            Intent intent = Intent.getIntent("baidumap://map");
            mContext.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void openAMap() {
        if (!isAMapInstall()) {
            return;
        }
        try {
            Intent intent = Intent.getIntent("androidamap://viewMap");
            mContext.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    /**
     * 检测传入包名对应的APP是否已安装
     *
     * @param context     -- 上下文
     * @param packagename -- 要检测APP的包名
     * @return true -- 已安装 false -- 未安装
     */
    private boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            //System.out.println("没有安装");
            return false;
        } else {
            //System.out.println("已经安装");
            return true;
        }
    }

    /**
     * 启动地图显示用户当前位置
     */
    public void startMapShowCurPosition() {
        mapService.startLocation(new OnLocationListener() {
            @Override
            public void onLocation(BDLocation bdLocation) {
                LatLngData latLngData = new LatLngData(bdLocation.getLatitude(),
                        bdLocation.getLongitude(), LatLngData.LatLngType.BAIDU);
                startMapShowPosition(latLngData);
            }

            @Override
            public void onLocationFailed() {
                Toast.makeText(mContext, R.string.location_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 启动地图显示指定位置
     *
     * @param target -- 指定位置坐标
     */
    public void startMapShowPosition(LatLngData target) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //TODO 这里要接收的是GPS坐标
        LatLngData temp = mMapUtils.changeCoordinateToGPS(target);
        Uri uri = Uri.parse("geo:" + temp.getmLatitude() + "," + temp.getmLongitude());
        intent.setData(uri);
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, R.string.no_map_app, Toast.LENGTH_SHORT).show();
        }
    }
}
