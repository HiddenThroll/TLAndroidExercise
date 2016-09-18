package com.tanlong.maplibrary.baiduImpl;

import com.baidu.mapapi.search.core.PoiInfo;

import java.util.List;

/**
 * POI搜索监听器
 * Created by 龙 on 2016/9/13.
 */
public interface PoiSearchListener {

    void onPoiResult(List<PoiInfo> list);
}
