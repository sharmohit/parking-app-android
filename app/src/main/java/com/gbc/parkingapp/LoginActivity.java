// Group: Project Groups 12
// Name: Mohit Sharma
// Student ID: 101342267
// Group Member: Javtesh Singh Bhullar
// Member ID: 101348129

package com.gbc.parkingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.gbc.parkingapp.databinding.ActivityLoginBinding;
import com.gbc.parkingapp.model.User;
import com.gbc.parkingapp.viewmodel.UserViewModel;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getCanonicalName();
    private ActivityLoginBinding binding;
    private UserViewModel userViewModel;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityLoginBinding.inflate(this.getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.userViewModel = UserViewModel.getInstance();

        this.binding.btnLogin.setOnClickListener(this::loginClicked);

        this.binding.btnSignUp.setOnClickListener(this::signUpClicked);

        this.userViewModel.userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    if (!user.getId().isEmpty()) {
                        authenticateUser(user.getEmail(), user.getPassword());
                    } else {
                        showSnackBar("User not found!");
                    }
                } else {
                    showSnackBar("Something went wrong");
                }
            }
        });

        restoreLogin();
    }

    private void loginClicked(View view) {

        email = this.binding.editEmail.getText().toString().trim();
        password = this.binding.editPassword.getText().toString().trim();

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

        this.userViewModel.getUserByEmail(this.email);
    }

    private void authenticateUser(String userEmail, String userPassword) {
        if (this.email.contentEquals(userEmail)
                && this.password.contentEquals(userPassword)) {

            if (this.binding.cbRememberMe.isChecked()) {
                this.storeLogin(this.email, this.password);
            }
            showHomeScreen();
        } else {
            showSnackBar("Wrong email or password. Try again");
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(this.binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void showHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void restoreLogin() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                this.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        this.email = sharedPref.getString(this.getString(R.string.email_key), "");
        if (!this.email.isEmpty()) {
            this.password = sharedPref.getString(this.getString(R.string.password_key), "");
            this.userViewModel.getUserByEmail(this.email);
        }
    }

    private void storeLogin(String email, String password) {
        SharedPreferences sharedPref = this.getSharedPreferences(
                this.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor loginEditor = sharedPref.edit();
        loginEditor.putString(this.getString(R.string.email_key), email);
        loginEditor.putString(this.getString(R.string.password_key), password);
        loginEditor.apply();
    }

    public void signUpClicked(View view){

        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }
}