package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.carpool.helpers.HelperUser;
import com.example.carpool.model.FirebaseDB;
import com.example.carpool.model.User;
import com.example.carpool.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    TextView nameText,usernameText,emailText;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDB firebaseDB;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        firebaseDB = FirebaseDB.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        nameText = findViewById(R.id.name_view);
        emailText = findViewById(R.id.email_view);
        usernameText = findViewById(R.id.username_view);

        mUserViewModel.getUserById(user.getUid()).observe(this, userRoom -> {
            if(userRoom!=null) {
                usernameText.setText(userRoom.username);
                nameText.setText(userRoom.name);
                emailText.setText(userRoom.email);
            }
        });
    }
}