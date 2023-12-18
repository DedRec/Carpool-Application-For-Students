package com.example.carpool.items;

public class OrderItem {
    String driverid,userid,source, destination, carPlate, driverName, orderid, orderTime, orderPrice, orderState;
    public OrderItem() {
    }

    public OrderItem(String source, String destination, String carPlate, String driverName, String orderid, String orderTime, String orderState) {
        this.source = source;
        this.destination = destination;
        this.carPlate = carPlate;
        this.driverName = driverName;
        this.orderid = orderid;
        this.orderTime = orderTime;
        this.orderState = orderState;
        this.orderPrice = "$50";
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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
