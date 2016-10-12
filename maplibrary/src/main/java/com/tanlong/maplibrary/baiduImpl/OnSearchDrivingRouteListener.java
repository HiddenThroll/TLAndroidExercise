package com.tanlong.maplibrary.baiduImpl;

import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.tanlong.maplibrary.overlay.DrivingRouteOverlay;

/**
 * 搜索驾车路线
 * Created by 龙 on 2016/10/12.
 */
public interface OnSearchDrivingRouteListener {

    /**
     * 未搜索到结果
     */
    void onNoResult();

    /**
     * 绘制驾车路线
     * @param result -- 搜索到的驾车路线结果
     * @param overlay  -- 已默认绘制的驾车路线
     */
    void onDrawRoute(DrivingRouteResult result, DrivingRouteOverlay overlay);
}
