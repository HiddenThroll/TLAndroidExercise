package com.tanlong.maplibrary.baiduImpl;

import com.baidu.location.BDLocation;

/**
 * Created by 龙 on 2016/9/12.
 */
public interface OnLocationListener {

    void onLocation(BDLocation bdLocation);

    void onLocationFailed();
}
