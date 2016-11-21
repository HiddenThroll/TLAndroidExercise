package com.tanlong.maplibrary.model;

import java.io.Serializable;

/**
 * 自定义坐标类
 * Created by 龙 on 2016/7/19.
 */
public class LatLngData implements Serializable {

    private double mLatitude;
    private double mLongitude;
    private LatLngType mType;

    private float direction;// 方向，正北为0度，逆时针增加, 与百度地图坐标系相同
    private long timeStamp;// 获得该坐标的时间戳，毫秒为单位

    /**
     * 坐标系类型
     */
    public enum LatLngType {
        /** 百度坐标系 bd09ll*/
        BAIDU,
        /** 国测局02坐标系，适用于 高德地图 腾讯地图等*/
        GCJ_02,
        /** GPS数据*/
        GPS
    }

    public LatLngData() {
        timeStamp = System.currentTimeMillis();
    }

    public LatLngData(double mLatitude, double mLongitude, LatLngType mType) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mType = mType;
        timeStamp = System.currentTimeMillis();
    }

    public LatLngData(String mLatitude, String mLongitude, LatLngType mType) {
        this(Double.valueOf(mLatitude), Double.valueOf(mLongitude), mType);
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public LatLngType getmType() {
        return mType;
    }

    public void setmType(LatLngType mType) {
        this.mType = mType;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        if (direction > 360 || direction < 0) {
            direction = 0;
        }
        this.direction = direction;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "LatLngData{" +
                "mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mType=" + mType +
                ", direction=" + direction +
                '}';
    }
}
