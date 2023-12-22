package com.example.carpool.model;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.carpool.helpers.HelperOrder;
import com.example.carpool.helpers.HelperTrip;
import com.example.carpool.helpers.HelperUser;
import com.example.carpool.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class FirebaseDB {
    private FirebaseDatabase database;
    private DatabaseReference userReference, driverReference, tripsReference, orderReference;
    private static FirebaseDB instance;

    private FirebaseDB() {
        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("users");
        driverReference = database.getReference("drivers");
        tripsReference = database.getReference("trips");
        orderReference = FirebaseDatabase.getInstance().getReference("orders");

    }

    public static synchronized FirebaseDB getInstance() {
        if (instance == null) {
            instance = new FirebaseDB();
        }
        return instance;
    }

    public DatabaseReference getUserReference() {
        return userReference;
    }

    public DatabaseReference getDriverReference() {
        return driverReference;
    }

    public DatabaseReference getTripsReference() {
        return tripsReference;
    }

    public DatabaseReference getOrderReference() {
        return orderReference;
    }

    public interface SignUpCallback {
        void onSignUpSuccess();
        void onSignUpFailure(String errorMessage);
    }

    public void signUp(String name, String email, String username, String password, FirebaseAuth mAuth, Context context, SignUpCallback callback) {
        Query checkUserDatabase = userReference.orderByChild("username").equalTo(username);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // User already exists
                    callback.onSignUpFailure("Username is already taken.");
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    try {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name)
                                                    .build();
                                            user.updateProfile(profileUpdates);

                                            // Handle successful authentication
                                            Toast.makeText(context, "Account created successfully.", Toast.LENGTH_SHORT).show();

                                            HelperUser helper = new HelperUser(name, email, username, password, user.getUid());
                                            userReference.child(user.getUid()).setValue(helper, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                    if (error == null) {
                                                        // Successfully saved user data
                                                        callback.onSignUpSuccess();
                                                    } else {
                                                        // Handle error when saving user data
                                                        callback.onSignUpFailure("Error saving user data.");
                                                    }
                                                }
                                            });
                                        } else {
                                            // Handle authentication failure
                                            callback.onSignUpFailure("Authentication failed: " + task.getException().getMessage());
                                        }
                                    } catch (IllegalArgumentException e) {
                                        // Handle invalid email or password
                                        callback.onSignUpFailure("Invalid email or password.");
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                callback.onSignUpFailure("Database error.");
            }
        });
    }

    public void checkUser(FirebaseAuth mAuth){
        FirebaseUser user = mAuth.getCurrentUser();
        final String[] username = new String[1];
        final String[] password = new String[1];
        Query checkDriverDatabase = driverReference.orderByChild("userid").equalTo(user.getUid());
        Query checkUserDatabase = userReference.orderByChild("userid").equalTo(user.getUid());
        checkDriverDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    username[0] =  snapshot.child(user.getUid()).child("username").getValue(String.class);
                    password[0] = snapshot.child(user.getUid()).child("password").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    HelperUser helper = new HelperUser(user.getDisplayName(), user.getEmail(),username[0],password[0],user.getUid());
                    userReference.child(user.getUid()).setValue(helper);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public interface DataCallback<T> {
        void onDataLoaded(T data);
        void onError(String errorMessage);
    }
    public void getUser(String userId, DataCallback<HelperUser> callback) {
        userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HelperUser user = snapshot.getValue(HelperUser.class);
                    callback.onDataLoaded(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }
    public void getUsername(String uid, DataCallback<String> callback) {
        Query checkUserDatabase = userReference.orderByChild("userid").equalTo(uid);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username;
                if (snapshot.exists()) {
                    username = snapshot.child(uid).child("username").getValue(String.class);
                } else {
                    username = "USERNAME NOT FOUND";
                }

                // Invoke the callback with the result
                callback.onDataLoaded(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
                callback.onError(error.getMessage());
            }
        });
    }
    public void getDriverName(String driverId, DataCallback<String> callback) {
        Query checkUserDatabase = driverReference.orderByChild("userid").equalTo(driverId);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String driverName = snapshot.child(driverId).child("name").getValue(String.class);
                    callback.onDataLoaded(driverName);
                } else {
                    callback.onError("Driver not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void getTrip(String tripId, DataCallback<HelperTrip> callback) {
        Query checkTripDatabase = tripsReference.orderByChild("tripid").equalTo(tripId);
        checkTripDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HelperTrip trip = snapshot.child(tripId).getValue(HelperTrip.class);
                    callback.onDataLoaded(trip);
                } else {
                    callback.onError("Trip not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void getTripOrders(String tripId, DataCallback<ArrayList<String>> callback) {
        Query checkTripDatabase = tripsReference.orderByChild("tripid").equalTo(tripId);
        checkTripDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HelperTrip trip = snapshot.child(tripId).child("").getValue(HelperTrip.class);

                    if (trip != null && trip.getOrders() != null) {
                        callback.onDataLoaded(new ArrayList<>(trip.getOrders()));
                    } else {
                        callback.onError("Orders not found for the trip");
                    }
                } else {
                    callback.onError("Trip not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }
    public void updateTripOrders(String tripId, ArrayList<String> orders){
        tripsReference.child(tripId).child("orders").setValue(orders);
    }
    public void updateTripPassengerNumber(String tripId, String passengers){
        tripsReference.child(tripId).child("passengers_number").setValue(passengers);
    }
    public void insertOrderDate(String orderId, String date) {
        orderReference.child(orderId).child("tripDate").setValue(date);
    }
    public void insertOrder(String orderId, HelperOrder order){
        orderReference.child(orderId).setValue(order);
    }

}
