package com.tanlong.maplibrary;

import android.graphics.Point;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.tanlong.maplibrary.model.LatLngData;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图工具类
 * Created by 龙 on 2016/9/12.
 */
public class MapUtils {

    private final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    public MapUtils() {
    }

    /************************ 坐标相关 ********************************/

    /**
     * 国测局GCJ-02坐标体系（谷歌、高德、腾讯）到百度坐标BD-09体系的转换
     *
     * @param gg_lat -- 国测局坐标 纬度
     * @param gg_lon -- 国测局坐标 经度
     * @return LatLngData
     */
    public LatLngData bd_encrypt(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new LatLngData(bd_lat, bd_lon, LatLngData.LatLngType.BAIDU);
    }


    /**
     * 百度坐标BD-09体系到国测局GCJ-02坐标体系（谷歌、高德、腾讯）的转换
     *
     * @param bd_lat -- 百度坐标系 纬度
     * @param bd_lon -- 百度坐标系 经度
     * @return LatLngData
     */
    public LatLngData bd_decrypt(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new LatLngData(gg_lat, gg_lon, LatLngData.LatLngType.GCJ_02);
    }

    /**
     * 将传入坐标转换为百度坐标系坐标
     *
     * @param latLngData -- 传入坐标数据
     * @return 对应的百度坐标数据
     */
    public LatLng changeCoordinateToBaidu(LatLngData latLngData) {
        LatLng latLng = null;
        CoordinateConverter converter = new CoordinateConverter();
        if (LatLngData.LatLngType.BAIDU.equals(latLngData.getmType())) {
            latLng = new LatLng(latLngData.getmLatitude(), latLngData.getmLongitude());
        } else if (LatLngData.LatLngType.GPS.equals(latLngData.getmType())) {// 传入坐标为GPS类型
            latLng = converter.from(CoordinateConverter.CoordType.GPS).coord(
                    new LatLng(latLngData.getmLatitude(), latLngData.getmLongitude())).convert();
        } else if (LatLngData.LatLngType.GCJ_02.equals(latLngData.getmType())) {// 传入坐标为GCJ02坐标
            latLng = converter.from(CoordinateConverter.CoordType.COMMON).coord(
                    new LatLng(latLngData.getmLatitude(), latLngData.getmLongitude())).convert();
        }
        return latLng;
    }

    /**
     * 将传入坐标转换为百度坐标系坐标, 返回数据为LatLngData格式
     *
     * @param latLngData -- 传入坐标数据
     * @return 对应的百度坐标数据
     */
    public LatLngData changeCoordinateToBDLatLngData(LatLngData latLngData) {
        LatLng latLng = changeCoordinateToBaidu(latLngData);
        LatLngData result = null;
        if (latLng != null) {
            result = new LatLngData(latLng.latitude, latLng.longitude, LatLngData.LatLngType.BAIDU);
            if (latLngData.getmType().equals(LatLngData.LatLngType.GPS)) {
                // 添加方向转变，GPS定义为 正北为0°，按顺时针方向增加
                result.setDirection(360 - latLngData.getDirection());
            } else if (latLngData.getmType().equals(LatLngData.LatLngType.BAIDU)) {
                // 添加方向转变，百度坐标定义为 正北为0°，按逆时针方向增加
                result.setDirection(latLngData.getDirection());
            }
            result.setTimeStamp(latLngData.getTimeStamp());
        }
        return result;
    }

    /**
     * 将传入坐标转换为GCJ02坐标系坐标
     *
     * @param latLngData -- 传入坐标数据
     * @return 对应GCJ02坐标
     */
    public LatLngData changeCoordinateToGCJ02(LatLngData latLngData) {
        LatLngData gcjLatLng = new LatLngData();
        CoordinateConverter converter = new CoordinateConverter();
        if (LatLngData.LatLngType.BAIDU.equals(latLngData.getmType())) {
            gcjLatLng = bd_decrypt(latLngData.getmLatitude(), latLngData.getmLongitude());
        } else if (LatLngData.LatLngType.GPS.equals(latLngData.getmType())) {
            // 通过百度坐标进行一次转换
            LatLng temp = converter.from(CoordinateConverter.CoordType.GPS).coord(
                    new LatLng(latLngData.getmLatitude(), latLngData.getmLongitude())).convert();
            gcjLatLng = bd_decrypt(temp.latitude, temp.longitude);
        } else if (LatLngData.LatLngType.GCJ_02.equals(latLngData.getmType())) {// 已是GCJ坐标，无需转换
            gcjLatLng = latLngData;
        }
        gcjLatLng.setTimeStamp(latLngData.getTimeStamp());
        return gcjLatLng;
    }

