package com.example.drivercarpool;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class HomeFragment extends Fragment {
    private RecyclerView myRecyclerView;
    private ItemAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    Button addBtn;
    ArrayList<TripItem> itemList;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        reference = FirebaseDatabase.getInstance().getReference("trips");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        addBtn = view.findViewById(R.id.btnAddTrip);

        createItemList();
        buildRecyclerView(view);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddTripActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    public void createItemList(){
        itemList = new ArrayList<>();
    }
    public void buildRecyclerView(View view){
        myRecyclerView = view.findViewById(R.id.recycler_view_trips);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(getContext());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    TripItem trip = dataSnapshot.getValue(TripItem.class);
                    if(TextUtils.equals(trip.getDriverId(),user.getUid())){
                        /*if(TextUtils.equals(trip.)){

                        }*/
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
            public void onEditClick(int position) {
                String driverId = itemList.get(position).getDriverId();
                String source = itemList.get(position).getFrom();
                String destination = itemList.get(position).getTo();
                String carPlate = itemList.get(position).getCarPlate();
                String passengers = itemList.get(position).getPassengers_number();
                String time = itemList.get(position).getTime();
                String tripid = itemList.get(position).getTripid();
                Intent intent = new Intent(getContext(),EditActivity.class);
                intent.putExtra("tripid",tripid);
                intent.putExtra("source",source);
                intent.putExtra("destination",destination);
                intent.putExtra("carPlate",carPlate);
                intent.putExtra("passengers",passengers);
                Log.d("trip",passengers);
                intent.putExtra("time",time);
                startActivity(intent);
            }
        });
    }
}