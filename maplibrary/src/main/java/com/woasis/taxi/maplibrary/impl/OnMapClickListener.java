package com.woasis.taxi.maplibrary.impl;


import com.woasis.taxi.maplibrary.model.LatLngData;

/**
 * 百度地图点击监听
 * Created by 龙 on 2016/9/13.
 */
public interface OnMapClickListener {

    void onMapClick(LatLngData latLng);

}
