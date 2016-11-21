package com.tanlong.maplibrary.baiduImpl;


import com.baidu.mapapi.map.Marker;
import com.tanlong.maplibrary.model.MarkDataBase;

/**
 * Created by 龙 on 2016/9/13.
 */
public interface OnMarkerClickListener<T> {

   void onMarkerClick(MarkDataBase<T> markDataBase, Marker marker);

}
