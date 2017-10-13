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
     * The position of this marker. This must always return the same value.
     */
    LatLng getPosition();

    BitmapDescriptor getBitmapDescriptor();

    Bundle getBundle();
}