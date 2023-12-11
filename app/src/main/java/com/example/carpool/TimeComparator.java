package com.example.carpool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

class TimeComparator implements Comparator<TripItem> {
    @Override
    public int compare(TripItem trip1, TripItem trip2) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mma", Locale.US);

        try {
            Date date1 = sdf.parse(trip1.getTime().replaceAll("\\s", ""));
            Date date2 = sdf.parse(trip2.getTime().replaceAll("\\s", ""));
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
