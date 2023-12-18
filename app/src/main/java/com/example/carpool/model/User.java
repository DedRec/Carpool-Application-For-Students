package com.example.carpool.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    public String userId;
    @NonNull
    public String email;
    @NonNull
    public String username;
    @NonNull
    public String name;
    @NonNull
    public String password;
}
