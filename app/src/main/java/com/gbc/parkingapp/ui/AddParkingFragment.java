/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gbc.parkingapp.R;
import com.gbc.parkingapp.databinding.FragmentAddParkingBinding;
import com.gbc.parkingapp.model.Parking;
import com.gbc.parkingapp.viewmodel.ParkingViewModel;
import com.gbc.parkingapp.viewmodel.UserViewModel;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.type.Date;

import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.util.List;

public class AddParkingFragment extends Fragment implements View.OnFocusChangeListener, ChipGroup.OnCheckedChangeListener {
    private FragmentAddParkingBinding binding;
    private UserViewModel userViewModel;
    private ParkingViewModel parkingViewModel;
    private int parkingHours;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userViewModel = UserViewModel.getInstance();
        this.parkingViewModel = ParkingViewModel.getInstance();
        this.parkingHours = -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentAddParkingBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.binding.btnAddParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    addParking();
                    Snackbar.make(v, "Parking added successfully.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        this.binding.editBuildingCode.setOnFocusChangeListener(this::onFocusChange);
        this.binding.editCarPlate.setOnFocusChangeListener(this::onFocusChange);
        this.binding.editSuitNumber.setOnFocusChangeListener(this::onFocusChange);
        this.binding.editAddress.setOnFocusChangeListener(this::onFocusChange);
        this.binding.chipGroupHours.setOnCheckedChangeListener(this::onCheckedChanged);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.edit_building_code:
                    this.binding.labelBuildingCode.setError(null);
                    break;
                case R.id.edit_car_plate:
                    this.binding.labelCarPlate.setError(null);
                    break;
                case R.id.edit_suit_number:
                    this.binding.labelSuitNumber.setError(null);
                    break;
                case R.id.edit_address:
                    this.binding.labelAddress.setError(null);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(@NotNull ChipGroup group, int checkedId) {
        switch (group.getCheckedChipId()) {
            case R.id.chip_1:
                this.parkingHours = 1;
                break;
            case R.id.chip_2:
                this.parkingHours = 4;
                break;
            case R.id.chip_3:
                this.parkingHours = 12;
                break;
            case R.id.chip_4:
                this.parkingHours = 24;
                break;
            default:
                break;
        }

        this.binding.tvParkingHours.setError(null);
    }

    private void addParking() {
        Parking parking = new Parking();
        parking.setBuilding_code(this.binding.editBuildingCode.getText().toString().trim());
        parking.setParking_hours(this.parkingHours);
        parking.setCar_plate_number(this.binding.editCarPlate.getText().toString().trim());
        parking.setSuit_number(this.binding.editSuitNumber.getText().toString().trim());
        parking.setStreet_address(this.binding.editAddress.getText().toString().trim());
        parking.setDate_time(Timestamp.now());
        this.parkingViewModel.addUserParking(this.userViewModel.userLiveData.getValue().getId(), parking);
        this.clearAllInput();
    }

    private Boolean validateInput() {
        boolean isValid = true;
        if (this.binding.editBuildingCode.getText().toString().trim().length() != 5) {
            this.binding.labelBuildingCode.setError("Building code must have 5 alphanumeric characters");
            isValid = false;
        }
        if (this.binding.editCarPlate.getText().toString().trim().length() < 2
                || this.binding.editCarPlate.getText().length() > 8) {
            this.binding.labelCarPlate.setError("Car plate number must be min 2 and max 8 alphanumeric characters");
            isValid = false;
        }
        if (this.binding.editSuitNumber.getText().toString().trim().length() < 2
                || this.binding.editSuitNumber.getText().length() > 5) {
            this.binding.labelSuitNumber.setError("Suit number must be min 2 and max 5 alphanumeric characters");
            isValid = false;
        }
        if (this.binding.editAddress.getText().toString().trim().isEmpty()) {
            this.binding.labelAddress.setError("Address can't be empty");
            isValid = false;
        }
        if(this.parkingHours < 0) {
            this.binding.tvParkingHours.setError("");
            isValid =false;
        }

        return isValid;
    }

    private void clearAllInput() {
        this.binding.layoutAddParking.clearFocus();
        this.binding.editBuildingCode.getText().clear();
        this.binding.editCarPlate.getText().clear();
        this.binding.editSuitNumber.getText().clear();
        this.binding.editAddress.getText().clear();
        this.binding.chipGroupHours.clearCheck();
    }
}