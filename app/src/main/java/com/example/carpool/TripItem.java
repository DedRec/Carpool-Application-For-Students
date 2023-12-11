package com.example.carpool;

import android.widget.LinearLayout;

import java.util.ArrayList;

public class TripItem {
    String to,from,time,carPlate, driverId, tripid, passengers_number;
    ArrayList<String> users;
    LinearLayout layout;

    public TripItem() {
    }

    public TripItem(String to, String from, String time, String carPlate) {
        this.to = to;
        this.from = from;
        this.time = time;
        this.carPlate = carPlate;
    }

    public String getPassengers_number() {
        return passengers_number;
    }

    public void setPassengers_number(String passengers_number) {
        this.passengers_number = passengers_number;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getTime() {
        return time;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}
