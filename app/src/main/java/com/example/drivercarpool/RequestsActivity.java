package com.example.drivercarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.drivercarpool.adapters.RequestAdapter;
import com.example.drivercarpool.items.RequestItem;
import com.example.drivercarpool.model.FirebaseDB;
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

public class RequestsActivity extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private RequestAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDB firebaseDB;

    Intent intent;
    ArrayList<RequestItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        firebaseDB = FirebaseDB.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        createItemList();
        buildRecyclerView();
    }
    public void createItemList(){
        itemList = new ArrayList<>();
    }
    public void buildRecyclerView(){
        myRecyclerView = findViewById(R.id.recyclerViewRequests);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);

        firebaseDB.getOrderReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RequestItem request = dataSnapshot.getValue(RequestItem.class);
                    if(TextUtils.equals(request.getDriverid(),user.getUid())){
                        if(TextUtils.equals(request.getOrderState(),"pending")){
                            if(isConfirmedTrip(request.getTripDate(),request.getOrderTime())){
                                itemList.add(request);
                            }else{
                                firebaseDB.updateOrderStateNotConfirmed(request.getOrderid());
                            }
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myAdapter = new RequestAdapter(itemList);

        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemListener(new RequestAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //myAdapter.notifyItemChanged(position);
            }

            @Override
            public void onConfirmClick(int position) {
                String orderId = itemList.get(position).getOrderid();
                firebaseDB.updateOrderStateConfirm(orderId);
            }
        });
    }
    private boolean isConfirmedTrip(String tripDate, String tripTime) {
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

        tripCalendar.set(Calendar.HOUR_OF_DAY, parsedHour);
        tripCalendar.set(Calendar.MINUTE, Integer.parseInt(timeParts[1].split(" ")[0]));

        currentHour = currentCalendar.get(Calendar.HOUR);
        currentMinute = currentCalendar.get(Calendar.MINUTE);

        String currentAmPm = currentCalendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

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
            // Automatically Not Confirm Orders after 11:30 PM
            if(isSameDayAndMonthAndYear){
                if ((currentHour < 11 && TextUtils.equals(currentAmPm,"PM"))||
                        (currentHour == 11 && currentMinute<=30 && TextUtils.equals(currentAmPm,"PM")|| TextUtils.equals(currentAmPm,"AM"))) {
                    return true;
                }else{
                    // Turn order status to not confirmed if still pending
                    return false;
                }
            }else{
                // Turn order status to not confirmed if still pending
                return false;
            }
        } else if (tripTime.equals("5:30 PM")) {
            // Automatically Not Confirm Orders after 4:30 PM
            if (isSameDayAndMonthAndYear) {
                return true;
            } else if (isNextDay) {
                if (TextUtils.equals(currentAmPm, "AM") || (TextUtils.equals(currentAmPm, "PM") && currentHour < 4) ||
                        (TextUtils.equals(currentAmPm, "PM") && currentHour == 4 && currentMinute<=30)) {
                    return true;
                } else {
                    // Turn order status to not confirmed if still pending
                    return false;
                }
            }
        }
        return false;
    }
}