/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.woasis.taxi.maplibrary.clusterutil.clustering;


import com.baidu.mapapi.model.LatLng;

import java.util.Collection;

/**
 * A collection of ClusterItems that are nearby each other.
 * 地图上相邻ClusterItem的集合
 */
public interface Cluster<T extends ClusterItem> {
    public LatLng getPosition();

    Collection<T> getItems();

    int getSize();
}