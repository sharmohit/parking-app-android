package com.gbc.parkingapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Patterns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbc.parkingapp.R;
import com.gbc.parkingapp.databinding.FragmentProfileBinding;
import com.gbc.parkingapp.model.Parking;
import com.gbc.parkingapp.model.User;
import com.gbc.parkingapp.viewmodel.ParkingViewModel;
import com.gbc.parkingapp.viewmodel.UserViewModel;
import com.google.api.LogDescriptor;

public class ProfileFragment extends Fragment {
    private final String TAG = this.getClass().getCanonicalName();
    private FragmentProfileBinding binding;
    private ParkingViewModel parkingViewModel;
    private UserViewModel userViewModel;
    private User user;

    private String name ;
    private String email ;
    private String password ;
    private String phone ;
    private String car_plate_number ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userViewModel = UserViewModel.getInstance();
        this.parkingViewModel = ParkingViewModel.getInstance();
        this.user = userViewModel.userLiveData.getValue();
        this.userViewModel.userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    if (user.getId().isEmpty()) {
                        Log.d(TAG, "onChanged: Unable to update user");
                    } else {
                        Log.d(TAG, "onChanged: User updated successfully " + user.toString());
                    }
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentProfileBinding.inflate(inflater,container,false);

        name = this.userViewModel.userLiveData.getValue().getName();
        email = this.userViewModel.userLiveData.getValue().getEmail();
        password = this.userViewModel.userLiveData.getValue().getPassword();
        phone = this.userViewModel.userLiveData.getValue().getPhone();
        car_plate_number = this.userViewModel.userLiveData.getValue().getCar_plate_number();

        this.binding.editName.setText(this.user.getName());
        this.binding.editEmail.setText(this.user.getEmail());
        this.binding.editPassword.setText(this.user.getPassword());
        this.binding.editPhone.setText(this.user.getPhone());
        this.binding.editCarPlateNumber.setText(this.user.getCar_plate_number());

        return this.binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    btnUpdateClicked();
                }
            }
        });

        this.binding.btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    btnLogoutClicked();
            }
        });

        this.binding.btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


             AlertDialog.Builder alert =   new AlertDialog.Builder(getContext());
                        alert.setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                btnDeleteClicked();
                                moveToLoginScreen();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });



        binding.editName.setOnFocusChangeListener(this::onFocusChange);
        binding.editEmail.setOnFocusChangeListener(this::onFocusChange);
        binding.editPassword.setOnFocusChangeListener(this::onFocusChange);
        binding.editPhone.setOnFocusChangeListener(this::onFocusChange);
        binding.editCarPlateNumber.setOnFocusChangeListener(this::onFocusChange);
    }


    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.edit_name:
                    this.binding.labelName.setError(null);
                    break;
                case R.id.edit_email:
                    this.binding.labelEmail.setError(null);
                    break;
                case R.id.edit_password:
                    this.binding.labelPassword.setError(null);
                    break;
                case R.id.edit_phone:
                    this.binding.labelPhone.setError(null);
                    break;
                case R.id.edit_car_plate_number:
                    this.binding.labelCarPlateNumber.setError(null);
                    break;
            }
        }
    }


    private Boolean validateInput() {
        boolean isValid = true;

        if (this.binding.editName.getText().toString().trim().isEmpty()) {
            this.binding.labelName.setError("Name can't be empty");
            isValid = false;
        }
        if (this.binding.editEmail.getText().toString().trim().isEmpty()) {
            this.binding.labelEmail.setError("Email can't be empty");
            isValid = false;
        }

        if (this.binding.editPassword.getText().toString().trim().isEmpty()) {
            this.binding.labelPassword.setError("Password can't be empty");
            isValid = false;
        }
        if (this.binding.editPhone.getText().toString().trim().isEmpty()) {
            this.binding.labelPhone.setError("Phone can't be empty");
            isValid = false;
        }

        if (this.binding.editCarPlateNumber.getText().toString().trim().length() < 2
                || this.binding.editCarPlateNumber.getText().length() > 8) {
            this.binding.editCarPlateNumber.setError("Car plate number must be min 2 and max 8 alphanumeric characters");
            isValid = false;
        }

        return isValid;
    }

    public void btnUpdateClicked (){

        name = this.binding.editName.getText().toString();
        email = this.binding.editEmail.getText().toString();
        password = this.binding.editPassword.getText().toString();
        phone = this.binding.editPhone.getText().toString();
        car_plate_number = this.binding.editCarPlateNumber.getText().toString();

        // Store values in User Singleton
        storeValuesInUser();

        // Store values in FireStore database
        this.user.setName(name);
        this.user.setEmail(email);
        this.user.setPassword(password);
        this.user.setPhone(phone);
        this.user.setCar_plate_number(car_plate_number);
        this.userViewModel.updateUser(this.user);
    }
    public void btnLogoutClicked(){

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(
                this.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor loginEditor = sharedPref.edit();
        loginEditor.putString(this.getString(R.string.email_key), "");
        loginEditor.putString(this.getString(R.string.password_key), "");
        this.userViewModel.userLiveData.setValue(null);
        loginEditor.apply();
        moveToLoginScreen();
    }

    public void btnDeleteClicked(){
        if (!this.parkingViewModel.getParkingListLiveData().getValue().isEmpty())
        {
            for (Parking parking : this.parkingViewModel.getParkingListLiveData().getValue()) {
                this.parkingViewModel.deleteUserParking(this.user.getId(), parking.getId());
            }
        }

            this.userViewModel.deleteUser(this.user.getId());
        btnLogoutClicked();
    }

    private void moveToLoginScreen(){
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_profile_fragment_to_loginFragment);
    }

    public void storeValuesInUser(){
        userViewModel.userLiveData.getValue().setName(name);
        userViewModel.userLiveData.getValue().setEmail(email);
        userViewModel.userLiveData.getValue().setPassword(password);
        userViewModel.userLiveData.getValue().setPhone(phone);
        userViewModel.userLiveData.getValue().setCar_plate_number(car_plate_number);
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}