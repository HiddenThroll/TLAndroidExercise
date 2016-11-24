package com.tanlong.maplibrary.service;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.tanlong.maplibrary.MapUtils;
import com.tanlong.maplibrary.baiduImpl.OnSearchDrivingRouteListener;
import com.tanlong.maplibrary.model.LatLngData;
import com.tanlong.maplibrary.overlay.CustomDrivingRouteOverlay;
import com.tanlong.maplibrary.overlay.DrivingRouteOverlay;

/**
 * 百度路径规划服务
 * Created by 龙 on 2016/11/21.
 */

public class BDRoutePlanService extends BDBaseService {

    private MapUtils mMapUtils;
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    public BDRoutePlanService(MapView mapView) {
        mMapUtils = new MapUtils();
        mMapView = mapView;
        mBaiduMap = mMapView.getMap();
    }

    private int mStartMarkRes = 0;
    private int mEndMarkRes = 0;

    /**
     * 设置起点Marker图标资源
     * @param startMarkRes
     */
    public void setStartMarkRes(int startMarkRes) {
        this.mStartMarkRes = startMarkRes;
    }

    /**
     * 设置终点Marker图标资源
     * @param endMarkRes
     */
    public void setEndMarkRes(int endMarkRes) {
        this.mEndMarkRes = endMarkRes;
    }

    /**
     * 搜索驾车路线，并将路线绘制在地图上
     *
     * @param src            -- 起点坐标
     * @param tar            -- 终点坐标
     * @param searchListener -- 搜索驾车路线监听器
     */
    public void searchDrivingPlanByLayLng(LatLngData src, LatLngData tar,
                                          final OnSearchDrivingRouteListener searchListener) {
        searchDrivingPlanByLayLng(src, tar, 1, searchListener);
    }

    /**
     * 搜索驾车路线
     *
     * @param src            -- 起点坐标
     * @param tar            -- 终点坐标
     * @param policy         -- 路径规划策略，-1代表躲避拥堵，0代表时间优先，1代表距离最短，2代表费用较少
     * @param searchListener -- 搜索驾车路线监听器
     */
    public void searchDrivingPlanByLayLng(LatLngData src, LatLngData tar, int policy,
                                          final OnSearchDrivingRouteListener searchListener) {
        RoutePlanSearch planSearch = RoutePlanSearch.newInstance();
        PlanNode startNode = PlanNode.withLocation(mMapUtils.changeCoordinateToBaidu(src));
        PlanNode endNode = PlanNode.withLocation(mMapUtils.changeCoordinateToBaidu(tar));

        planSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                // 驾车路线结果回调
                if (result == null) {
                    //未找到结果
                    searchListener.onNoResult();
                } else {
                    if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                        int routeSize = result.getRouteLines().size();
                        if (routeSize > 0) {
                            searchListener.onGetResult(result);
                        } else {
                            searchListener.onNoResult();
                        }
                    }
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });

        DrivingRoutePlanOption option = new DrivingRoutePlanOption()
                .from(startNode).to(endNode)
                .trafficPolicy(DrivingRoutePlanOption.DrivingTrafficPolicy.ROUTE_PATH_AND_TRAFFIC)// 默认返回路况信息
                .policy(getDrivingPolicy(policy));// 设置搜索策略

        planSearch.drivingSearch(option);
    }

    /**
     * 获得搜索驾车路线的策略
     *
     * @param index -- 策略索引,-1代表躲避拥堵，0代表时间优先，1代表距离最短，2代表费用较少
     * @return 搜索驾车路线的策略
     */
    private DrivingRoutePlanOption.DrivingPolicy getDrivingPolicy(int index) {
        DrivingRoutePlanOption.DrivingPolicy policy;
        switch (index) {
            case -1:
                policy = DrivingRoutePlanOption.DrivingPolicy.ECAR_AVOID_JAM;
                break;
            case 0:
                policy = DrivingRoutePlanOption.DrivingPolicy.ECAR_TIME_FIRST;
                break;
            case 1:
                policy = DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST;
                break;
            case 2:
                policy = DrivingRoutePlanOption.DrivingPolicy.ECAR_FEE_FIRST;
                break;
            default:
                policy = DrivingRoutePlanOption.DrivingPolicy.ECAR_AVOID_JAM;
        }
        return policy;
    }

    /**
     * 绘制规划驾车路线
     *
     * @param routeLine -- 驾车路线
     * @return 绘制的驾车路线
     */
    public DrivingRouteOverlay drawDrivingRouteLine(DrivingRouteLine routeLine) {
        return drawDrivingRouteLine(routeLine, 0, 0);
    }

    /**
     * 绘制规划驾车路线
     *
     * @param routeLine      -- 驾车路线
     * @param startMarkerRes -- 起点图标
     * @param endMarkerRes   -- 终点图标
     * @return 绘制的驾车路线
     */
    public DrivingRouteOverlay drawDrivingRouteLine(DrivingRouteLine routeLine, int startMarkerRes,
                                                    int endMarkerRes) {
        CustomDrivingRouteOverlay routeOverlay = new CustomDrivingRouteOverlay(mBaiduMap);
        routeOverlay.setStartMarkerRes(startMarkerRes);
        routeOverlay.setEndMarkerRes(endMarkerRes);
        routeOverlay.setData(routeLine);
        routeOverlay.addToMap();
        routeOverlay.zoomToSpan();
        return routeOverlay;
    }

}
