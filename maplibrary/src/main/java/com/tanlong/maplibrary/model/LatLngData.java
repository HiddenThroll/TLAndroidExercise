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

    private float direction;// 方向，正北为0度，逆时针增加

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
    }

    public LatLngData(double mLatitude, double mLongitude, LatLngType mType) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mType = mType;
    }

    public LatLngData(String mLatitude, String mLongitude, LatLngType mType) {
        this.mLatitude = Double.valueOf(mLatitude);
        this.mLongitude = Double.valueOf(mLongitude);
        this.mType = mType;
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
