package com.example.drivercarpool;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.drivercarpool.model.FirebaseDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Button signoutBtn,checkRequestsBtn;
    private TextView nameText,usernameText,emailText;
    private FirebaseAuth mAuth;
    private FirebaseDB firebaseDB;

    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseDB = FirebaseDB.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        nameText = view.findViewById(R.id.name_view);
        emailText = view.findViewById(R.id.email_view);
        usernameText = view.findViewById(R.id.username_view);
        signoutBtn = view.findViewById(R.id.signout_btn);
        checkRequestsBtn = view.findViewById(R.id.check_requested_trips_btn);

        nameText.setText(user.getDisplayName());
        emailText.setText(user.getEmail());
        firebaseDB.getUsername(user.getUid(), new FirebaseDB.DataCallback<String>() {
            @Override
            public void onDataLoaded(String data) {
                usernameText.setText(data);
            }

            @Override
            public void onError(String errorMessage) {
                usernameText.setText("404 Username Not Found");
            }
        });

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);

                getActivity().finish();
            }
        });

        checkRequestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReq = new Intent(getContext(),RequestsActivity.class);
                startActivity(intentReq);
            }
        });
        return view;
    }
}