package com.example.drivercarpool.items;

public class RequestItem {
    String driverid,userid,source, destination, carPlate, driverName, orderid, orderTime, orderPrice, orderState;

    public RequestItem() {
    }

    public RequestItem(String driverid, String userid, String source, String destination, String carPlate, String driverName, String orderid, String orderTime, String orderPrice, String orderState) {
        this.driverid = driverid;
        this.userid = userid;
        this.source = source;
        this.destination = destination;
        this.carPlate = carPlate;
        this.driverName = driverName;
        this.orderid = orderid;
        this.orderTime = orderTime;
        this.orderPrice = orderPrice;
        this.orderState = orderState;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }
}
