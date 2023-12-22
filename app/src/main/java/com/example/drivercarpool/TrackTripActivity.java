package com.example.drivercarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;

import com.example.drivercarpool.adapters.StatusAdapter;
import com.example.drivercarpool.helpers.HelperTrip;
import com.example.drivercarpool.model.FirebaseDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class TrackTripActivity extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private StatusAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private ArrayList<HelperTrip> itemList;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDB firebaseDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_trip);
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
        myRecyclerView = findViewById(R.id.recycler_view_trips);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);

        firebaseDB.getTripsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HelperTrip trip = dataSnapshot.getValue(HelperTrip.class);
                    if(TextUtils.equals(trip.getDriverId(),user.getUid())){
                        if(!(TextUtils.equals(trip.getTripState(),"Completed")|TextUtils.equals(trip.getTripState(),"Canceled"))){
                            itemList.add(trip);
                        }
                    }
                }
                Collections.sort(itemList, new TimeComparatorHelperTrip());
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myAdapter = new StatusAdapter(itemList);

        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemListener(new StatusAdapter.onItemClickListener() {
            @Override
            public void onStartClick(int position) {
                String tripId = itemList.get(position).getTripid();
                firebaseDB.updateTripStateStarted(tripId);
                firebaseDB.updateTripOrders(tripId,"Started");
            }

            @Override
            public void onCompleteClick(int position) {
                String tripId = itemList.get(position).getTripid();
                firebaseDB.updateTripStateCompleted(tripId);
                firebaseDB.updateTripOrders(tripId,"Completed");
            }

            @Override
            public void onCancelClick(int position) {
                String tripId = itemList.get(position).getTripid();
                firebaseDB.updateTripStateCanceled(tripId);
                firebaseDB.updateTripOrders(tripId,"Canceled");
            }
        });
    }
}