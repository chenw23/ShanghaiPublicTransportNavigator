package com.fudan.sw.dsa.project2.bean;

/**
 * For each station of subway
 * If you need other attribute, add it
 *
 * @author zjiehang
 */
public class Address {
    private String address;
    private double longitude;//经度
    private double latitude;//纬度

    public Address(String address, String longitude, String latitude) {
        this.address = address;
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
