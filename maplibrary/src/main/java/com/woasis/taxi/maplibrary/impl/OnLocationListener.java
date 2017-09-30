package com.woasis.taxi.maplibrary.impl;

import com.baidu.location.BDLocation;

/**
 * 百度地图定位监听
 * Created by 龙 on 2016/9/12.
 */
public interface OnLocationListener {

    void onLocation(BDLocation bdLocation);

    void onLocationFailed();
}
