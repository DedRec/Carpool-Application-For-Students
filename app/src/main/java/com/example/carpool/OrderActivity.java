package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private OrderAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    ArrayList<OrderItem> itemList;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        reference = FirebaseDatabase.getInstance().getReference("orders");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(OrderActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        createItemList();
        buildRecyclerView();
    }
    public void createItemList(){
        itemList = new ArrayList<>();
    }
    public void buildRecyclerView(){
        myRecyclerView = findViewById(R.id.recyclerViewOrderHistory);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    OrderItem order = dataSnapshot.getValue(OrderItem.class);
                    if(TextUtils.equals(order.getUserid(),user.getUid())){
                        itemList.add(order);
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myAdapter = new OrderAdapter(itemList);
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);
    }
}