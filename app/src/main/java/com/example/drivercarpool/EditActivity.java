package com.example.drivercarpool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    Button editBtn,cancelBtn;
    EditText sourceEditText, destinationEditText, carPlateEditText, passengersText;
    Spinner timeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        reference = FirebaseDatabase.getInstance().getReference("trips");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent intent = getIntent();

        timeSpinner = findViewById(R.id.time_spinner);
        sourceEditText = findViewById(R.id.source_input);
        destinationEditText = findViewById(R.id.destination_input);
        carPlateEditText = findViewById(R.id.car_plate_input);
        passengersText = findViewById(R.id.passenger_input);
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
                DatabaseReference tripReference = reference.child(tripid);
                HelperTrip helper = new HelperTrip(destination,source,time,carPlate,user.getUid(),tripid,passengers);
                tripReference.setValue(helper);

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