package com.tanlong.maplibrary;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.tanlong.maplibrary.baiduImpl.OnLocationListener;
import com.tanlong.maplibrary.baiduImpl.OnMapClickListener;
import com.tanlong.maplibrary.baiduImpl.OnMapStatusChangeListener;
import com.tanlong.maplibrary.baiduImpl.OnMarkerClickListener;
import com.tanlong.maplibrary.baiduImpl.OnRequestAddressListener;
import com.tanlong.maplibrary.baiduImpl.OnRequestNearPoiListener;
import com.tanlong.maplibrary.baiduImpl.OnSearchDrivingRouteListener;
import com.tanlong.maplibrary.baiduImpl.PoiSearchListener;
import com.tanlong.maplibrary.model.LatLngData;
import com.tanlong.maplibrary.model.MarkDataBase;
import com.tanlong.maplibrary.overlay.CustomDrivingRouteOverlay;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 百度地图服务
 * Created by 龙 on 2016/9/12.
 */
public class BaiduMapService {

    private Context mContext;
    private BaiduMap mBaiduMap;
    private MapView mMapView;

    private MapUtils mMapUtils;

    private MapStatus mOldMapStatus;
    /**
     * 更新时间间隔，移动地图时，一定时间（默认20s）内只更新一次数据
     */
    private int updateTimeInterval = 20 * 1000;
    private long mLastUpdateTime;

    public static final String MARKER_DATA = "marker_data";
    private int mErrorPic;

    public BaiduMapService(Context mContext) {
        this.mContext = mContext;
        mErrorPic = R.mipmap.ic_marker_error;
    }

    public BaiduMapService initMapService(MapView mapView) {
        mMapView = mapView;
        mBaiduMap = mapView.getMap();
        mMapUtils = new MapUtils(mBaiduMap);
        return this;
    }

    public BaiduMap getmBaiduMap() {
        return mBaiduMap;
    }

    public MapView getmMapView() {
        return mMapView;
    }

    /************************ 地图操作相关 ********************************/

