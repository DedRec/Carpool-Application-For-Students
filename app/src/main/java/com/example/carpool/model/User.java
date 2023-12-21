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

    public User(@NonNull String userId, @NonNull String email, @NonNull String username, @NonNull String name) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.name = name;
    }
}
