package com.curry.signapp.bean;

import java.io.Serializable;

/**
 * Created by curry on 2017/4/4.
 */

public class SimpleLocation implements Serializable {
    //时间
    private String time;
    //经度
    private String longitude;
    //纬度
    private String latitude;

    public SimpleLocation() {
    }

    public SimpleLocation(String time, String longitude, String latitude) {
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "SimpleLocation{" +
                "time='" + time + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
