package com.example.drivercarpool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.drivercarpool.model.FirebaseDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditActivity extends AppCompatActivity {
    private Button editBtn,cancelBtn;
    private DatePicker datePicker;
    private EditText sourceEditText, destinationEditText, carPlateEditText, passengersText;
    private Spinner timeSpinner;
    private FirebaseAuth auth;
    private FirebaseDB firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        firebaseDB = FirebaseDB.getInstance();
        auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();

        timeSpinner = findViewById(R.id.time_spinner);
        sourceEditText = findViewById(R.id.source_input);
        destinationEditText = findViewById(R.id.destination_input);
        carPlateEditText = findViewById(R.id.car_plate_input);
        passengersText = findViewById(R.id.passenger_input);
        datePicker = findViewById(R.id.datePicker);
        editBtn = findViewById(R.id.edit_btn);
        cancelBtn = findViewById(R.id.cancel_btn);

        if(TextUtils.equals(intent.getStringExtra("time"),"7:30 AM")){
            timeSpinner.setSelection(0);
        }else{
            timeSpinner.setSelection(1);
        }

        sourceEditText.setText(intent.getStringExtra("source"));
        destinationEditText.setText(intent.getStringExtra("destination"));
        carPlateEditText.setText(intent.getStringExtra("carPlate"));
        passengersText.setText(intent.getStringExtra("passengers"));


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = String.valueOf(timeSpinner.getSelectedItem());
                String source = String.valueOf(sourceEditText.getText());
                String destination = String.valueOf(destinationEditText.getText());
                String carPlate = String.valueOf(carPlateEditText.getText());
                String passengers = String.valueOf(passengersText.getText());
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String selectedDate = String.format("%02d/%02d/%04d", month, day, year);


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
                }else if (!(source.toLowerCase().contains("gate3")|source.toLowerCase().contains("gate4"))&!(destination.toLowerCase().contains("gate3")|destination.toLowerCase().contains("gate4"))) {
                    Toast.makeText(getApplicationContext(),"Destination must be Gate3/4 or Source must be Gate3/4", Toast.LENGTH_SHORT).show();
                    return;
                }

                String tripid = intent.getStringExtra("tripid");
                firebaseDB.updateTripDestination(tripid,destination);
                firebaseDB.updateTripSource(tripid,source);
                firebaseDB.updateTripTime(tripid,time);
                firebaseDB.updateTripCarPlate(tripid,carPlate);
                firebaseDB.updateTripPassengerNumber(tripid,passengers);
                firebaseDB.updateTripDate(tripid, selectedDate);

                Intent intent2 = new Intent(EditActivity.this,MainActivity.class);
                startActivity(intent2);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}