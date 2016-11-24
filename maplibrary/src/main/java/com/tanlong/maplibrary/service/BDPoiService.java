package com.tanlong.maplibrary.service;

import com.baidu.mapapi.map.BaiduMap;
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
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.tanlong.maplibrary.MapUtils;
import com.tanlong.maplibrary.baiduImpl.OnRequestNearPoiListener;
import com.tanlong.maplibrary.baiduImpl.PoiSearchListener;
import com.tanlong.maplibrary.model.LatLngData;

import java.util.ArrayList;
import java.util.List;

/**
 * 百度POI服务
 * Created by 龙 on 2016/10/10.
 */
public class BDPoiService extends BDBaseService{

    private MapUtils mMapUtils;
    private BaiduMap mBaiduMap;

    public BDPoiService(BaiduMap baiduMap) {
        mBaiduMap = baiduMap;
        mMapUtils = new MapUtils();
    }

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

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

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

}
