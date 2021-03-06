/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.tanlong.exercise.ui.activity.map.basemap;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.StationInfo;
import com.tanlong.exercise.ui.activity.map.render.CustomClusterRenderer;
import com.tanlong.exercise.util.ToastHelp;
import com.woasis.taxi.maplibrary.BaiduMapService;
import com.woasis.taxi.maplibrary.clusterutil.clustering.Cluster;
import com.woasis.taxi.maplibrary.clusterutil.clustering.ClusterItem;
import com.woasis.taxi.maplibrary.clusterutil.clustering.ClusterManager;
import com.woasis.taxi.maplibrary.impl.OnLocationListener;
import com.woasis.taxi.maplibrary.impl.OnMapStatusChangeListener;
import com.woasis.taxi.maplibrary.impl.OnMarkerClickListener;
import com.woasis.taxi.maplibrary.model.LatLngData;
import com.woasis.taxi.maplibrary.model.MarkDataBase;
import com.woasis.taxi.maplibrary.service.BDLocNaviService;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 此Demo用来说明点聚合功能
 */
public class MarkerClusterDemoActivity extends Activity implements OnMapLoadedCallback, OnLocationListener {
    private final String TAG = getClass().getSimpleName();

    MapView mMapView;
    BaiduMap mBaiduMap;
    MapStatus ms;
    private ClusterManager<MyItem> mClusterManager;

    List<StationInfo> stationInfoList;
    List<MyItem> items;
    private BDLocNaviService locNaviService;
    private BaiduMapService baiduMapService;

    private boolean isAddTestMarker = false;
    private String addTestMarkerName = "测试添加Marker";

    private CustomClusterRenderer<MyItem> customClusterRenderer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_map);
        mMapView = (MapView) findViewById(R.id.mv_baidu_map);
        mBaiduMap = mMapView.getMap();
        locNaviService = new BDLocNaviService(this, mBaiduMap);
        locNaviService.startLocationAndMoveCenter(this);

        baiduMapService = new BaiduMapService(this);
        baiduMapService.initBaiduMap(mMapView);

        mBaiduMap.setOnMapLoadedCallback(this);
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        customClusterRenderer = new CustomClusterRenderer<>(this, mBaiduMap, mClusterManager);
        mClusterManager.setRenderer(customClusterRenderer);

        // 添加Marker点
        getData();
        addMarker();

        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnSingleMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public void onMarkerClick(MarkDataBase markDataBase, Marker marker) {
                ToastHelp.showShortMsg(MarkerClusterDemoActivity.this, (String)markDataBase.getData());
            }
        });

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                Toast.makeText(MarkerClusterDemoActivity.this,
                        "有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item, final Marker marker) {
                StationInfo stationInfo = (StationInfo) item.getBundle().getSerializable(MyItem.STATION_INFO);
                Toast.makeText(MarkerClusterDemoActivity.this,
                        "点击" + stationInfo.getStationname(), Toast.LENGTH_SHORT).show();

                //查找指定Marker(684)并改变
                Marker testMarker = mClusterManager.getMarkerByIdentify(684);
                if (testMarker != null) {
                    Log.e(TAG, "change testMarker");
                    for (MyItem myItem : items) {
                        StationInfo temp = (StationInfo) myItem.getBundle().getSerializable(MyItem.STATION_INFO);
                        if (temp.getSsid() == 684) {
                            myItem.setmBitmapDescriptor(R.mipmap.ic_launcher);
                        }
                    }
                    testMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                } else {
                    Log.e(TAG, "testMarker is null");
                }

                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mClusterManager.releaseResource();
        mMapView.onDestroy();
        super.onDestroy();
    }

    private void getData() {
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("StationData");
            StringBuffer stringBuffer = new StringBuffer();
            byte[] receiveData = new byte[1024];
            int realRead = 0;
            while ((realRead = inputStream.read(receiveData)) != -1) {
                byte[] temp = new byte[realRead];
                System.arraycopy(receiveData, 0, temp, 0, temp.length);
                stringBuffer.append(new String(temp, "utf-8"));
            }
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            String station = jsonObject.getJSONObject("body").getString("station");
            stationInfoList = new Gson().fromJson(station,
                    new TypeToken<List<StationInfo>>(){}.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向地图添加Marker点
     */
    private void addMarker() {
        items = new ArrayList<>();
        for (StationInfo stationInfo : stationInfoList) {
            LatLng latLng = new LatLng(Double.valueOf(stationInfo.getLatitude()),
                    Double.valueOf(stationInfo.getLongtitude()));
            items.add(new MyItem(latLng, stationInfo));
        }
        mClusterManager.addItems(items);
    }

    private void testAddMarker() {
        if (!isAddTestMarker) {
            isAddTestMarker = true;
            LatLng latLng = new LatLng(29.613586, 106.553256);
            StationInfo stationInfo = new StationInfo(addTestMarkerName,
                    String.valueOf(latLng.longitude), String.valueOf(latLng.latitude));
            MyItem myItem = new MyItem(latLng, stationInfo);
            mClusterManager.addItem(myItem);
            mClusterManager.cluster();//手动触发点聚合计算
        }
    }

    @Override
    public void onLocation(BDLocation bdLocation) {
        LatLngData target = new LatLngData(bdLocation.getLatitude(), bdLocation.getLongitude(),
                LatLngData.LatLngType.BAIDU);
        MarkDataBase<String> markDataBase = new MarkDataBase<>();
        markDataBase.setData("测试数据");
        baiduMapService.addMarker(R.mipmap.ic_launcher, target, markDataBase, 0);
    }

    @Override
    public void onLocationFailed() {

    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        private StationInfo mStationInfo;
        public static final String STATION_INFO = "station_info";
        private BitmapDescriptor mBitmapDescriptor;

        public MyItem(LatLng latLng, StationInfo stationInfo) {
            mPosition = latLng;
            mStationInfo = stationInfo;
            mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        }

        public void setmBitmapDescriptor(int res) {
            mBitmapDescriptor = BitmapDescriptorFactory.fromResource(res);
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return mBitmapDescriptor;
        }

        @Override
        public Bundle getBundle() {
            Bundle bundle = new Bundle();
            bundle.putSerializable(STATION_INFO, mStationInfo);
            bundle.putInt(MARKER_IDENTITY, mStationInfo.getSsid());
            return bundle;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MyItem myItem = (MyItem) o;

            return mStationInfo != null ? mStationInfo.equals(myItem.mStationInfo) : myItem.mStationInfo == null;

        }

        @Override
        public int hashCode() {
            return mStationInfo != null ? mStationInfo.hashCode() : 0;
        }
    }

    @Override
    public void onMapLoaded() {
        // TODO Auto-generated method stub
        ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }
}