    /**
     * 将传入坐标转换为原始GPS坐标
     *
     * @param srcLatLng -- 传入坐标数据
     * @return 对应GPS坐标
     */
    public LatLngData changeCoordinateToGPS(LatLngData srcLatLng) {
        // 将原坐标转化为百度地图坐标
        LatLng bdLatLgn = changeCoordinateToBaidu(srcLatLng);
        // 将转换的坐标当成GPS坐标来使用，然后以此计算出对应地图的地图坐标
        CoordinateConverter converter = new CoordinateConverter();
        LatLng temp = converter.from(CoordinateConverter.CoordType.GPS).coord(
                new LatLng(bdLatLgn.latitude, bdLatLgn.longitude)).convert();
        // 将原坐标和新得到的坐标相减，就得到了偏移了，既然有了偏移，将该偏移和原坐标叠加，就反算出了对应的GPS坐标了
        double latitude = srcLatLng.getmLatitude() * 2 - temp.latitude;
        double longitude = srcLatLng.getmLongitude() * 2 - temp.longitude;
        LatLngData gpsLatLng = new LatLngData(latitude, longitude, LatLngData.LatLngType.GPS);

        if (srcLatLng.getmType().equals(LatLngData.LatLngType.GPS)) {
            // 添加方向转变，GPS定义为 正北为0°，按顺时针方向增加
            gpsLatLng.setDirection(360 - srcLatLng.getDirection());
        } else if (srcLatLng.getmType().equals(LatLngData.LatLngType.BAIDU)) {
            // 添加方向转变，百度坐标定义为 正北为0°，按逆时针方向增加
            gpsLatLng.setDirection(srcLatLng.getDirection());
        }
        gpsLatLng.setTimeStamp(srcLatLng.getTimeStamp());
        return gpsLatLng;
    }

    /**
     * 将屏幕坐标转换成地理坐标
     *
     * @param point -- 屏幕坐标
     * @return
     */
    public LatLngData changePointToLatLng(Point point,BaiduMap mBaiduMap) {
        LatLngData latLngData = new LatLngData();
        LatLng temp = mBaiduMap.getProjection().fromScreenLocation(point);
        latLngData.setmLatitude(temp.latitude);
        latLngData.setmLongitude(temp.longitude);
        latLngData.setmType(LatLngData.LatLngType.BAIDU);
        return latLngData;
    }

    /**
     * 将地理坐标转换为屏幕坐标
     *
     * @param srcLatLng -- 地理坐标
     * @return
     */
    public Point changeLatLngToPoint(LatLngData srcLatLng,BaiduMap mBaiduMap) {
        LatLng temp = changeCoordinateToBaidu(srcLatLng);
        return mBaiduMap.getProjection().toScreenLocation(temp);
    }

