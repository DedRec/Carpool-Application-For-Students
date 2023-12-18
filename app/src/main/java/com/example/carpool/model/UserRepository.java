package com.example.carpool.model;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {

    private UserDao mUserDao;
    private LiveData<List<User>> mAllUsers;
    private Context mContext;

    // Note that in order to unit test the UserRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public UserRepository(Application application) {
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mAllUsers = mUserDao.getAllUsers();
        mContext = application.getApplicationContext();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<User>> getAllUsers() {
        if (isNetworkAvailable()) {
            // Fetch data from the network
            fetchDataFromNetwork();
        }
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

    public LiveData<User> getUserById(String userId) {
        return mUserDao.getUserById(userId);
    }

    public void updateUser(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.update(user);
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
