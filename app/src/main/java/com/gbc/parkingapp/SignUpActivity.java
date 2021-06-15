package com.gbc.parkingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.gbc.parkingapp.databinding.ActivitySignUpBinding;
import com.gbc.parkingapp.model.User;
import com.gbc.parkingapp.viewmodel.UserViewModel;

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getCanonicalName();

    private ActivitySignUpBinding binding;

    private UserViewModel userViewModel;
    private User user;

    private String name;
    private String email;
    private String password;
    private String phone;
    private String car_plate_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivitySignUpBinding.inflate(this.getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.userViewModel = UserViewModel.getInstance();
        //this.user = User.getInstance();

        this.binding.btnSignup.setOnClickListener(this::signUpClicked);
        this.binding.btnLogin.setOnClickListener(this::loginClicked);

        this.userViewModel.userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "onChanged: Nmae of user is : " + user.getName());
            }
        });

    }

    public void signUpClicked(View view){

        name = this.binding.editName.getText().toString();
        email = this.binding.editEmail.getText().toString();
        password = this.binding.editPassword.getText().toString();
        phone = this.binding.editMobileNo.getText().toString();
        car_plate_number = this.binding.editCarPlateNo.getText().toString();


        Boolean isValidInput = true;

        if (email.isEmpty()) {
            this.binding.editEmail.setError("Email is required");
            isValidInput = false;
        } else if (!this.isValidEmail(email)) {
            this.binding.editEmail.setError("Incorrect email");
            isValidInput = false;
        }
        if (password.isEmpty()) {
            this.binding.editPassword.setError("Password is required");
            isValidInput = false;
        }
        if (!isValidInput) {
            return;
        }

        // Store values in User Singleton
       // storeValuesInUser();

        // Store values in FireStore database

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setCar_plate_number(car_plate_number);

        this.userViewModel.signUpUser(user);
        userViewModel.userLiveData.postValue(user);
        showHomeScreen();

    }

    public void storeValuesInUser(){

        userViewModel.userLiveData.postValue(user);

        userViewModel.userLiveData.getValue().setName(name);
        userViewModel.userLiveData.getValue().setEmail(email);
        userViewModel.userLiveData.getValue().setPassword(password);
        userViewModel.userLiveData.getValue().setPhone(phone);
        userViewModel.userLiveData.getValue().setCar_plate_number(car_plate_number);
    }

    public void loginClicked(View view){
        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        //startActivity(intent);
    }

    private void showHomeScreen() {
        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //startActivity(intent);
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}