    /**
     * 扩充坐标
     *
     * @param srcLatLng -- 源坐标
     * @param tarLatLng -- 目的地坐标
     * @param timeBase  -- 分解坐标的时间基数，以毫秒为单位
     * @return -- 扩充后的坐标数据
     * @throws Exception
     */
    public List<LatLngData> fillLatLng(LatLngData srcLatLng, LatLngData tarLatLng, long timeBase)
            throws Exception {
        long timeDifference = tarLatLng.getTimeStamp() - srcLatLng.getTimeStamp();// 时间差，毫秒为单位
        if (timeDifference <= 0) {
            throw new Exception("tarTime不能小于srcTime!");
        }
        int count = 1;// 坐标扩展次数
        if (timeDifference <= timeBase) {
            count = 1;
        } else {
            count = (int) (timeDifference / timeBase);
            if (timeDifference % timeBase >= timeBase / 2) {
                count++;
            }
        }
        double latDiff = (tarLatLng.getmLatitude() - srcLatLng.getmLatitude()) / count;
        double longDiff = (tarLatLng.getmLongitude() - srcLatLng.getmLongitude()) / count;
        float dirDiff = (tarLatLng.getDirection() - srcLatLng.getDirection()) / count;
        List<LatLngData> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LatLngData data = new LatLngData();
            data.setmLatitude(srcLatLng.getmLatitude() + i * latDiff);
            data.setmLongitude(srcLatLng.getmLongitude() + i * longDiff);
            data.setDirection(srcLatLng.getDirection() + i * dirDiff);
            data.setmType(LatLngData.LatLngType.BAIDU);
            result.add(data);
        }
        return result;
    }

    public List<LatLngData> fillLatLngByDistance(LatLngData srcLatLng, LatLngData tarLatLng,
                                                 int disBase) {
        double distance = DistanceUtil.getDistance(changeCoordinateToBaidu(srcLatLng),
                changeCoordinateToBaidu(tarLatLng));
        int count = 0;
        if (distance <= disBase) {
            count = 1;
        } else {
            count = (int) (distance / disBase);
        }
        double latDiff = (tarLatLng.getmLatitude() - srcLatLng.getmLatitude()) / count;
        double longDiff = (tarLatLng.getmLongitude() - srcLatLng.getmLongitude()) / count;
        float dirDiff = (tarLatLng.getDirection() - srcLatLng.getDirection()) / count;
        List<LatLngData> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LatLngData data = new LatLngData();
            data.setmLatitude(srcLatLng.getmLatitude() + i * latDiff);
            data.setmLongitude(srcLatLng.getmLongitude() + i * longDiff);
            data.setDirection(srcLatLng.getDirection() + i * dirDiff);
            data.setmType(LatLngData.LatLngType.BAIDU);
            result.add(data);
        }
        return result;
    }

    /**
     * 根据路径填充坐标
     * @param routeLine -- 规划路径
     * @param timeInterval -- 获得起点终点数据时间差
     * @param isNeedDirection -- 是否需要设置方向，false默认方向为0
     * @return
     */
    public List<LatLngData> fillLatLngByRoute(DrivingRouteLine routeLine, long timeInterval,
                                              boolean isNeedDirection) {
        List<LatLngData> srcResult = new ArrayList<>();
        List<LatLngData> finalResult = new ArrayList<>();

        // 获得路径坐标，还需进一步扩充
        for (DrivingRouteLine.DrivingStep step : routeLine.getAllStep()) {
            for (LatLng latLng : step.getWayPoints()) {
                srcResult.add(new LatLngData(latLng.latitude, latLng.longitude, LatLngData.LatLngType.BAIDU));
            }
        }

        int count = (int) (timeInterval / 80 / srcResult.size());
        Log.e("MapUtils","fillLatLngByRoute() count is "+ count );
        for (int i = 0, size = srcResult.size(); i < size - 1; i++) {
            LatLngData startPoint = srcResult.get(i);
            LatLngData endPoint = srcResult.get(i + 1);
            LatLng fromPoint = new LatLng(startPoint.getmLatitude(), startPoint.getmLongitude());
            LatLng toPoint = new LatLng(endPoint.getmLatitude(), endPoint.getmLongitude());
            double angle = 0;
            if (isNeedDirection) {
                angle = getAngle(fromPoint, toPoint);
            }
            // 判断前后点的距离坐标，小于5米，不扩充
            if (DistanceUtil.getDistance(fromPoint, toPoint) < 5) {
                startPoint.setDirection((float) angle);
                finalResult.add(startPoint);
                continue;
            }

            double latDiff = (endPoint.getmLatitude() - startPoint.getmLatitude()) / count;
            double longDiff = (endPoint.getmLongitude() - startPoint.getmLongitude()) / count;
            for (int j = 0; j < count; j++) {// 扩充坐标
                LatLngData data = new LatLngData();
                data.setmLatitude(startPoint.getmLatitude() + j * latDiff);
                data.setmLongitude(startPoint.getmLongitude() + j * longDiff);
                data.setmType(LatLngData.LatLngType.BAIDU);
                data.setDirection((float) angle);
                finalResult.add(data);
            }
        }
        return finalResult;
    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }
}
