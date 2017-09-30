package com.woasis.taxi.maplibrary.impl;

import com.baidu.mapapi.search.route.DrivingRouteResult;

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
     * 获得驾车路线
     * @param result -- 搜索到的驾车路线结果
     */
    void onGetResult(DrivingRouteResult result);
}
