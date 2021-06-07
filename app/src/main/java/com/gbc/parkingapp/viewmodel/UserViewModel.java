// Group: Project Groups 12
// Name: Mohit Sharma
// Student ID: 101342267
// Group Member: Javtesh Singh Bhullar
// Member ID: 101348129

package com.gbc.parkingapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gbc.parkingapp.model.User;
import com.gbc.parkingapp.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private static UserViewModel instance;
    private final UserRepository userRepository = new UserRepository();
    public MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public static UserViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new UserViewModel(application);
        }

        return instance;
    }

    public UserViewModel(Application application) {
        super(application);
    }

    public void getUserByEmail(String email) {
        this.userRepository.getUserByEmail(email, this.userLiveData);
    }
}
