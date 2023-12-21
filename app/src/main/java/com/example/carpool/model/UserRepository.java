package com.example.carpool.model;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.carpool.helpers.HelperUser;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UserRepository {

    private UserDao mUserDao;
    private LiveData<List<User>> mAllUsers;
    private Context mContext;
    private FirebaseDB firebaseDB;

    public UserRepository(Application application) {
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mAllUsers = mUserDao.getAllUsers();
        mContext = application.getApplicationContext();
        firebaseDB = FirebaseDB.getInstance();
    }

    public LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }

    public void insertUser(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insertUser(user);
        });
    }
    public User getUserById(String userId) {
        Future<User> future = UserRoomDatabase.databaseWriteExecutor.submit(() -> {
            return mUserDao.getUserById(userId);
        });

        try {
            User user = future.get();
            return user;
        } catch (InterruptedException | ExecutionException e) {
            // Handle exceptions as needed
            e.printStackTrace();
            return null;
        }
    }
    public void checkUserExistsInLocalDB(String userId){
        User user = getUserById(userId);
        if(user == null){
            firebaseDB.getUser(userId, new FirebaseDB.DataCallback<HelperUser>() {
                @Override
                public void onDataLoaded(HelperUser data) {
                    User user1 = new User(data.getUserid(), data.getEmail(), data.getUsername(), data.getName());
                    insertUser(user1);
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        }
    }
    public void updateUser(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.update(user);
        });
    }
}
