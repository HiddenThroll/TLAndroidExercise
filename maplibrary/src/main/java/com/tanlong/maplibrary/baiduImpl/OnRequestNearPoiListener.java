package com.tanlong.maplibrary.baiduImpl;

import com.baidu.mapapi.search.core.PoiInfo;

/**
 * 通过经纬度获得最近的POI信息
 * Created by 龙 on 2016/9/13.
 */
public interface OnRequestNearPoiListener {

    void onGetNearPoiListener(PoiInfo poiInfo);

}
