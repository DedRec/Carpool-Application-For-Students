package com.example.drivercarpool;

import java.util.ArrayList;

public class HelperTrip {
    String to,from,time,carPlate, driverId, tripid, passengers_number;
    ArrayList<String> users;

    public HelperTrip() {
    }

    public HelperTrip(String to, String from, String time, String carPlate, String driverId, String tripid, String passengers_number) {
        this.to = to;
        this.from = from;
        this.time = time;
        this.carPlate = carPlate;
        this.driverId = driverId;
        this.tripid = tripid;
        this.passengers_number = passengers_number;
        this.users = new ArrayList<>();
    }

    public HelperTrip(String to, String from, String time, String carPlate, String driverId, String tripid) {
        this.to = to;
        this.from = from;
        this.time = time;
        this.carPlate = carPlate;
        this.driverId = driverId;
        this.tripid = tripid;
        this.passengers_number = "3";
    }



    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public String getPassengers_number() {
        return passengers_number;
    }

    public void setPassengers_number(String passengers_number) {
        this.passengers_number = passengers_number;
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

    public String getTime() {
        return time;
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

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
    }
}
