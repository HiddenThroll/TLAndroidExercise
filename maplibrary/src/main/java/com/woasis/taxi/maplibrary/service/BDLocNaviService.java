package com.woasis.taxi.maplibrary.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.woasis.taxi.maplibrary.R;
import com.woasis.taxi.maplibrary.impl.OnLocationListener;
import com.woasis.taxi.maplibrary.model.LatLngData;
import com.woasis.taxi.maplibrary.util.MapUtils;

/**
 * Created by 龙 on 2017/9/30.
 */

public class BDLocNaviService {

    private Context mContext;
    private BaiduMap mBaiduMap;
    private MapUtils mMapUtils;

    public static int AMAP_NAVIGATION_TRANSIT = 1;
    public static int AMAP_NAVIGATION_DRIVING = 2;
    public static int AMAP_NAVIGATION_WALKING = 4;

    public static String BAIDU_NAVIGATION_TRANSIT = "transit";
    public static String BAIDU_NAVIGATION_DRIVING = "driving";
    public static String BAIDU_NAVIGATION_WALKING = "walking";

    public BDLocNaviService(Context mContext, BaiduMap mBaiduMap) {
        this.mContext = mContext;
        this.mBaiduMap = mBaiduMap;
        mMapUtils = new MapUtils();
    }

    /* *********************** 定位相关 ******************************* */

    /**
     * 启动定位，调用本方法前需确保已获取定位权限
     *
     * @param onLocationListener -- 定位监听器
     */
    public void startLocation(OnLocationListener onLocationListener) {
        // 设置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setIsNeedAddress(true);// 是否需要地址信息
        locationOption.setOpenGps(true);// 是否使用gps
        locationOption.setIgnoreKillProcess(false);//定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程
        locationOption.setCoorType("bd09ll");// 返回坐标类型

        startLocation(locationOption, onLocationListener);
    }

    /**
     * 启动定位，调用本方法前需确保已获取定位权限
     *
     * @param option             -- 定位参数
     * @param onLocationListener -- 定位监听器
     */
    public void startLocation(LocationClientOption option, final OnLocationListener onLocationListener) {
        final LocationClient locationClient = new LocationClient(mContext.getApplicationContext());//Context需要是全进程有效的Context,推荐用getApplicationContext获取全进程有效的Context
        locationClient.setLocOption(option);

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                locationClient.stop();

                if (onLocationListener != null) {
                    boolean locResult = false;
                    switch (bdLocation.getLocType()) {
                        case BDLocation.TypeCriteriaException:// 无法定位结果
                        case BDLocation.TypeNetWorkException:// 网络连接失败
                        case BDLocation.TypeNone:// 无效定位结果
                        case BDLocation.TypeOffLineLocationFail:// 离线定位失败
                        case BDLocation.TypeServerError:// server定位失败
                            locResult = false;
                            break;
                        case BDLocation.TypeGpsLocation:// GPS定位结果
                        case BDLocation.TypeNetWorkLocation:// 网络定位结果
                        case BDLocation.TypeOffLineLocation:// 离线定位成功
                            locResult = true;
                            break;
                    }
                    if (locResult) {
                        onLocationListener.onLocation(bdLocation);
                    } else {
                        onLocationListener.onLocationFailed();
                    }
                }
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        locationClient.start();
    }

    /**
     * 启动定位并移动地图中心至定位位置，调用本方法前需确保已获取定位权限
     *
     * @param onLocationListener -- 定位监听器
     */
    public void startLocationAndMoveCenter(final OnLocationListener onLocationListener) {
        if (mBaiduMap == null) {
            return;
        }
        startLocation(new OnLocationListener() {
            @Override
            public void onLocation(BDLocation bdLocation) {
                moveMapToLatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

                if (getMapZoom() < 15) {
                    setMapZoom(15);
                }
                if (onLocationListener != null) {
                    onLocationListener.onLocation(bdLocation);
                }
            }

            @Override
            public void onLocationFailed() {
                if (onLocationListener != null) {
                    onLocationListener.onLocationFailed();
                }
            }
        });
    }

