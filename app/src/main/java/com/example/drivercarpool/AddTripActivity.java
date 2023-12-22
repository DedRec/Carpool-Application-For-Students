package com.example.drivercarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.drivercarpool.helpers.HelperTrip;
import com.example.drivercarpool.model.FirebaseDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTripActivity extends AppCompatActivity {
    private String tripid;
    private Button addBtn;
    private EditText sourceEditText, destinationEditText, carPlateEditText, passengersText;
    private Spinner timeSpinner;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDB firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        firebaseDB = FirebaseDB.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        timeSpinner = findViewById(R.id.time_spinner);
        sourceEditText = findViewById(R.id.source_input);
        destinationEditText = findViewById(R.id.destination_input);
        carPlateEditText = findViewById(R.id.car_plate_input);
        passengersText = findViewById(R.id.passenger_input);
        addBtn = findViewById(R.id.add_btn);
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        firebaseDB.getTripsReference().orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int lastId = 0;

                for (DataSnapshot order : snapshot.getChildren()) {
                    String lastOrderId = order.getKey();
                    lastId = getLastId(lastOrderId);
                }

                lastId = lastId > 0 ? lastId : 0;

                lastId++;
                tripid = "trip" + String.format("%03d", lastId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = String.valueOf(timeSpinner.getSelectedItem());
                String source = String.valueOf(sourceEditText.getText());
                String destination = String.valueOf(destinationEditText.getText());
                String carPlate = String.valueOf(carPlateEditText.getText());
                String passengers = String.valueOf(passengersText.getText());
                String selectedDate = dateFormat.format(currentDate);

                if(TextUtils.isEmpty(source)){
                    Toast.makeText(getApplicationContext(),"Enter source",Toast.LENGTH_SHORT).show();
                    sourceEditText.requestFocus();
                    return;
                } else if(TextUtils.isEmpty(destination)){
                    Toast.makeText(getApplicationContext(),"Enter destination",Toast.LENGTH_SHORT).show();
                    destinationEditText.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(carPlate)) {
                    Toast.makeText(getApplicationContext(),"Enter Car Plate Number",Toast.LENGTH_SHORT).show();
                    carPlateEditText.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(passengers)) {
                    Toast.makeText(getApplicationContext(),"Enter Passengers Number",Toast.LENGTH_SHORT).show();
                    passengersText.requestFocus();
                    return;
                }

                if (time.equals("7:30 AM")) {
                    if (!destination.equalsIgnoreCase("Gate 3") &&
                            !destination.equalsIgnoreCase("Gate 4")) {
                        Toast.makeText(AddTripActivity.this, "Destination must be Gate 3 or Gate 4 when adding a 7:30AM Trip", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else if (time.equals("5:30 PM")) {
                    if (!source.equalsIgnoreCase("Gate 3") &&
                            !source.equalsIgnoreCase("Gate 4")) {
                        Toast.makeText(AddTripActivity.this, "Source must be Gate 3 or Gate 4 when adding a 5:30PM Trip", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                firebaseDB.checkIfTimeSlotAvailable(time, selectedDate, user.getUid(), new FirebaseDB.DataCallback<Boolean>() {
                    @Override
                    public void onDataLoaded(Boolean data) {
                        if (!data) {
                            Log.d("TimeSlot","Already taken");
                            Toast.makeText(AddTripActivity.this, "Trip already made for this Timeslot. Try another Timeslot", Toast.LENGTH_SHORT).show();

                        }else{
                            HelperTrip trip = new HelperTrip(destination,source,time,carPlate,user.getUid(),tripid,passengers,selectedDate);
                            firebaseDB.insertTrip(tripid,trip);

                            Toast.makeText(getApplicationContext(), "Trip added successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddTripActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
            }
        });
    }
    private int getLastId(String orderId) {
        try {
            return Integer.parseInt(orderId.replace("trip", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}