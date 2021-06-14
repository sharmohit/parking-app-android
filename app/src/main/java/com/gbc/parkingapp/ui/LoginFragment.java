/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbc.parkingapp.MainActivity;
import com.gbc.parkingapp.R;
import com.gbc.parkingapp.databinding.FragmentLoginBinding;
import com.gbc.parkingapp.model.User;
import com.gbc.parkingapp.viewmodel.UserViewModel;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment implements View.OnFocusChangeListener {
    private FragmentLoginBinding binding;
    private UserViewModel userViewModel;
    private String email;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.userViewModel = UserViewModel.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentLoginBinding.inflate(inflater, container, false);
        this.binding.editEmail.setOnFocusChangeListener(this::onFocusChange);
        this.binding.editPassword.setOnFocusChangeListener(this::onFocusChange);
        this.binding.btnLogin.setOnClickListener(this::loginClicked);

        this.userViewModel.userLiveData.observe(getViewLifecycleOwner(), new Observer<User>() {
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

        return this.binding.getRoot();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.edit_email:
                    this.binding.labelEmail.setError(null);
                    break;
                case R.id.edit_password:
                    this.binding.labelPassword.setError(null);
                    break;
            }
        }
    }

    private void loginClicked(View view) {

        email = this.binding.editEmail.getText().toString().trim();
        password = this.binding.editPassword.getText().toString().trim();

        Boolean isValidInput = true;

        if (email.isEmpty()) {
            if (this.binding.labelEmail.getError() == null) {
                this.binding.labelEmail.setError("Email is required");
            }
            isValidInput = false;
        } else if (!this.isValidEmail(email)) {
            if (this.binding.labelEmail.getError() == null) {
                this.binding.labelEmail.setError("Incorrect email");
            }
            isValidInput = false;
        }
        if (password.isEmpty()) {
            if (this.binding.labelPassword.getError() == null) {
                this.binding.labelPassword.setError("Password is required");
            }
            isValidInput = false;
        }
        if (!isValidInput) {
            this.binding.layoutLogin.clearFocus();
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
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigateUp();
        navController.navigate(R.id.action_loginFragment_to_home_fragment);
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void restoreLogin() {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(
                this.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        this.email = sharedPref.getString(this.getString(R.string.email_key), "");
        if (!this.email.isEmpty()) {
            this.password = sharedPref.getString(this.getString(R.string.password_key), "");
            this.userViewModel.getUserByEmail(this.email);
        }
    }

    private void storeLogin(String email, String password) {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(
                this.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor loginEditor = sharedPref.edit();
        loginEditor.putString(this.getString(R.string.email_key), email);
        loginEditor.putString(this.getString(R.string.password_key), password);
        loginEditor.apply();
    }
}