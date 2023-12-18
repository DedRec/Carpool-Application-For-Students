package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {
    TextView driverNameT, timeT, sourceT, destinationT, carPlateT;
    Button paymentBtn;
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
                orders.add(orderid);

                HelperOrder order = new HelperOrder(driverid,user.getUid(),intent.getStringExtra("source"),intent.getStringExtra("destination"),intent.getStringExtra("carPlate"),driverName,orderid,intent.getStringExtra("time"));
                firebaseDB.updateTripPassengerNumber(trip.getTripid(),passengers);
                firebaseDB.updateTripOrders(trip.getTripid(),orders);
                firebaseDB.insertOrder(orderid,order);

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
}