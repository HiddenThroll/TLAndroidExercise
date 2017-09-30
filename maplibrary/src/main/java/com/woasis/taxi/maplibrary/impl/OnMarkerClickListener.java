package com.woasis.taxi.maplibrary.impl;


import com.baidu.mapapi.map.Marker;
import com.woasis.taxi.maplibrary.model.MarkDataBase;

/**
 * 百度地图 点击覆盖物监听
 * Created by 龙 on 2016/9/13.
 */
public interface OnMarkerClickListener<T> {

    void onMarkerClick(MarkDataBase<T> markDataBase, Marker marker);

}
