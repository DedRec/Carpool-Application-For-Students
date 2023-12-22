package com.example.drivercarpool.model;

import com.example.drivercarpool.helpers.Helper;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UserRepository {

    private UserDao mUserDao;
    private LiveData<List<User>> mAllUsers;
    private Context mContext;
    private FirebaseDB firebaseDB;

    // Note that in order to unit test the UserRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public UserRepository(Application application) {
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mAllUsers = mUserDao.getAllUsers();
        mContext = application.getApplicationContext();
        firebaseDB = FirebaseDB.getInstance();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<User>> getAllUsers() {
        /*if (isNetworkAvailable()) {
            // Fetch data from the network
            fetchDataFromNetwork();
        }*/
        return mAllUsers;
    }

    private void fetchDataFromNetwork() {
        // TODO: Implement network data fetching logic
        // If network fetch is successful, update Room Database
        // Use an API, Retrofit, or other network libraries
        // Example: ApiService.fetchUsers(callback);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
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
            Log.d("userRepo","Fetching user from firebase");
            firebaseDB.getUser(userId, new FirebaseDB.DataCallback<Helper>() {
                @Override
                public void onDataLoaded(Helper data) {
                    User user1 = new User(data.getUserid(), data.getEmail(), data.getUsername(), data.getName());
                    Log.d("userRepo","Inserting user to ROOM now.....");
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

    /*private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/
}
