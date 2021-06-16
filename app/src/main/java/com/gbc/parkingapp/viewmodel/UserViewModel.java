/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gbc.parkingapp.model.User;
import com.gbc.parkingapp.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private static UserViewModel instance;
    private final UserRepository userRepository = new UserRepository();
    public MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public static UserViewModel getInstance() {
        if (instance == null) {
            instance = new UserViewModel();
        }
        return instance;
    }

    public UserViewModel() {
        super();
    }

    public void getUserByEmail(String email) {
        this.userRepository.getUserByEmail(email, this.userLiveData);
    }
    public void signUpUser(User user) {
        this.userRepository.signUpUser(user,this.userLiveData);
    }
    public void updateUser(User user){
        this.userRepository.updateUser(user, this.userLiveData);
    }

    public void deleteUser(String id){
        this.userRepository.deleteUser(id);
    }
}
