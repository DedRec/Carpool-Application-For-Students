package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {
    TextView driverNameT, timeT, sourceT, destinationT, carPlateT;
    Button paymentBtn;
    RadioGroup radioGroup;
    int lastId = 0;
    String orderid,driverName;
    private String driverid;
    private HelperTrip trip;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference driverReference;
    private DatabaseReference orderReference;
    private DatabaseReference tripReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        driverReference = FirebaseDatabase.getInstance().getReference("drivers");
        orderReference = FirebaseDatabase.getInstance().getReference("orders");
        tripReference = FirebaseDatabase.getInstance().getReference("trips");

        driverNameT = findViewById(R.id.driverNameValue);
        timeT = findViewById(R.id.timeValue);
        sourceT = findViewById(R.id.tripSourceValue);
        destinationT = findViewById(R.id.destinationValue);
        carPlateT = findViewById(R.id.carPlateValue);
        paymentBtn = findViewById(R.id.btn_add_payment);
        radioGroup = findViewById(R.id.paymentMethodGroup);

        Intent intent = getIntent();
        Query checkUserDatabase = driverReference.orderByChild("userid").equalTo(intent.getStringExtra("driverId"));
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    driverName =  snapshot.child(intent.getStringExtra("driverId")).child("name").getValue(String.class);
                    driverid = snapshot.child(intent.getStringExtra("driverId")).child("userid").getValue(String.class);
                    driverNameT.setText(driverName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query checkTripDatabase = tripReference.orderByChild("tripid").equalTo(intent.getStringExtra("tripid"));
        checkTripDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    trip =  snapshot.child(intent.getStringExtra("tripid")).getValue(HelperTrip.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        orderReference.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot order : snapshot.getChildren()){
                    String lastOrderId = order.getKey();
                    lastId = getLastId(lastOrderId);
                }
                lastId = lastId > 0 ? lastId : 0;
                orderid = "order"+(++lastId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        timeT.setText(intent.getStringExtra("time"));
        sourceT.setText(intent.getStringExtra("source"));
        destinationT.setText(intent.getStringExtra("destination"));
        carPlateT.setText(intent.getStringExtra("carPlate"));

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
                int passengersInt = Integer.parseInt(trip.getPassengers_number());
                passengersInt-=1;
                String passengers = Integer.toString(passengersInt);

                HelperOrder order = new HelperOrder(driverid,user.getUid(),intent.getStringExtra("source"),intent.getStringExtra("destination"),intent.getStringExtra("carPlate"),driverName,orderid,intent.getStringExtra("time"));
                tripReference.child(trip.getTripid()).child("passengers_number").setValue(passengers);
                orderReference.child(orderid).setValue(order);

                Intent intent2 = new Intent(PaymentActivity.this, OrderActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }
    public int getLastId(String lastOrderId){
        return Integer.parseInt(lastOrderId.replaceAll("\\D+", ""));
    }
}