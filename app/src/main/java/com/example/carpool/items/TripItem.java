package com.example.carpool.items;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TripItem {
    String to,from,time,carPlate, driverId, tripid, passengers_number, date, day, dayOfMonth, month, year, tripState;
    ArrayList<String> orders;
    LinearLayout layout;

    public TripItem() {
    }

    public TripItem(String to, String from, String time, String carPlate) {
        this.to = to;
        this.from = from;
        this.time = time;
        this.carPlate = carPlate;
    }

    public void parseDate() {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

            Date date = sdf.parse(this.date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);

            this.month = new SimpleDateFormat("MMM", Locale.US).format(date);
            this.dayOfMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            this.day = new SimpleDateFormat("EEE", Locale.US).format(date);
            this.year = String.valueOf(calendar.get(Calendar.YEAR));
        }catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    public String getTripState() {
        return tripState;
    }

    public void setTripState(String tripState) {
        this.tripState = tripState;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPassengers_number() {
        return passengers_number;
    }

    public void setPassengers_number(String passengers_number) {
        this.passengers_number = passengers_number;
    }

    public ArrayList<String> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<String> orders) {
        this.orders = orders;
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
