package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private ItemAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private FirebaseAuth auth;
    private FirebaseUser user;
    DatabaseReference reference;
    Intent intentMain;
    ArrayList<TripItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reference = FirebaseDatabase.getInstance().getReference("trips");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user == null){
            intentMain = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intentMain);
            finish();
        }

        createItemList();
        buildRecyclerView();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    public void createItemList(){
        itemList = new ArrayList<>();
    }
    public void buildRecyclerView(){
        myRecyclerView = findViewById(R.id.recycler_view_trips);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TripItem trip = dataSnapshot.getValue(TripItem.class);
                    if(Integer.parseInt(trip.getPassengers_number()) > 0){
                        itemList.add(trip);
                    }
                }
                Collections.sort(itemList, new TimeComparator());
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myAdapter = new ItemAdapter(itemList);

        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemListener(new ItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //myAdapter.notifyItemChanged(position);
            }

            @Override
            public void onBookClick(int position) {
                //itemList.get(position).setTo("Booked");
                //myAdapter.notifyItemChanged(position);
                String driverId = itemList.get(position).getDriverId();
                String source = itemList.get(position).getFrom();
                String destination = itemList.get(position).getTo();
                String carPlate = itemList.get(position).getCarPlate();
                String time = itemList.get(position).getTime();
                String tripid = itemList.get(position).getTripid();
                String passengers = itemList.get(position).getPassengers_number();
                Intent intent = new Intent(MainActivity.this,PaymentActivity.class);
                intent.putExtra("driverId",driverId);
                intent.putExtra("source",source);
                intent.putExtra("destination",destination);
                intent.putExtra("carPlate",carPlate);
                intent.putExtra("time",time);
                intent.putExtra("tripid",tripid);
                intent.putExtra("passenger",passengers);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.payment_info){
            intentMain = new Intent(MainActivity.this,PaymentActivity.class);
            startActivity(intentMain);
            return true;
        } else if (id == R.id.cart_info) {
            intentMain = new Intent(MainActivity.this,CartActivity.class);
            startActivity(intentMain);
            return true;
        } else if (id == R.id.order_history_info) {
            intentMain = new Intent(MainActivity.this,OrderActivity.class);
            startActivity(intentMain);
            return true;
        } else if (id == R.id.signout) {
            FirebaseAuth.getInstance().signOut();
            intentMain = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intentMain);
            finish();
            return true;
        }else if (id == R.id.profile_data){
            intentMain = new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(intentMain);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}