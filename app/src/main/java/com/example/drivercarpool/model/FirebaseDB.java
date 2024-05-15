package com.example.drivercarpool.model;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.drivercarpool.helpers.Helper;
import com.example.drivercarpool.helpers.HelperTrip;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
        Query checkDriverDatabase = driverReference.orderByChild("username").equalTo(username);
        checkDriverDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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

                                            Helper helper = new Helper(name, email, username, password, user.getUid());
                                            driverReference.child(user.getUid()).setValue(helper, new DatabaseReference.CompletionListener() {
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
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
        checkDriverDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Helper helper = new Helper(user.getDisplayName(), user.getEmail(),username[0],password[0],user.getUid());
                    driverReference.child(user.getUid()).setValue(helper);
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
    public void getUser(String userId, DataCallback<Helper> callback) {
        driverReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Helper user = snapshot.getValue(Helper.class);
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
        Query checkDriverDatabase = driverReference.orderByChild("userid").equalTo(uid);
        checkDriverDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void updateTripDestination(String tripId, String destination){
        tripsReference.child(tripId).child("to").setValue(destination);

    }
    public void updateTripSource(String tripId, String source){
        tripsReference.child(tripId).child("from").setValue(source);

    }
    public void updateTripTime(String tripId, String time){
        tripsReference.child(tripId).child("time").setValue(time);

    }
    public void updateTripCarPlate(String tripId, String carPlate){
        tripsReference.child(tripId).child("carPlate").setValue(carPlate);

    }
    public void updateTripPassengerNumber(String tripId, String passengers){
        tripsReference.child(tripId).child("passengers_number").setValue(passengers);
    }
    private void updateOrderState(String orderId, String state) {
        if (TextUtils.equals(state, "Started")) {
            updateOrderStateStarted(orderId);
        } else if (TextUtils.equals(state, "Completed")) {
            updateOrderStateCompleted(orderId);
        } else if (TextUtils.equals(state, "Canceled")) {
            updateOrderStateCanceled(orderId);
        }
    }
    public void updateOrderStateConfirm(String orderId){
        orderReference.child(orderId).child("orderState").setValue("Confirmed");
    }
    public void updateOrderStateStarted(String orderId){
        orderReference.child(orderId).child("orderState").setValue("Started");
    }
    public void updateOrderStateCompleted(String orderId){
        orderReference.child(orderId).child("orderState").setValue("Completed");
    }
    public void updateOrderStateCanceled(String orderId){
        orderReference.child(orderId).child("orderState").setValue("Canceled");
    }
    public void updateOrderStateNotConfirmed(String orderId){
        orderReference.child(orderId).child("orderState").setValue("Not Confirmed");
    }
    public void updateTripStateStarted(String tripId){
        tripsReference.child(tripId).child("tripState").setValue("Started");
    }
    public void updateTripStateCompleted(String tripId){
        tripsReference.child(tripId).child("tripState").setValue("Completed");
    }
    public void updateTripStateCanceled(String tripId){
        tripsReference.child(tripId).child("tripState").setValue("Canceled");
    }
    public void insertTrip(String tripId, HelperTrip trip){
        tripsReference.child(tripId).setValue(trip);
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
    public void updateTripOrders(String tripId, String state){
        getTripOrders(tripId, new DataCallback<ArrayList<String>>() {
            @Override
            public void onDataLoaded(ArrayList<String> data) {
                for (String orderId : data) {
                    updateOrderState(orderId,state);
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
    public void checkIfTimeSlotAvailable(String time, String date, String userId, DataCallback<Boolean> callback) {
        Query checkTripsWithThisTime = tripsReference.orderByChild("time").equalTo(time);
        checkTripsWithThisTime.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isTimeSlotAvailable = true;

                for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                    HelperTrip trip = tripSnapshot.getValue(HelperTrip.class);

                    if (trip != null && TextUtils.equals(trip.getDriverId(), userId) &&
                            TextUtils.equals(trip.getDate(), date) &&
                            TextUtils.equals(trip.getTime(), time) &&
                            !TextUtils.equals(trip.getTripState(),"Canceled")) {
                        isTimeSlotAvailable = false;
                        break;  // No need to continue checking, we found a conflicting time slot
                    }
                }

                callback.onDataLoaded(isTimeSlotAvailable);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }
    public void updateTripRequestNotConfirmed(String tripId){
        getTripOrders(tripId, new DataCallback<ArrayList<String>>() {
            @Override
            public void onDataLoaded(ArrayList<String> data) {
                for (String orderId : data) {
                    updateOrderStateNotConfirmed(orderId);
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

}