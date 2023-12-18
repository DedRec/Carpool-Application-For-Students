package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.example.carpool.helpers.HelperUser;
import com.example.carpool.model.FirebaseDB;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseDB = FirebaseDB.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        nameText = findViewById(R.id.name_view);
        emailText = findViewById(R.id.email_view);
        usernameText = findViewById(R.id.username_view);

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
    }
}