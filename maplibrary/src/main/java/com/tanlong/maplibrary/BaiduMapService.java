package com.tanlong.maplibrary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
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
import com.baidu.mapapi.utils.DistanceUtil;
import com.tanlong.maplibrary.baiduImpl.OnLocationListener;
import com.tanlong.maplibrary.baiduImpl.OnMapClickListener;
import com.tanlong.maplibrary.baiduImpl.OnMapStatusChangeListener;
import com.tanlong.maplibrary.baiduImpl.OnMarkerClickListener;
import com.tanlong.maplibrary.baiduImpl.OnRequestAddressListener;
import com.tanlong.maplibrary.baiduImpl.OnRequestNearPoiListener;
import com.tanlong.maplibrary.baiduImpl.PoiSearchListener;
import com.tanlong.maplibrary.model.LatLngData;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 百度地图服务
 * Created by 龙 on 2016/9/12.
 */
public class BaiduMapService {

    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private BaiduMap mBaiduMap;
    private MapView mMapView;

    private MapUtils mMapUtils=new MapUtils();

    private MapStatus mOldMapStatus;
    /**
     * 更新时间间隔，移动地图时，一定时间（默认20s）内只更新一次数据
     */
    private int updateTimeInterval = 20 * 1000;
    private long mLastUpdateTime;

    public static final String MARKER_DATA = "marker_data";
    private int mErrorPic;

    private OnMarkerClickListener mOnMarkerClickListener;
    //    private OnMapClickListener mOnMapClickListener;
//    private OnLocationListener mOnLocationListener;
    //导航权限
    private final static String authBaseArr[] =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};

    public BaiduMapService(Context mContext) {
        this.mContext = mContext;
        mErrorPic = R.mipmap.ic_marker_error;
    }
    public void initBaiduMap(MapView mapView) {
        mMapView = mapView;
        mBaiduMap = mapView.getMap();

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
        return getMapZoom(15);
    }

    public float getMapZoom(float def) {
        try {
            def = mBaiduMap.getMapStatus().zoom;//根据Bugtags日志，有时无法获取地图状态
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    /**
     * 地图点击
     *
     * @param onMapClickListener -- 地图点击监听
     */
    public void setOnMapClickListener(final OnMapClickListener onMapClickListener) {
//        mOnMapClickListener = onMapClickListener;
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (onMapClickListener != null) {
                    LatLngData latLngData = new LatLngData(latLng.latitude, latLng.longitude,
                            LatLngData.LatLngType.BAIDU);
                    onMapClickListener.onMapClick(latLngData);
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    /************************ 定位相关 ********************************/
    /**
     * 实时定位Client
     */
    private LocationClient mLocationClient;
    /**
     * 启动定位，定位成功后会自动调用LocationClient.stop()方法停止定位
     *
     * @param onLocationListener -- 定位监听器
     */
    public void startLocation(OnLocationListener onLocationListener) {
        // 设置定位参数
        startLocation(getNormalLocationOption(), onLocationListener);
    }

    /**
     * 获取常用的百度地图定位参数，即需要地址信息，使用gps，返回坐标类型为bd09ll
     * @return 百度地图定位参数
     */
    public LocationClientOption getNormalLocationOption() {
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setIsNeedAddress(true);// 是否需要地址信息
        locationOption.setOpenGps(true);// 是否使用gps
        locationOption.setIgnoreKillProcess(false);//定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程
        locationOption.setCoorType("bd09ll");// 返回坐标类型

        return locationOption;
    }

    /**
     * 启动定位，定位成功后会自动调用LocationClient.stop()方法停止定位
     *
     * @param option             -- 定位参数
     * @param onLocationListener -- 定位监听器
     */
    public void startLocation(LocationClientOption option, final OnLocationListener onLocationListener) {
//        mOnLocationListener = onLocationListener;
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
//        mOnLocationListener = onLocationListener;
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

                onLocationListener.onLocationFailed();
            }
        });
    }

    /**
     * 启动定时定位，通过closeTimingLocation方法停止定时定位
     * @param timeSpan -- 定位间隔，单位为ms，不能小于1000，否则无效
     * @param onLocationListener -- 定位监听器
     */
    public void startTimingLocation(int timeSpan, final OnLocationListener onLocationListener) {
        if (timeSpan < 1000 ) {
            Toast.makeText(mContext,"定位时间间隔不能小于1s", Toast.LENGTH_SHORT).show();
            return ;
        }

        mLocationClient = new LocationClient(mContext);
        LocationClientOption option = getNormalLocationOption();
        option.setScanSpan(timeSpan);// 设置发起定位请求间隔
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
                if (reverseGeoCodeResult.getPoiList()==null||reverseGeoCodeResult.getPoiList().size()<1){
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

    /************************
     * Marker相关
     ********************************/

    /**
     * 显示弹出窗，一个地图中只会存在一个弹出窗，弹出窗会透传事件
     *
     * @param latLngData -- 弹出窗坐标
     * @param view       -- 弹出窗内容View
     * @param yOff       -- 弹出窗针对坐标的Y轴偏移，像素为单位，负数上移，正数下移
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

}
