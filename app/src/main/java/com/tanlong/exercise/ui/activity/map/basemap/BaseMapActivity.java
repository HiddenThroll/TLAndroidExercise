package com.tanlong.exercise.ui.activity.map.basemap;

import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapView;
import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.LogTool;
import com.tanlong.maplibrary.BaiduMapService;
import com.tanlong.maplibrary.baiduImpl.OnLocationListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 基础地图界面
 * Created by Administrator on 2016/8/31.
 */
public class BaseMapActivity extends BaseActivity {

    @Bind(R.id.mv_baidu_map)
    MapView mBaiduMap;

    BaiduMapService mMapService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_map);
        ButterKnife.bind(this);

        initData();

        startLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBaiduMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaiduMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaiduMap.onDestroy();
    }

    private void initData() {
        mMapService = new BaiduMapService(this).initMapService(mBaiduMap);
    }

    private void startLocation() {
        mMapService.startLocationAndMoveCenter(new OnLocationListener() {
            @Override
            public void onLocation(BDLocation bdLocation) {
                LogTool.e(TAG, "定位成功");
            }

            @Override
            public void onLocationFailed() {
                LogTool.e(TAG, "定位失败");
            }
        });
    }
}
