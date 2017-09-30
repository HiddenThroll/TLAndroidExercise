package com.woasis.taxi.maplibrary.impl;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * 百度地图 地理反编译监听
 * Created by 龙 on 2016/9/13.
 */
public interface OnRequestAddressListener {

    void onGetAddressListener(ReverseGeoCodeResult result);
}
