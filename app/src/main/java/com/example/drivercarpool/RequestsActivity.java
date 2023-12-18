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

import java.util.ArrayList;

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
                            itemList.add(request);
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
            public void onAcceptClick(int position) {
                String orderId = itemList.get(position).getOrderid();
                firebaseDB.updateOrderStateAccept(orderId);
            }

            @Override
            public void onDeclineClick(int position) {
                String orderId = itemList.get(position).getOrderid();
                firebaseDB.updateOrderStateDeclined(orderId);
            }
        });
    }
}