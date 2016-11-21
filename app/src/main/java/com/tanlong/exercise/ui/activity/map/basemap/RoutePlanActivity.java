package com.tanlong.exercise.ui.activity.map.basemap;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.LogTool;
import com.tanlong.exercise.util.ToastHelp;
import com.tanlong.maplibrary.BaiduMapService;
import com.tanlong.maplibrary.baiduImpl.OnLocationListener;
import com.tanlong.maplibrary.baiduImpl.OnSearchDrivingRouteListener;
import com.tanlong.maplibrary.model.LatLngData;
import com.tanlong.maplibrary.overlay.DrivingRouteOverlay;
import com.tanlong.maplibrary.service.BDRoutePlanService;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 龙 on 2016/10/11.
 */
public class RoutePlanActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_start_point)
    EditText etStartPoint;
    @Bind(R.id.et_end_point)
    EditText etEndPoint;
    @Bind(R.id.btn_drive_search)
    Button btnDriveSearch;
    @Bind(R.id.mv_baidu_map)
    MapView mvBaiduMap;

    BaiduMapService mapService;
    BDRoutePlanService routePlanService;
    DrivingRouteResult drivingRouteResult;
    DrivingRouteOverlay drivingRouteOverlay;

    int routeIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_route_plan);
        ButterKnife.bind(this);

        initMap();
        initListener();
    }

    private void initMap() {
        mapService = new BaiduMapService(this);
        mapService.initBaiduMap(mvBaiduMap);
        routePlanService = new BDRoutePlanService(mvBaiduMap);

        mapService.startLocationAndMoveCenter(new OnLocationListener() {
            @Override
            public void onLocation(BDLocation bdLocation) {
                LatLngData src = new LatLngData(bdLocation.getLatitude(), bdLocation.getLongitude(),
                        LatLngData.LatLngType.BAIDU);
                LatLngData tar = new LatLngData(29.6195630000,106.5032380000,
                        LatLngData.LatLngType.BAIDU);
                routePlanService.searchDrivingPlanByLayLng(src, tar, -1,new OnSearchDrivingRouteListener() {
                    @Override
                    public void onNoResult() {
                        ToastHelp.showShortMsg(RoutePlanActivity.this, "未发现路径");
                    }

                    @Override
                    public void onGetResult(DrivingRouteResult result) {
                        LogTool.e(TAG, "route line size is " + result.getRouteLines().size());
                        drivingRouteResult = result;
                        drivingRouteOverlay = routePlanService.drawDrivingRouteLine(result.getRouteLines().get(0));
                    }
                });
            }

            @Override
            public void onLocationFailed() {

            }
        });
    }

    private void initListener() {
        btnDriveSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_drive_search:
                changeDrivingRoute();
                break;
        }
    }

    private void changeDrivingRoute() {
        int size = drivingRouteResult.getRouteLines().size();
        if (size == 1) {
            ToastHelp.showShortMsg(this, "只有一条路线，无法切换");
        } else {
            if (routeIndex >= size) {
                routeIndex = 0;
            }
            routePlanService.setStartMarkRes(R.mipmap.ic_location);
            drivingRouteOverlay.updateRoute(drivingRouteResult.getRouteLines().get(routeIndex));
            ToastHelp.showShortMsg(this, "这是第" + (routeIndex + 1) + "条路线");
            routeIndex++;
        }
    }
}
