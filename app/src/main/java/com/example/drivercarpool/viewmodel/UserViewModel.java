package com.example.drivercarpool.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.drivercarpool.model.User;
import com.example.drivercarpool.model.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;

    private final LiveData<List<User>> mAllUsers;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository(application);
        mAllUsers = mRepository.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() { return mAllUsers; }
    public LiveData<User> getUserById(String userId) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();

        User user = mRepository.getUserById(userId);
        userLiveData.postValue(user);

        return userLiveData;
    }
    public void checkUserExistsInLocalDB(String userId){
        mRepository.checkUserExistsInLocalDB(userId);
    }
    public void insert(User user) { mRepository.insertUser(user); }
    public void update(User user) { mRepository.updateUser(user); }

}
