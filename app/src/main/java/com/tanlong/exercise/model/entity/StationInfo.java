package com.tanlong.exercise.model.entity;

import java.io.Serializable;

/**
 * Created by 龙 on 2017/10/10.
 */

public class StationInfo implements Serializable{

    private String stationname;
    private String longtitude;
    private String latitude;
    private int ssid;
    private int canusenum;

    public StationInfo() {
    }

    public StationInfo(String stationname, String longtitude, String latitude) {
        this.stationname = stationname;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    public int getSsid() {
        return ssid;
    }

    public void setSsid(int ssid) {
        this.ssid = ssid;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    public int getCanusenum() {
        return canusenum;
    }

    public void setCanusenum(int canusenum) {
        this.canusenum = canusenum;
    }

    @Override

    public String toString() {
        return "StationInfo{" +
                "stationname='" + stationname + '\'' +
                ", longtitude='" + longtitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StationInfo that = (StationInfo) o;

        return ssid == that.ssid;

    }

    @Override
    public int hashCode() {
        return ssid;
    }
}
