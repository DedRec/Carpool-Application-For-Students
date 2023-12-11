package com.example.drivercarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestsActivity extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private RequestAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private FirebaseAuth auth;
    private FirebaseUser user;
    DatabaseReference reference;
    Intent intent;
    ArrayList<RequestItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        reference = FirebaseDatabase.getInstance().getReference("orders");
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

        reference.addValueEventListener(new ValueEventListener() {
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
                String orderid = itemList.get(position).getOrderid();

                DatabaseReference orderReference = reference.child(orderid).child("orderState");
                orderReference.setValue("Accepted");
            }

            @Override
            public void onDeclineClick(int position) {
                String orderid = itemList.get(position).getOrderid();

                DatabaseReference orderReference = reference.child(orderid).child("orderState");
                orderReference.setValue("Declined");
            }
        });
    }
}