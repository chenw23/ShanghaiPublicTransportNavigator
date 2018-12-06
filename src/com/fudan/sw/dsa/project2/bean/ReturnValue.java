package com.fudan.sw.dsa.project2.bean;

import java.util.List;

public class ReturnValue {
    private Address startPoint;
    private List<Address> subwayList;
    private Address endPoint;
    private double minutes;

    public Address getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Address startPoint) {
        this.startPoint = startPoint;
    }

    public List<Address> getSubwayList() {
        return subwayList;
    }

    public void setSubwayList(List<Address> subwayList) {
        this.subwayList = subwayList;
    }

    public Address getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Address endPoint) {
        this.endPoint = endPoint;
    }

    public double getMinutes() {
        return minutes;
    }

    public void setMinutes(double minutes) {
        this.minutes = minutes;
    }
}
