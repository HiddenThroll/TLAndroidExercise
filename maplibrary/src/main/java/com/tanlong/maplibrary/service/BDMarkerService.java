package com.tanlong.maplibrary.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.tanlong.maplibrary.MapUtils;
import com.tanlong.maplibrary.baiduImpl.OnMarkerClickListener;
import com.tanlong.maplibrary.model.LatLngData;
import com.tanlong.maplibrary.model.MarkDataBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.tanlong.maplibrary.BaiduMapService.MARKER_DATA;

/**
 * Created by 龙 on 2016/11/21.
 */

public class BDMarkerService extends BDBaseService{
    private int mErrorPic;
    private MapUtils mMapUtils;
    private Context mContext;
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private OnMarkerClickListener mOnMarkerClickListener;

    public BDMarkerService(Context context, MapView mapView) {
        mMapUtils = new MapUtils();
        mContext = context;
        mMapView = mapView;
        mBaiduMap = mMapView.getMap();
    }

    public int getmErrorPic() {
        return mErrorPic;
    }

    public void setmErrorPic(int mErrorPic) {
        this.mErrorPic = mErrorPic;
    }

    /**
     * 设置最外层显示View的布局参数，规避在部分低版本手机上使用BitmapDescriptorFactory.fromView(view)方法
     * 报空指针异常的bug
     *
     * @param view -- 最外层显示View
     */
    private void wrapMarkerView(View view) {
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 在地图上添加Marker并设置点击事件
     *
     * @param view   -- Marker使用的View
     * @param latLng -- Marker坐标
     * @param t      -- Marker携带的数据，用于业务处理
     */
    public Marker addMarker(View view, LatLngData latLng, MarkDataBase t) {
        return addMarker(view, latLng, t, 0);
    }


    /**
     * 在地图上添加Marker并设置点击事件
     *
     * @param view     -- Marker使用的View
     * @param latLng   -- Marker坐标
     * @param t        -- Marker携带的数据，用于业务处理
     * @param errorPic -- 添加Marker出错时，使用的默认图片
     */
    public Marker addMarker(View view, LatLngData latLng, MarkDataBase t, int errorPic) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng positionLatLng = mMapUtils.changeCoordinateToBaidu(latLng);
        markerOptions.position(positionLatLng);
        wrapMarkerView(view);
        try {
            markerOptions.icon(BitmapDescriptorFactory.fromView(view));
        } catch (Exception e) {
            e.printStackTrace();
            if (errorPic != 0) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(errorPic));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(mErrorPic));
            }
        }

        Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);
        marker.setRotate(latLng.getDirection());
        // 设置mark携带的数据，点击时调用
        Bundle bundle = new Bundle();
        bundle.putSerializable(MARKER_DATA, t);
        marker.setExtraInfo(bundle);

        return marker;
    }

    /**
     * 添加Marker
     * @param drawable -- Marker图片资源
     * @param latLng -- Marker坐标
     * @param t -- Marker携带的数据，用于业务处理
     * @return
     * @throws Exception
     */
    public Marker addMarker(int drawable, LatLngData latLng, MarkDataBase t, int errorPic) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng positionLatLng = mMapUtils.changeCoordinateToBaidu(latLng);
        markerOptions.position(positionLatLng);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(drawable);
        if (bitmapDescriptor == null) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(mErrorPic));
            Toast.makeText(mContext, "addMarker(int drawable, LatLngData latLng, MarkDataBase t)传入图片资源无法解析",
                    Toast.LENGTH_SHORT).show();
        } else {
            markerOptions.icon(bitmapDescriptor);
        }
        Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);

        // 设置mark携带的数据，点击时调用
        Bundle bundle = new Bundle();
        bundle.putSerializable(MARKER_DATA, t);
        marker.setExtraInfo(bundle);
        return marker;
    }

    /**
     * 设置Marker点击响应
     *
     * @param onMarkerClickListener -- Marker点击响应监听器
     */
    public void setMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        mOnMarkerClickListener = onMarkerClickListener;
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                MarkDataBase markDataBase = (MarkDataBase) marker.getExtraInfo().getSerializable(MARKER_DATA);
                if (mOnMarkerClickListener != null) {
                    mOnMarkerClickListener.onMarkerClick(markDataBase, marker);
                }
                return false;
            }
        });
    }

    public void changeMarker(Marker marker, View view) {
        changeMarker(marker, view, null);
    }

    public void changeMarker(Marker marker, LatLngData latLng) {
        changeMarker(marker, null, latLng);
    }

    /**
     * 改变Marker的View和位置
     *
     * @param marker -- 改变的Marker
     * @param view   -- 改变后的View
     * @param latLng -- 改变后的地址
     */
    public void changeMarker(Marker marker, View view, LatLngData latLng) {
        if (view != null) {
            wrapMarkerView(view);
            marker.setIcon(BitmapDescriptorFactory.fromView(view));
        }
        if (latLng != null) {
            LatLng positionLatLng = mMapUtils.changeCoordinateToBaidu(latLng);
            marker.setPosition(positionLatLng);
            marker.setRotate(latLng.getDirection());
        }
    }

    public void clearMarker() {
        if (mBaiduMap == null) {
            return;
        }
        mBaiduMap.clear();
    }

    /**
     * 改变Marker的间隔时间, 单位为ms
     */
    private int mShowTimeInterval = 20;
    /**
     * 扩展坐标的间隔时间, 单位为ms
     */
    private int mHandleTimeInterval = 200;
    /**
     * 检测Marker的间隔时间, 单位为ms
     */
    private int mCheckMarkerTimeInterval = 30 * 1000;
    /**
     * Marker最长存在时间，单位为ms
     */
    private int mMarkerExistMaxTime = 30 * 1000;

    private Map<String, ChangeMarkerHandler> markerHandlerMap;
    private CheckMarkerHandler mCheckMarkerHandler;

    /**
     * 改变Marker的Handler
     */
    private class ChangeMarkerHandler extends Handler {
        private List<LatLngData> dataList;
        private Marker marker;
        private View view;
        private int count;
        private String index;

        public ChangeMarkerHandler(List<LatLngData> dataList, Marker marker, View view, String index) {
            this.dataList = dataList;
            this.marker = marker;
            this.view = view;
            this.count = 0;
            this.index = index;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {// 改变Marker
                if (count >= dataList.size()) {
                    // 已完成该次改变
                } else {
                    changeMarker(marker, view, dataList.get(count));
                    count++;
                    sendEmptyMessageDelayed(1, mShowTimeInterval);
                }
            } else if (msg.what == -1) {// 将Marker从地图上移除
                marker.remove();
                markerHandlerMap.remove(index);
            }
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setDataList(List<LatLngData> dataList) {
            this.dataList = dataList;
        }

        public void setMarker(Marker marker) {
            this.marker = marker;
        }

        public void setView(View view) {
            this.view = view;
        }

        public List<LatLngData> getDataList() {
            return dataList;
        }
    }

    private class CheckMarkerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                long curTime = System.currentTimeMillis();
                if (markerHandlerMap == null) {
                    sendEmptyMessageDelayed(1, mCheckMarkerTimeInterval);
                } else {
                    // 检测markerHandlerMap中的Marker的坐标数据是否已超过规定时间
                    for (Map.Entry<String, ChangeMarkerHandler> entry : markerHandlerMap.entrySet()) {
                        List<LatLngData> latLngDataList = entry.getValue().getDataList();
                        long latLngTime = latLngDataList.get(latLngDataList.size() - 1).getTimeStamp();
                        if (curTime - latLngTime > mMarkerExistMaxTime) {// 最后一个坐标的获取时间
                            //移除该Marker
                            entry.getValue().sendEmptyMessage(-1);
                        }
                    }
                    sendEmptyMessageDelayed(1, mCheckMarkerTimeInterval);
                }
            } else if (msg.what == -1) {
                removeMessages(1);
            }
        }
    }

    /**
     * 启动Marker改变任务
     *
     * @param index  -- 改变Marker的Tag
     * @param marker -- 改变的Marker
     * @param view   -- Marker使用的View
     * @param src    -- 起始坐标
     * @param tar    -- 目的地坐标
     */
    public void startChangeMarker(String index, Marker marker, View view, LatLngData src,
                                  LatLngData tar) {
        if (markerHandlerMap == null) {
            markerHandlerMap = new HashMap<>();
        }

        if (mCheckMarkerHandler == null) {
            mCheckMarkerHandler = new CheckMarkerHandler();
            mCheckMarkerHandler.sendEmptyMessage(1);
        }
        try {
            List<LatLngData> dataList = mMapUtils.fillLatLng(src, tar, mHandleTimeInterval);
            if (markerHandlerMap.containsKey(index)) {//已有该Marker
                // 取消原Marker改变，开始新marker改变
                ChangeMarkerHandler handler = markerHandlerMap.get(index);
                handler.removeMessages(1);//取消原Marker改变
                // 重置参数
                handler.setCount(0);
                handler.setDataList(dataList);
                handler.sendEmptyMessage(1);
                handler.setMarker(marker);
                handler.setView(view);
            } else {
                ChangeMarkerHandler handler = new ChangeMarkerHandler(dataList, marker, view, index);
                markerHandlerMap.put(index, handler);
                handler.sendEmptyMessage(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 启动Marker改变任务
     * @param index  -- 改变Marker的Tag
     * @param marker -- 改变的Marker
     * @param routeLine -- 起点坐标和终点坐标之间的规划路径
     * @param timeInterval -- 获取起点坐标与终点坐标之间的时间差值
     * @param isNeedDirection -- 是否需要方向信息，false默认方向为0；
     */
    public void startChangeMarker(String index, Marker marker, DrivingRouteLine routeLine,
                                  boolean isNeedDirection, int timeInterval) {
        startChangeMarker(index, marker, null, routeLine, isNeedDirection, timeInterval);
    }

    /**
     * 启动Marker改变任务
     * @param index  -- 改变Marker的Tag
     * @param marker -- 改变的Marker
     * @param view   -- Marker使用的View
     * @param routeLine -- 起点坐标和终点坐标之间的规划路径
     * @param timeInterval -- 获取起点坐标与终点坐标之间的时间差值
     * @param isNeedDirection -- 是否需要方向信息，false默认方向为0；
     */
    public void startChangeMarker(String index, Marker marker, View view, DrivingRouteLine routeLine,
                                  boolean isNeedDirection, int timeInterval) {
        if (markerHandlerMap == null) {
            markerHandlerMap = new HashMap<>();
        }

        if (mCheckMarkerHandler == null) {
            mCheckMarkerHandler = new CheckMarkerHandler();
            mCheckMarkerHandler.sendEmptyMessage(1);
        }

        List<LatLngData> dataList = mMapUtils.fillLatLngByRoute(routeLine, timeInterval * 1000, isNeedDirection);
        if (markerHandlerMap.containsKey(index)) {//已有该Marker
            // 取消原Marker改变，开始新marker改变
            ChangeMarkerHandler handler = markerHandlerMap.get(index);
            handler.removeMessages(1);//取消原Marker改变
            // 重置参数
            handler.setCount(0);
            handler.setDataList(dataList);
            handler.sendEmptyMessage(1);
            handler.setMarker(marker);
            handler.setView(view);
        } else {
            ChangeMarkerHandler handler = new ChangeMarkerHandler(dataList, marker, view, index);
            mShowTimeInterval = 200;
            markerHandlerMap.put(index, handler);
            handler.sendEmptyMessage(1);
        }
    }

    /**
     * 显示弹出窗，一个地图中只会存在一个弹出窗，弹出窗会透传事件
     *
     * @param latLngData -- 弹出窗坐标
     * @param view       -- 弹出窗内容View
     * @param yOff       -- 弹出窗针对坐标的Y轴偏移，像素为单位，负数上移，正数下移
     */
    public void showInfoWindow(LatLngData latLngData, View view, int yOff) {
        hideInfoWindow();
        LatLng latLng = mMapUtils.changeCoordinateToBaidu(latLngData);
        InfoWindow infoWindow = new InfoWindow(view, latLng, yOff);
        mBaiduMap.showInfoWindow(infoWindow);
    }

    public void hideInfoWindow() {
        mBaiduMap.hideInfoWindow();
    }
}
