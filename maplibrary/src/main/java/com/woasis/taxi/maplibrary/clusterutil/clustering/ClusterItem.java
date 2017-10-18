/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.woasis.taxi.maplibrary.clusterutil.clustering;


import android.os.Bundle;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.model.LatLng;
import com.woasis.taxi.maplibrary.model.MarkDataBase;

/**
 * ClusterItem 代表 地图上的一个Marker
 * 包含2个方法，分别是 返回Marker坐标、返回Marker的BitmapDescriptor
 */
public interface ClusterItem {
    /**
     * 存储用于查找该Marker的Key的Key，具体使用方法为：
     * 在ClusterItem的实现类的getBundle()方法中，存储Marker的独有信息Key（如站点ID），
     * 即Bundle中存在MARKER_IDENTIFY -> Key的键值对，注意这里的Key必须是int类型
     */
    String MARKER_IDENTITY = "";
    /**
     * The position of this marker. This must always return the same value.
     */
    LatLng getPosition();

    /**
     * 决定Marker长什么样子
     * @return
     */
    BitmapDescriptor getBitmapDescriptor();

    /**
     * Marker携带的数据，用于业务处理
     * @return
     */
    Bundle getBundle();
}