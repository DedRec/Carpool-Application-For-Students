package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpool.helpers.HelperOrder;
import com.example.carpool.helpers.HelperTrip;
import com.example.carpool.model.FirebaseDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PaymentActivity extends AppCompatActivity {
    TextView driverNameT, timeT, sourceT, destinationT, carPlateT;
    Button paymentBtn,bypassBtn;
    RadioGroup radioGroup;
    ArrayList<String> orders;
    String orderid,driverName;
    private String driverid;
    private HelperTrip trip;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDB firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        firebaseDB = FirebaseDB.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        driverNameT = findViewById(R.id.driverNameValue);
        timeT = findViewById(R.id.timeValue);
        sourceT = findViewById(R.id.tripSourceValue);
        destinationT = findViewById(R.id.destinationValue);
        carPlateT = findViewById(R.id.carPlateValue);
        paymentBtn = findViewById(R.id.btn_add_payment);
        bypassBtn = findViewById(R.id.btn_bypass_time);
        radioGroup = findViewById(R.id.paymentMethodGroup);

        Intent intent = getIntent();
        driverid = intent.getStringExtra("driverId");
        firebaseDB.getDriverName(driverid, new FirebaseDB.DataCallback<String>() {
            @Override
            public void onDataLoaded(String data) {
                driverName = data;
                driverNameT.setText(data);
            }

            @Override
            public void onError(String errorMessage) {
            }
        });

        firebaseDB.getTrip(intent.getStringExtra("tripid"), new FirebaseDB.DataCallback<HelperTrip>() {
            @Override
            public void onDataLoaded(HelperTrip data) {
                trip = data;

            }

            @Override
            public void onError(String errorMessage) {
            }
        });
        firebaseDB.getTripOrders(intent.getStringExtra("tripid"), new FirebaseDB.DataCallback<ArrayList<String>>() {
            @Override
            public void onDataLoaded(ArrayList<String> data) {
                orders = data;
            }

            @Override
            public void onError(String errorMessage) {
                orders =  new ArrayList<>();
            }
        });

        firebaseDB.getOrderReference().orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int lastId = 0;

                for (DataSnapshot order : snapshot.getChildren()) {
                    String lastOrderId = order.getKey();
                    lastId = getLastId(lastOrderId);
                }

                lastId = lastId > 0 ? lastId : 0;

                lastId++;
                orderid = "order" + String.format("%03d", lastId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        timeT.setText(intent.getStringExtra("time"));
        sourceT.setText(intent.getStringExtra("source"));
        destinationT.setText(intent.getStringExtra("destination"));
        carPlateT.setText(intent.getStringExtra("carPlate"));

        bypassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(getApplicationContext(), "Please select a Payment method", Toast.LENGTH_SHORT).show();
                    radioGroup.requestFocus();
                    return;
                } else if(Integer.parseInt(trip.getPassengers_number()) == 0){
                    Toast.makeText(getApplicationContext(), "Trip Fully booked!", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(PaymentActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                    return;
                }
                int passengersInt = Integer.parseInt(trip.getPassengers_number());
                passengersInt-=1;
                String passengers = Integer.toString(passengersInt);
                orders.add(orderid);

                HelperOrder order = new HelperOrder(driverid,user.getUid(),intent.getStringExtra("source"),intent.getStringExtra("destination"),intent.getStringExtra("carPlate"),driverName,orderid,intent.getStringExtra("time"));
                firebaseDB.updateTripPassengerNumber(trip.getTripid(),passengers);
                firebaseDB.updateTripOrders(trip.getTripid(),orders);
                firebaseDB.insertOrder(orderid,order);
                firebaseDB.insertOrderDate(orderid,trip.getDate());

                Intent intent2 = new Intent(PaymentActivity.this, OrderActivity.class);
                startActivity(intent2);
                finish();
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(getApplicationContext(), "Please select a Payment method", Toast.LENGTH_SHORT).show();
                    radioGroup.requestFocus();
                    return;
                } else if(Integer.parseInt(trip.getPassengers_number()) == 0){
                    Toast.makeText(getApplicationContext(), "Trip Fully booked!", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(PaymentActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                    return;
                }
                if (!checkBookingAllowed(trip.getDate(),trip.getTime())) {
                    Toast.makeText(getApplicationContext(), "Booking not allowed for this trip", Toast.LENGTH_SHORT).show();
                    return;
                }
                int passengersInt = Integer.parseInt(trip.getPassengers_number());
                passengersInt-=1;
                String passengers = Integer.toString(passengersInt);
                orders.add(orderid);

                HelperOrder order = new HelperOrder(driverid,user.getUid(),intent.getStringExtra("source"),intent.getStringExtra("destination"),intent.getStringExtra("carPlate"),driverName,orderid,intent.getStringExtra("time"));
                firebaseDB.updateTripPassengerNumber(trip.getTripid(),passengers);
                firebaseDB.updateTripOrders(trip.getTripid(),orders);
                firebaseDB.insertOrder(orderid,order);
                firebaseDB.insertOrderDate(orderid,trip.getDate());

                Intent intent2 = new Intent(PaymentActivity.this, OrderActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }
    private int getLastId(String orderId) {
        try {
            return Integer.parseInt(orderId.replace("order", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    private boolean checkBookingAllowed(String tripDate, String tripTime) {
        // tripDate is in the format "MM/dd/yyyy" and tripTime is in the format "hh:mm a"
        String[] dateParts = tripDate.split("/");
        int monthOfTrip = Integer.parseInt(dateParts[0]);
        int tripDay = Integer.parseInt(dateParts[1]);
        int yearOfTrip = Integer.parseInt(dateParts[2]);

        // Get the current date and time
        Calendar currentCalendar = Calendar.getInstance();
        int currentMonth = currentCalendar.get(Calendar.MONTH) + 1; // Month is 0-based
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentHour = currentCalendar.get(Calendar.HOUR);
        int currentMinute = currentCalendar.get(Calendar.MINUTE);
        Log.d("TimeConstraint", "Current Month: " + currentMonth);
        Log.d("TimeConstraint", "Current Day: " + currentDay);
        Log.d("TimeConstraint", "Current Hour: " + currentHour);
        Log.d("TimeConstraint", "Current Minute: " + currentMinute);

        // Get the current hour in 12-hour format (AM/PM)
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String currentTimeFormatted = dateFormat.format(currentCalendar.getTime());

        Date parsedTime = null;
        try {
            parsedTime = dateFormat.parse(currentTimeFormatted);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        currentCalendar.setTime(parsedTime);
        currentCalendar.set(currentYear, currentMonth - 1, currentDay);

        Calendar tripCalendar = Calendar.getInstance();
        tripCalendar.set(yearOfTrip, monthOfTrip - 1, tripDay);

        String[] timeParts = tripTime.split(":");
        int parsedHour = Integer.parseInt(timeParts[0]);
        String tripAmPm = timeParts[1].split(" ")[1].trim(); // Extract AM/PM after minutes

        Log.d("TimeConstraint", "Trip Adj Year: " + (tripCalendar.get(Calendar.YEAR)));
        Log.d("TimeConstraint", "Trip Adj Month: " + (tripCalendar.get(Calendar.MONTH) + 1));
        Log.d("TimeConstraint", "Trip Adj Day: " + tripCalendar.get(Calendar.DAY_OF_MONTH));
        Log.d("TimeConstraint", "Trip Adjusted Hour: " + parsedHour);
        Log.d("TimeConstraint", "Trip Adjusted Minute: " + timeParts[1].split(" ")[0]);
        Log.d("TimeConstraint", "Trip Adjusted AM/PM: " + tripAmPm);

        tripCalendar.set(Calendar.HOUR_OF_DAY, parsedHour);
        tripCalendar.set(Calendar.MINUTE, Integer.parseInt(timeParts[1].split(" ")[0]));

        currentHour = currentCalendar.get(Calendar.HOUR);
        currentMinute = currentCalendar.get(Calendar.MINUTE);

        String currentAmPm = currentCalendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
        Log.d("TimeConstraint", "Current Adj Year: " + (currentCalendar.get(Calendar.YEAR)));
        Log.d("TimeConstraint", "Current Adj Month: " + (currentCalendar.get(Calendar.MONTH) + 1));
        Log.d("TimeConstraint", "Current Adj Day: " + (currentCalendar.get(Calendar.DAY_OF_MONTH)));
        Log.d("TimeConstraint", "Current Adjusted Hour: " + currentHour);
        Log.d("TimeConstraint", "Current Adjusted Minute: " + currentMinute);
        Log.d("TimeConstraint", "Current Adjusted AM/PM: " + currentAmPm);

        boolean isSameDayAndMonthAndYear =
                currentCalendar.get(Calendar.YEAR) == tripCalendar.get(Calendar.YEAR) &&
                        currentCalendar.get(Calendar.MONTH) == tripCalendar.get(Calendar.MONTH) &&
                        currentCalendar.get(Calendar.DAY_OF_MONTH) == tripCalendar.get(Calendar.DAY_OF_MONTH);

        boolean isNextDay =
                // Check for same month and consecutive days
                (currentCalendar.get(Calendar.YEAR) == tripCalendar.get(Calendar.YEAR) &&
                        currentCalendar.get(Calendar.MONTH) == tripCalendar.get(Calendar.MONTH) &&
                        currentCalendar.get(Calendar.DAY_OF_MONTH) == tripCalendar.get(Calendar.DAY_OF_MONTH) + 1) ||

                        // Check for end of month to beginning of next month
                        (tripCalendar.get(Calendar.DAY_OF_MONTH) == tripCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) &&
                                currentCalendar.get(Calendar.DAY_OF_MONTH) == 1 &&
                                currentCalendar.get(Calendar.MONTH) == tripCalendar.get(Calendar.MONTH) + 1) ||

                        // Check for last day of year to first day of next year
                        (tripCalendar.get(Calendar.DAY_OF_MONTH) == tripCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) &&
                                currentCalendar.get(Calendar.DAY_OF_MONTH) == 1 &&
                                currentCalendar.get(Calendar.YEAR) == tripCalendar.get(Calendar.YEAR) + 1);

        if (TextUtils.equals(tripTime,"7:30 AM")) {
            // Allow booking for 7:30 AM trip until 10:00 PM same day
            if(isSameDayAndMonthAndYear){
                if ((currentHour < 10 && TextUtils.equals(currentAmPm,"PM")) || TextUtils.equals(currentAmPm,"AM")||(currentHour == 12 && TextUtils.equals(currentAmPm,"PM"))) {
                    // Allow booking only if the trip time is before 10:00 pm or 12:00 pm (noon) on the trip date
                    Log.d("TimeConstraint", "Time constraint passed for 7:30AM");
                    return true;
                }else{
                    Log.d("TimeConstraint", "Time constraint NOT passed for 7:30AM");
                    return false;
                }
            }else{
                Log.d("TimeConstraint", "Time constraint NOT passed for 7:30AM, NOT SAME DAY");
                return false;
            }
        } else if (tripTime.equals("5:30 PM")) {
            // Allow booking for 5:30 PM trip until 1:00 PM next day
            if (isSameDayAndMonthAndYear) {
                Log.d("TimeConstraint", "All day is open so Time constraint passed for 5:30PM");
                return true;
            } else if (isNextDay) {
                if (TextUtils.equals(currentAmPm, "AM") || (TextUtils.equals(currentAmPm, "PM") && currentHour < 1)) {
                    Log.d("TimeConstraint", "Next day, so Time constraint Limited but passed for 5:30PM");
                    return true;
                } else {
                    Log.d("TimeConstraint", "Time constraint NOT passed for 5:30PM :(");
                    return false;
                }
            }
        }
        Log.d("TimeConstraint", "Time constraint didn't pass the day constraint");
        return false;
    }
}