    /**
     * 打开定位图层 并 显示定位位置
     * @param latitude -- 定位位置经度
     * @param longitude -- 定位位置纬度
     */
    public void openLocationOverlay(double latitude, double longitude) {
        if (mBaiduMap == null) {
            return;
        }
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationConfiguration configuration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null);
        mBaiduMap.setMyLocationConfiguration(configuration);
        MyLocationData myLocationData = new MyLocationData.Builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
        mBaiduMap.setMyLocationData(myLocationData);
    }
/* *********************** 定位相关 END ******************************* */

/* *********************** 导航相关 ******************************* */
    /**
     * 使用百度地图进行驾车导航
     *
     * @param latLng     -- 终点坐标
     * @param srcContent -- 调用来源，规则：companyName|appName
     */
    public void useBaiduNavigation(final LatLngData latLng, final String srcContent) {
        useBaiduNavigation(latLng, BAIDU_NAVIGATION_DRIVING, srcContent);
    }

    /**
     * 使用百度地图进行导航
     * @param latLng -- 终点坐标
     * @param mode -- 导航模式，transit代表公交，driving代表驾车，walking代表步行
     * @param srcContent -- 调用来源，规则：companyName|appName
     */
    public void useBaiduNavigation(LatLngData latLng, final String mode, final String srcContent) {
        if (!isBaiduMapInstall()) {
            return;
        }

        final LatLng bdLatLng = mMapUtils.changeCoordinateToBaidu(latLng);
        startLocation(new OnLocationListener() {
            @Override
            public void onLocation(BDLocation bdLocation) {
                String url = "intent://map/direction?origin=latlng:" + bdLocation.getLatitude()
                        + "," + bdLocation.getLongitude() + "|name:我的位置&destination="
                        + bdLatLng.latitude + "," + bdLatLng.longitude
                        + "&mode=" + mode + "&src=" + srcContent + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
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
     * 使用高德地图进行驾车导航
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
     * 使用高德地图进行路径规划
     * @param latLng -- 终点坐标
     * @param type -- 路径规划模式，1(公交) 2（驾车） 4(步行)
     */
    public void useAMapRoutePlan(LatLngData latLng, int type) {
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
            Uri uri = Uri.parse("androidamap://route?dlat=" + endLatLng.getmLatitude() + "&dlon=" +
                    endLatLng.getmLongitude() + "&dev=" + dev + "&t=" + type);
            intent.setData(uri);
            mContext.startActivity(intent);
        } catch (Exception e) {// 根据Bugtags记录，有时找不到高德地图
            Toast.makeText(mContext, "启动高德地图失败", Toast.LENGTH_SHORT).show();
        }
    }
/* *********************** 导航相关 END ******************************* */


/* *********************** 辅助功能 ******************************* */
    /**
     * 移动地图中心到指定的位置
     *
     * @param lat -- 纬度
     * @param lng -- 经度
     */
    public void moveMapToLatLng(double lat, double lng) {
        try {
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(lat, lng)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置地图缩放级别
     *
     * @param zoom -- 缩放等级
     */
    private void setMapZoom(float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(zoom);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
        mBaiduMap.animateMapStatus(mapStatusUpdate);
    }

    /**
     * 获得地图当前缩放级别
     *
     * @return -- 缩放级别
     */
    private float getMapZoom() {
        return getMapZoom(15);
    }

    /**
     * 获得地图当前缩放级别
     * @param def -- 默认值
     * @return
     */
    private float getMapZoom(float def) {
        try {
            def = mBaiduMap.getMapStatus().zoom;//根据Bugtags日志，有时无法获取地图状态
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    /**
     * 是否已安装百度地图
     *
     * @return
     */
    private boolean isBaiduMapInstall() {
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
    private boolean isAMapInstall() {
        boolean result = isAppInstalled(mContext, "com.autonavi.minimap");
        if (!result) {
            Toast.makeText(mContext, R.string.no_a_map_installed, Toast.LENGTH_SHORT).show();
        }
        return result;
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
            return false;
        } else {
            return true;
        }
    }
}