    /**
     * 移动地图中心到指定的位置
     *
     * @param lat -- 纬度
     * @param lng -- 经度
     */
    public void moveMapToLatLng(double lat, double lng) {
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(lat, lng)));
    }

    /**
     * 设置地图缩放级别
     *
     * @param zoom -- 缩放等级
     */
    public void setMapZoom(float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(zoom);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 获得地图当前缩放级别
     *
     * @return -- 缩放级别
     */
    public float getMapZoom() {
        return mBaiduMap.getMapStatus().zoom;
    }

    /**
     * 地图点击
     * @param onMapClickListener -- 地图点击监听
     */
    public void setMapClickListener(final OnMapClickListener onMapClickListener) {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LatLngData latLngData = new LatLngData(latLng.latitude, latLng.longitude,
                        LatLngData.LatLngType.BAIDU);
                onMapClickListener.onMapClick(latLngData);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    /************************ 定位相关 ********************************/
    private LocationClient mLocationClient;
    /**
     * 启动定位，定位成功后会自动调用LocationClient.stop()方法停止定位
     *
     * @param onLocationListener -- 定位监听器
     */
    public void startLocation(final OnLocationListener onLocationListener) {
        // 设置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setIsNeedAddress(true);
        locationOption.setOpenGps(true);
        locationOption.setIgnoreKillProcess(false);
        locationOption.setCoorType("bd09ll");

        startLocation(locationOption, onLocationListener);
    }

    public void startTimingLocation(int timeSpan, final OnLocationListener onLocationListener) {
        if (timeSpan < 1000 ) {
            Toast.makeText(mContext,"定位时间间隔不能小于1s", Toast.LENGTH_SHORT).show();
            return ;
        }

        mLocationClient = new LocationClient(mContext);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");// 设置返回坐标系
        option.setScanSpan(timeSpan);// 设置发起定位请求间隔
        option.setIsNeedAddress(true);// 是否需要地址信息，
        option.setOpenGps(true);// 是否使用GPS
        option.setIgnoreKillProcess(false);// 定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，true代表不杀死
        mLocationClient.setLocOption(option);

        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
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
        });
        mLocationClient.start();
    }

    public void closeTimingLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    /**
     * 启动定位，定位成功后会自动调用LocationClient.stop()方法停止定位
     *
     * @param option             -- 定位参数
     * @param onLocationListener -- 定位监听器
     */
    public void startLocation(LocationClientOption option, final OnLocationListener onLocationListener) {
        final LocationClient locationClient = new LocationClient(mContext);
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
        });
        locationClient.start();
    }

    /**
     * 启动定位并移动地图中心至定位位置，定位成功后会自动调用LocationClient.stop()方法停止定位
     *
     * @param onLocationListener -- 定位监听器
     */
    public void startLocationAndMoveCenter(final OnLocationListener onLocationListener) {
        startLocation(new OnLocationListener() {
            @Override
            public void onLocation(BDLocation bdLocation) {
                moveMapToLatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

                if (getMapZoom() < 15) {
                    setMapZoom(15);
                }
                onLocationListener.onLocation(bdLocation);
            }

            @Override
            public void onLocationFailed() {
                onLocationListener.onLocationFailed();
            }
        });
    }

    /**
     * 设置最外层显示View的布局参数，规避在部分低版本手机上使用BitmapDescriptorFactory.fromView(view)方法
     * 报空指针异常的bug
     *
     * @param view -- 最外层显示View
     */
    private void wrapMarkerView(View view) {
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /************************ 地址编译相关 ********************************/
    /**
     * 根据输入坐标数据获得对应地址信息
     *
     * @param latLng                   -- 坐标
     * @param onRequestAddressListener -- 地址反编译监听器
     */
    public void requestAddressByLatLng(LatLngData latLng, final OnRequestAddressListener onRequestAddressListener) {
        final GeoCoder geoCoder = GeoCoder.newInstance();

        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                onRequestAddressListener.onGetAddressListener(reverseGeoCodeResult);
                geoCoder.destroy();
            }
        });
        ReverseGeoCodeOption geoCodeOption = new ReverseGeoCodeOption();
        // 将自定义坐标转化为百度坐标
        LatLng bdLatLng = mMapUtils.changeCoordinateToBaidu(latLng);
        geoCodeOption.location(bdLatLng);
        // 发起地址反编译请求，将经纬度转换为地址
        geoCoder.reverseGeoCode(geoCodeOption);
    }

    /************************* 地图导航相关  ********************************/
    /**
     * 使用百度地图进行导航
     *
     * @param latLng     -- 终点坐标
     * @param srcContent -- 调用来源，规则：companyName|appName
     */
    public void useBaiduNavigation(final LatLngData latLng, final String srcContent) {
        if (!isAppInstalled(mContext, "com.baidu.BaiduMap")) {
            Toast.makeText(mContext, R.string.no_baidu_map_installed, Toast.LENGTH_SHORT).show();
            return;
        }

        final LatLng bdLatLng = mMapUtils.changeCoordinateToBaidu(latLng);
        startLocation(new OnLocationListener() {
            @Override
            public void onLocation(BDLocation bdLocation) {
                String url = "intent://map/direction?origin=latlng:" + bdLocation.getLatitude()
                        + "," + bdLocation.getLongitude() + "|name:我的位置&destination="
                        + bdLatLng.latitude + "," + bdLatLng.longitude
                        + "&mode=driving&src=" + srcContent + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
                try {
                    Intent intent = Intent.parseUri(url, 0);
                    mContext.startActivity(intent);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
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
        if (!isAppInstalled(mContext, "com.autonavi.minimap")) {
            Toast.makeText(mContext, R.string.no_a_map_installed, Toast.LENGTH_SHORT).show();
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

        Intent intent = new Intent("com.autonavi.minimap");
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        // dev是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
        // style指定导航方式，2代表路程短
        intent.setData(Uri.parse("androidamap://navi?sourceApplication=amap&poiname=fangheng&lat="
                + endLatLng.getmLatitude() + "&lon=" + endLatLng.getmLongitude() + "&dev=" + dev +
                "&style=2"));
        mContext.startActivity(intent);
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
        startLocation(new OnLocationListener() {
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

    /************************* 地图状态变化相关  ********************************/
    /**
     * 获得移动地图时更新时间间隔
     *
     * @return
     */
    public int getUpdateTimeInterval() {
        return updateTimeInterval;
    }

    /**
     * 设置移动地图时更新时间间隔，默认为20s
     *
     * @param updateTimeInterval -- 新的更新时间间隔
     */
    public void setUpdateTimeInterval(int updateTimeInterval) {
        this.updateTimeInterval = updateTimeInterval;
    }

    /**
     * 设置地图移动监听器
     *
     * @param statusChangeListener -- 移动地图监听器
     */
    public void setOnMapStatusChangeListener(final OnMapStatusChangeListener statusChangeListener) {

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                mOldMapStatus = mapStatus;
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                //TODO 缩放级别无变化且一定时间（默认20s）内只更新一次数据
                if (System.currentTimeMillis() - mLastUpdateTime >= updateTimeInterval) {
                    try {
                        if (mOldMapStatus.zoom == mapStatus.zoom) {
                            mLastUpdateTime = System.currentTimeMillis();
                            if (statusChangeListener != null) {
                                statusChangeListener.onMapMoveFinish(mapStatus);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // TODO 部分低版本手机会报空指针异常
                        mLastUpdateTime = System.currentTimeMillis();
                        if (statusChangeListener != null) {
                            statusChangeListener.onMapMoveFinish(mapStatus);
                        }
                    }
                }

                //TODO 缩放比例发生变化时回调
                try {
                    if (mOldMapStatus.zoom != mapStatus.zoom) {
                        if (statusChangeListener != null) {
                            statusChangeListener.onMapZoomChange(mOldMapStatus, mapStatus);
                        }
                    }
                } catch (Exception e) {
                    // TODO 部分低版本手机会报空指针异常，获取的MapStatusData为空，无法判断缩放比例变化
                    e.printStackTrace();
                }
            }
        });
    }

    /************************* POI相关  ********************************/

    /**
     * 城市内检索
     *
     * @param city              城市
     * @param keyword           检索关键字
     * @param pageSize          每页容量
     * @param poiSearchListener 检索监听事件
     */
    public void searchPoiInCity(String city, String keyword, int pageSize,
                                final PoiSearchListener poiSearchListener) {
        PoiSearch poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                List<PoiInfo> list = new ArrayList<>();
                if (poiResult.getAllPoi() == null || poiResult.getAllPoi().size() < 1) {
                    list = null;
                } else {
                    list.addAll(poiResult.getAllPoi());
                }
                poiSearchListener.onPoiResult(list);
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });
        PoiCitySearchOption option = new PoiCitySearchOption();
        option.city(city);
        option.keyword(keyword);
        option.pageCapacity(pageSize);
        poiSearch.searchInCity(option);
    }

    /**
     * 根据输入坐标数据获得最近的POI信息
     *
     * @param target                   -- 坐标
     * @param onRequestNearPoiListener -- 获取最近POI监听器
     */
    public void requestNearPoiByLayLgn(final LatLngData target, final OnRequestNearPoiListener onRequestNearPoiListener) {
        final GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                double distance = 5000;//便于统一处理
                PoiInfo nearPoiInfo = null;
                if (reverseGeoCodeResult == null) {// 请求中断网时，reverseGeoCodeResult有可能返回空
                    geoCoder.destroy();
                    return;
                }

                for (PoiInfo poiInfo : reverseGeoCodeResult.getPoiList()) {
                    LatLng bdTarget = mMapUtils.changeCoordinateToBaidu(target);
                    double temp = Math.abs(DistanceUtil.getDistance(bdTarget, poiInfo.location));
                    if (temp < distance) {
                        nearPoiInfo = poiInfo;
                        distance = temp;
                    }
                }

                if (onRequestNearPoiListener != null) {
                    onRequestNearPoiListener.onGetNearPoiListener(nearPoiInfo);
                }
                geoCoder.destroy();
            }
        });
        ReverseGeoCodeOption geoCodeOption = new ReverseGeoCodeOption();
        // 将自定义坐标转化为百度坐标
        LatLng bdLatLng = mMapUtils.changeCoordinateToBaidu(target);
        geoCodeOption.location(bdLatLng);
        // 发起地址反编译请求，将经纬度转换为地址
        geoCoder.reverseGeoCode(geoCodeOption);
    }

    /************************ Marker相关 ********************************/

    public int getmErrorPic() {
        return mErrorPic;
    }

    public void setmErrorPic(int mErrorPic) {
        this.mErrorPic = mErrorPic;
    }

    /**
     * 在地图上添加Marker并设置点击事件
     *
     * @param view   -- Marker使用的View
     * @param latLng -- Marker坐标
     * @param t      -- Marker携带的数据，用于业务处理
     */
    public Marker addMarker(View view, LatLngData latLng, MarkDataBase t) {
        return addMarker(view, latLng, t, 0);
    }

    /**
     * 在地图上添加Marker并设置点击事件
     *
     * @param view   -- Marker使用的View
     * @param latLng -- Marker坐标
     * @param t      -- Marker携带的数据，用于业务处理
     * @param errorPic -- 添加Marker出错时，使用的默认图片
     */
    public Marker addMarker(View view, LatLngData latLng, MarkDataBase t, int errorPic) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng positionLatLng = mMapUtils.changeCoordinateToBaidu(latLng);
        markerOptions.position(positionLatLng);
        wrapMarkerView(view);
        try {
            markerOptions.icon(BitmapDescriptorFactory.fromView(view));
        } catch (Exception e) {
            e.printStackTrace();
            if (errorPic != 0) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(errorPic));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(mErrorPic));
            }
        }

        Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);
        // 设置mark携带的数据，点击时调用
        Bundle bundle = new Bundle();
        bundle.putSerializable(MARKER_DATA, t);
        marker.setExtraInfo(bundle);

        return marker;
    }

    /**
     * 设置Marker点击响应
     * @param onMarkerClickListener -- Marker点击响应监听器
     */
    public void setOnMarkerClickListener(final OnMarkerClickListener onMarkerClickListener) {
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                MarkDataBase markDataBase = (MarkDataBase) marker.getExtraInfo().getSerializable(MARKER_DATA);
                if (onMarkerClickListener != null) {
                    onMarkerClickListener.onMarkerClick(markDataBase);
                }
                return false;
            }
        });
    }

    /**
     * 改变Marker的View和位置
     * @param marker -- 改变的Marker
     * @param view -- 改变后的View
     * @param latLng -- 改变后的地址
     */
    public void changeMarker(Marker marker, View view, LatLngData latLng) {
        LatLng positionLatLng = mMapUtils.changeCoordinateToBaidu(latLng);
        wrapMarkerView(view);
        marker.setIcon(BitmapDescriptorFactory.fromView(view));
        marker.setPosition(positionLatLng);
        marker.setRotate(latLng.getDirection());
    }

    /**
     * 显示弹出窗，一个地图中只会存在一个弹出窗，弹出窗会透传事件
     * @param latLngData -- 弹出窗坐标
     * @param view -- 弹出窗内容View
     * @param yOff -- 弹出窗针对坐标的Y轴偏移，像素为单位，负数上移，正数下移
     */
    public void showInfoWindow(LatLngData latLngData, View view, int yOff) {
        hideInfoWindow();
        LatLng latLng = mMapUtils.changeCoordinateToBaidu(latLngData);
        InfoWindow infoWindow = new InfoWindow(view, latLng, yOff);
        mBaiduMap.showInfoWindow(infoWindow);
    }

    public void hideInfoWindow() {
        mBaiduMap.hideInfoWindow();
    }

    /************************ 路径规划相关 ********************************/
    private int mStartMarkRes = 0;
    private int mEndMarkRes = 0;

    public void setStartMarkRes(int startMarkRes) {
        this.mStartMarkRes = startMarkRes;
    }

    public void setEndMarkRes(int endMarkRes) {
        this.mEndMarkRes = endMarkRes;
    }

    /**
     * 搜索驾车路线，并将路线绘制在地图上
     * @param src -- 起点坐标
     * @param tar -- 终点坐标
     * @param searchListener -- 搜索驾车路线监听器
     */
    public void searchDrivingPlanByLayLng(LatLngData src, LatLngData tar,
                                          final OnSearchDrivingRouteListener searchListener) {
        searchDrivingPlanByLayLng(src, tar, -1, searchListener);
    }

    /**
     * 搜索驾车路线，并将路线绘制在地图上
     * @param src -- 起点坐标
     * @param tar -- 终点坐标
     * @param policy -- 路径规划策略，-1代表躲避拥堵，0代表时间优先，1代表距离最短，2代表费用较少
     * @param searchListener -- 搜索驾车路线监听器
     */
    public void searchDrivingPlanByLayLng(LatLngData src, LatLngData tar, int policy,
                                          final OnSearchDrivingRouteListener searchListener) {
        searchDrivingPlanByLayLng(src, tar, policy, 0, 0, searchListener);
    }

    /**
     * 搜索驾车路线，并将路线绘制在地图上
     * @param src -- 起点坐标
     * @param tar -- 终点坐标
     * @param policy -- 路径规划策略，-1代表躲避拥堵，0代表时间优先，1代表距离最短，2代表费用较少
     * @param startMarkRes -- 起点图标，R.mipmap.XXX形式，默认为0，使用系统提供图标
     * @param endMarkRes -- 终点图标，R.mipmap.XXX形式，默认为0，使用系统提供图标
     * @param searchListener -- 搜索驾车路线监听器
     */
    public void searchDrivingPlanByLayLng(LatLngData src, LatLngData tar, int policy,
                                          int startMarkRes, int endMarkRes,
                                          final OnSearchDrivingRouteListener searchListener) {
        RoutePlanSearch planSearch = RoutePlanSearch.newInstance();
        PlanNode startNode = PlanNode.withLocation(mMapUtils.changeCoordinateToBaidu(src));
        PlanNode endNode = PlanNode.withLocation(mMapUtils.changeCoordinateToBaidu(tar));

        mStartMarkRes = startMarkRes;
        mEndMarkRes = endMarkRes;

        planSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                // 驾车路线结果回调
                if (result == null) {
                    //未找到结果
                    searchListener.onNoResult();
                } else {
                    if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                        int routeSize = result.getRouteLines().size();
                        Log.e("BaiduMapService", "规划有" + routeSize + "条路线");
                        if (routeSize > 0) {
                            CustomDrivingRouteOverlay overlay = new CustomDrivingRouteOverlay(mBaiduMap);
                            overlay.setStartMarkerRes(mStartMarkRes);
                            overlay.setEndMarkerRes(mEndMarkRes);
                            overlay.setData(result.getRouteLines().get(0));
                            overlay.addToMap();
                            overlay.zoomToSpan();

                            searchListener.onDrawRoute(result, overlay);
                        }
                    }
                }
            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });

        DrivingRoutePlanOption option = new DrivingRoutePlanOption()
                .from(startNode).to(endNode)
                .trafficPolicy(DrivingRoutePlanOption.DrivingTrafficPolicy.ROUTE_PATH_AND_TRAFFIC)// 默认返回路况信息
                .policy(getDrivingPolicy(policy));// 设置搜索策略

        planSearch.drivingSearch(option);
    }

    /**
     * 获得搜索驾车路线的策略
     * @param index -- 策略索引
     * @return 搜索驾车路线的策略
     */
    private DrivingRoutePlanOption.DrivingPolicy getDrivingPolicy(int index) {
        DrivingRoutePlanOption.DrivingPolicy policy;
        switch (index) {
            case -1:
                policy = DrivingRoutePlanOption.DrivingPolicy.ECAR_AVOID_JAM;
                break;
            case 0:
                policy = DrivingRoutePlanOption.DrivingPolicy.ECAR_TIME_FIRST;
                break;
            case 1:
                policy = DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST;
                break;
            case 2:
                policy = DrivingRoutePlanOption.DrivingPolicy.ECAR_FEE_FIRST;
                break;
            default:
                policy = DrivingRoutePlanOption.DrivingPolicy.ECAR_AVOID_JAM;
        }
        return policy;
    }


}
