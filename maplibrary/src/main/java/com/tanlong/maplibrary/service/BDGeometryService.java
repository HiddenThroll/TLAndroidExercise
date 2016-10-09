package com.tanlong.maplibrary.service;

import android.content.Context;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.tanlong.maplibrary.MapUtils;
import com.tanlong.maplibrary.model.LatLngData;

import java.util.ArrayList;
import java.util.List;

/**
 * 百度地图几何覆盖物服务
 * Created by 龙 on 2016/10/9.
 */
public class BDGeometryService {

    private MapUtils mMapUtils;
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private Context mContext;

    public BDGeometryService(Context context, MapView mapView) {
        mMapUtils = new MapUtils();
        mMapView = mapView;
        mBaiduMap = mMapView.getMap();
        mContext = context;
    }

    /**
     * 绘制圆
     * @param center -- 圆心坐标
     * @param circleColor -- 圆填充色，0xAARRGGBB形式
     * @param radius -- 圆半径，单位为米
     * @param strokeColor -- 圆边颜色，0xAARRGGBB形式
     * @param strokeWidth -- 圆边宽度，单位为像素
     * @param bundle -- 携带信息，可以为null
     * @return
     */
    public Circle drawCircle(LatLngData center, int circleColor, int radius, int strokeColor,
                             int strokeWidth, Bundle bundle) {
        Stroke stroke = new Stroke(strokeWidth, strokeColor);
        OverlayOptions circleOption = new CircleOptions()
                .center(mMapUtils.changeCoordinateToBaidu(center)).fillColor(circleColor)
                .radius(radius).stroke(stroke).extraInfo(bundle);
        return (Circle) mBaiduMap.addOverlay(circleOption);
    }

    /**
     * 绘制多边形
     * @param points -- 多边形的各个顶点坐标，传入坐标点不能少于3个
     * @param fillColor -- 多边形填充色，0xAARRGGBB形式
     * @param strokeColor -- 边框颜色，0xAARRGGBB形式
     * @param strokeWidth -- 边框宽度，单位为像素
     * @param bundle -- 携带信息，可以为null
     * @return
     */
    public Polygon drawPolygon(List<LatLngData> points, int fillColor, int strokeColor,
                               int strokeWidth, Bundle bundle) throws Exception {
        if (points == null || points.size() < 3) {
            throw new Exception("传入坐标点不能少于3个");
        }
        Stroke stroke = new Stroke(strokeWidth, strokeColor);
        List<LatLng> latlngList = new ArrayList<>();
        for (LatLngData point : points) {
            latlngList.add(mMapUtils.changeCoordinateToBaidu(point));
        }
        OverlayOptions polygonOption = new PolygonOptions().points(latlngList).fillColor(fillColor)
                .stroke(stroke).extraInfo(bundle);
        return (Polygon) mBaiduMap.addOverlay(polygonOption);
    }

    /**
     * 绘制折线
     * @param points -- 折线的端点坐标
     * @param fillColor -- 折线每个点的颜色值，每一个点带一个颜色值，绘制时按照索引依次取值
     * @param width -- 线宽度，单位为像素
     * @param isDottedLine -- true绘制虚线，false实线
     * @param bundle -- 携带数据，可以为null
     */
    public Polyline drawLines(List<LatLngData> points, List<Integer> fillColor, int width, boolean isDottedLine,
                              Bundle bundle) {
        List<LatLng> latlngList = new ArrayList<>();
        for (LatLngData point : points) {
            latlngList.add(mMapUtils.changeCoordinateToBaidu(point));
        }
        OverlayOptions lineOption = new PolylineOptions().points(latlngList).colorsValues(fillColor)
                .dottedLine(isDottedLine).width(width).extraInfo(bundle);
        return (Polyline) mBaiduMap.addOverlay(lineOption);
    }

    /**
     * 改变折线颜色
     * @param polyline -- 改变的折线
     * @param color -- 改变后的颜色，0xAARRGGBB形式
     * @return
     */
    public Polyline changeLineColor(Polyline polyline, int color) {
        polyline.setColor(color);
        return polyline;
    }

    /**
     * 改变折线宽度
     * @param polyline -- 改变的折线
     * @param width -- 改变后的宽度，像素为单位
     * @return
     */
    public Polyline changeLineWidth(Polyline polyline, int width) {
        polyline.setWidth(width);
        return polyline;
    }

    /**
     * 改变折线坐标
     * @param polyline -- 改变的折线
     * @param points -- 改变后的坐标
     * @return
     */
    public Polyline changeLinePoints(Polyline polyline, List<LatLngData> points) {
        List<LatLng> latLngList = new ArrayList<>();
        for (LatLngData latLngData : points) {
            latLngList.add(mMapUtils.changeCoordinateToBaidu(latLngData));
        }
        polyline.setPoints(latLngList);
        return polyline;
    }

    /**
     * 新增折线坐标
     * @param polyline -- 改变的坐标
     * @param point -- 新增的坐标点
     * @return
     */
    public Polyline addLinePoints(Polyline polyline, LatLngData point) {
        List<LatLng> latLngList = polyline.getPoints();
        latLngList.add(mMapUtils.changeCoordinateToBaidu(point));
        polyline.setPoints(latLngList);
        return polyline;
    }


    /**
     * 绘制纹理折线
     * @param points -- 折线的端点坐标
     * @param drawables -- 纹理资源，对应的图片必须放置在assets文件夹下，格式为"XXX.png"
     * @param index -- 折线每个点的纹理索引,每一个点带一个索引，绘制时按照索引从customTextureList里面取
     * @param width -- 折线宽度，单位为像素
     * @param bundle -- 携带数据，可以为null
     * @return
     */
    public Polyline drawTextureLines(List<LatLngData> points, List<String> drawables,
                                     List<Integer> index, int width, Bundle bundle) {
        List<LatLng> latlngList = new ArrayList<>();
        for (LatLngData point : points) {
            latlngList.add(mMapUtils.changeCoordinateToBaidu(point));
        }

        List<BitmapDescriptor> bitmapList = new ArrayList<>();
        for (String drawable : drawables) {
            bitmapList.add(BitmapDescriptorFactory.fromAsset(drawable));
        }
        OverlayOptions lineOption = new PolylineOptions()
                .points(latlngList)
                .width(width)
                .dottedLine(true)
                .customTextureList(bitmapList)
                .textureIndex(index)
                .extraInfo(bundle);
        return (Polyline) mBaiduMap.addOverlay(lineOption);
    }
}
