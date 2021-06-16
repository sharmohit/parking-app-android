package com.gbc.parkingapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbc.parkingapp.R;
import com.gbc.parkingapp.databinding.FragmentProfileBinding;
import com.gbc.parkingapp.databinding.FragmentSignUpBinding;
import com.gbc.parkingapp.model.User;
import com.gbc.parkingapp.viewmodel.UserViewModel;


public class SignUpFragment extends Fragment {


    private final String TAG = this.getClass().getCanonicalName();
    private FragmentSignUpBinding binding;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentSignUpBinding.inflate(inflater,container,false);

        name = this.binding.editName.getText().toString();
        email = this.binding.editEmail.getText().toString();
        password = this.binding.editPassword.getText().toString();
        phone = this.binding.editPhone.getText().toString();
        car_plate_number = this.binding.editCarPlateNumber.getText().toString();

        return this.binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    btnSignUpClicked();
                }
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

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void btnSignUpClicked(){


        name = this.binding.editName.getText().toString();
        email = this.binding.editEmail.getText().toString();
        password = this.binding.editPassword.getText().toString();
        phone = this.binding.editPhone.getText().toString();
        car_plate_number = this.binding.editCarPlateNumber.getText().toString();


        Boolean isValidInput = true;

        if (email.isEmpty()) {
            this.binding.editEmail.setError("Email is required");
            isValidInput = false;
        } else if (!this.isValidEmail(email)) {
            this.binding.editEmail.setError("Incorrect email");
            isValidInput = false;
        }
        if (name.isEmpty()) {
            this.binding.editName.setError("Name is required");
            isValidInput = false;
        }
        if (password.isEmpty()) {
            this.binding.editPassword.setError("Password is required");
            isValidInput = false;
        }
        if (phone.isEmpty()) {
            this.binding.editPhone.setError("Phone is required");
            isValidInput = false;
        }
        if (car_plate_number.isEmpty()) {
            this.binding.editCarPlateNumber.setError("Car Plate Number is required");
            isValidInput = false;
        }
        if (car_plate_number.length()<2) {
            this.binding.editCarPlateNumber.setError("Minimum 2 alphanumerics characters allowed");
            isValidInput = false;
        }
        if (car_plate_number.length() > 8) {
            this.binding.editCarPlateNumber.setError("Maximum 8 alphanumerics characters allowed");
            isValidInput = false;
        }

        if (!isValidInput) {
            return;
        }

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

    private void showHomeScreen() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_signUpFragment_to_home_fragment);
    }
}