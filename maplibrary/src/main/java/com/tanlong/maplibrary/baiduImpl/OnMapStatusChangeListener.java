package com.tanlong.maplibrary.baiduImpl;

import com.baidu.mapapi.map.MapStatus;

/**
 * 移动地图监听器
 * Created by 龙 on 2016/7/21.
 */
public interface OnMapStatusChangeListener {

    /**
     * 移动地图完毕后调用, 此时可进行更新数据操作
     *
     * @param mapStatus -- 地图状态数据
     */
    void onMapMoveFinish(MapStatus mapStatus);

    /**
     * 地图缩放级别变化时调用，
     * @param oldStatus -- 变化前地图状态数据
     * @param newStatus -- 变化后地图状态数据
     */
    void onMapZoomChange(MapStatus oldStatus, MapStatus newStatus);

}
