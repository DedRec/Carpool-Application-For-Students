package com.example.carpool.helpers;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HelperTrip {
    String to,from,time,carPlate, driverId, tripid, passengers_number, date, tripState;
    ArrayList<String> orders;

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
        this.orders = new ArrayList<>();
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTripState() {
        return tripState;
    }

    public void setTripState(String tripState) {
        this.tripState = tripState;
    }

    public ArrayList<String> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<String> orders) {
        this.orders = orders;
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
