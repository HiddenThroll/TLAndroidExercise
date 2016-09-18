package com.tanlong.maplibrary.baiduImpl;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * 通过经纬度获得地址信息
 * Created by 龙 on 2016/9/13.
 */
public interface OnRequestAddressListener {

    void onGetAddressListener(ReverseGeoCodeResult result);
}
