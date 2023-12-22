package com.example.drivercarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.drivercarpool.adapters.TripHistoryAdapter;
import com.example.drivercarpool.helpers.HelperTrip;
import com.example.drivercarpool.model.FirebaseDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripHistoryActivity extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private TripHistoryAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    ArrayList<HelperTrip> itemList;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDB firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        firebaseDB = FirebaseDB.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        createItemList();
        buildRecyclerView();
    }
    public void createItemList(){
        itemList = new ArrayList<>();
    }
    public void buildRecyclerView(){
        myRecyclerView = findViewById(R.id.recyclerViewTripHistory);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);

        firebaseDB.getTripsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HelperTrip trip = dataSnapshot.getValue(HelperTrip.class);
                    if(TextUtils.equals(trip.getDriverId(),user.getUid())) {
                        if (TextUtils.equals(trip.getTripState(), "Completed") | TextUtils.equals(trip.getTripState(), "Canceled")) {
                            itemList.add(trip);
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myAdapter = new TripHistoryAdapter(itemList);
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);
    }
}