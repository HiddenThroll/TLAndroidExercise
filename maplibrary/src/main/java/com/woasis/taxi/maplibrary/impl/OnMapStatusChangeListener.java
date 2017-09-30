package com.woasis.taxi.maplibrary.impl;

import com.baidu.mapapi.map.MapStatus;

/**
 * 百度地图移动地图监听器
 * Created by 龙 on 2016/7/21.
 */
public interface OnMapStatusChangeListener {

    /**
     * 地图状态开始变化时调用，
     * @param mapStatus -- 变化开始时地图状态数据
     */
    void onMapStatusChangeStart(MapStatus mapStatus);

    /**
     * 地图状态变化中调用
     * @param mapStatus -- 状态变化中，地图状态数据
     */
    void onMapStatusChange(MapStatus mapStatus);

    /**
     * 移动地图完毕后调用, 此时可进行更新数据操作
     *
     * @param mapStatus -- 移动完毕后地图状态数据
     */
    void onMapMoveFinish(MapStatus mapStatus);

    /**
     * 移动地图完毕后，地图缩放级别变化时调用
     * @param oldStatus -- 变化前地图状态数据
     * @param newStatus -- 变化后地图状态数据
     */
    void onMapZoomChange(MapStatus oldStatus, MapStatus newStatus);

}
