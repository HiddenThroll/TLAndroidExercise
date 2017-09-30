package com.woasis.taxi.maplibrary.overlay;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;

/**
 * 自定义的DrivingRouteOverlay，可自定义起点、终点图标，线路颜色等
 * Created by 龙 on 2016/10/12.
 */
public class CustomDrivingRouteOverlay extends DrivingRouteOverlay {
    private int startMarkerRes;
    private int endMarkerRes;

    /**
     * 构造函数
     *
     * @param baiduMap 该DrivingRouteOvelray引用的 BaiduMap
     */
    public CustomDrivingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
        startMarkerRes = 0;
        endMarkerRes = 0;
    }

    public CustomDrivingRouteOverlay setStartMarkerRes(int res) {
        startMarkerRes = res;
        return this;
    }

    public CustomDrivingRouteOverlay setEndMarkerRes(int res) {
        endMarkerRes = res;
        return this;
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        if (startMarkerRes == 0) {
            return null;
        } else {
            return BitmapDescriptorFactory.fromResource(startMarkerRes);
        }
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        if (endMarkerRes == 0) {
            return null;
        } else {
            return BitmapDescriptorFactory.fromResource(endMarkerRes);
        }
    }

}
