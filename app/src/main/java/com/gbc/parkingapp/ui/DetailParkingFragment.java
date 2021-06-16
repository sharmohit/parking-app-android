package com.gbc.parkingapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbc.parkingapp.R;
import com.gbc.parkingapp.databinding.FragmentDetailParkingBinding;
import com.gbc.parkingapp.databinding.FragmentProfileBinding;
import com.gbc.parkingapp.model.Parking;
import com.gbc.parkingapp.viewmodel.ParkingViewModel;
import com.gbc.parkingapp.viewmodel.UserViewModel;
import com.google.firebase.firestore.GeoPoint;

import java.text.DateFormat;


public class DetailParkingFragment extends Fragment {

    private final String TAG = this.getClass().getCanonicalName();
    private FragmentDetailParkingBinding binding;

    private ParkingViewModel parkingViewModel;
    private Parking parking;

    private String carPlateNumber;
    private String buildingCode ;
    private String parkingHours ;
    private String suitNumber ;
    private String streetAddress ;
    private GeoPoint parkingCoordinates ;
    private String dateAndTime;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      this.parkingViewModel = ParkingViewModel.getInstance();

        this.parking = MapFragmentArgs.fromBundle(getArguments()).getParking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding =  FragmentDetailParkingBinding.inflate(inflater, container, false);
        Parking parking = MapFragmentArgs.fromBundle(getArguments()).getParking();

        if (parking != null) {
            Log.d(TAG, "onCreateView: parking is : " + parking.toString());
        }



        return  this.binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.parkingViewModel.getNewParkingLiveData().observe(getViewLifecycleOwner(), new Observer<Parking>() {
            @Override
            public void onChanged(Parking parking) {
                if (parking != null) {
                    if (parking.getId().isEmpty()) {
                        Log.d(TAG, "onChanged: Unable to update parking");
                    } else {
                        Log.d(TAG, "onChanged: Parking updated successfully " + parking.toString());
                        for (int i = 0; i < parkingViewModel.getParkingListLiveData().getValue().size(); i++)
                        {
                            if (parkingViewModel.getParkingListLiveData().getValue().get(i).getId().contentEquals(parking.getId()))
                            {
                                Log.d(TAG, "onEvent: Updated Existing Parking");
                                parkingViewModel.getParkingListLiveData().getValue().set(i, parking);
                            }
                        }

                    }
                }
            }
        });

        if (this.parking != null) {
            this.binding.editCarPlateNumber.setText(this.parking.getCar_plate_number());
            this.binding.editBuildingCode.setText(this.parking.getBuilding_code());
            this.binding.editParkingHours.setText(String.valueOf(this.parking.getParking_hours()));
            this.binding.editSuitNumber.setText(this.parking.getSuit_number());
            this.binding.editStreetAddress.setText(this.parking.getStreet_address());
            this.binding.editParkingCoordinateLatitude.setText(String.valueOf(this.parking.getCoordinate().getLatitude()));
            this.binding.editParkingCoordinateLongitude.setText(String.valueOf(this.parking.getCoordinate().getLongitude()));
            this.binding.editDateAndTime.setText(DateFormat.getDateInstance(DateFormat.FULL)
                    .format(parking.getDate_time().toDate()) + " at " + DateFormat.getTimeInstance(DateFormat.SHORT)
                    .format(parking.getDate_time().toDate()));

        }

        this.binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    btnUpdateClicked();
                }
            }
        });

        this.binding.btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    btnGetDirectionClicked();
            }
        });

        this.binding.btnDeleteCarParking.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert =   new AlertDialog.Builder(getContext());
                alert.setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                deleteParking();

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

        this.binding.editDateAndTime.setEnabled(false);

        binding.editCarPlateNumber.setOnFocusChangeListener(this::onFocusChange);
        binding.editBuildingCode.setOnFocusChangeListener(this::onFocusChange);
        binding.editParkingHours.setOnFocusChangeListener(this::onFocusChange);
        binding.editSuitNumber.setOnFocusChangeListener(this::onFocusChange);
        binding.editStreetAddress.setOnFocusChangeListener(this::onFocusChange);
        binding.editParkingCoordinateLatitude.setOnFocusChangeListener(this::onFocusChange);
        binding.editParkingCoordinateLongitude.setOnFocusChangeListener(this::onFocusChange);
        binding.editDateAndTime.setOnFocusChangeListener(this::onFocusChange);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.edit_building_code:
                    this.binding.editBuildingCode.setError(null);
                    break;
                case R.id.edit_parking_hours:
                    this.binding.editParkingHours.setError(null);
                    break;
                case R.id.edit_suit_number:
                    this.binding.editSuitNumber.setError(null);
                    break;
                case R.id.edit_street_address:
                    this.binding.editStreetAddress.setError(null);
                    break;
                case R.id.edit_parking_coordinate_latitude:
                    this.binding.editParkingCoordinateLatitude.setError(null);
                    break;
                case R.id.edit_parking_coordinate_longitude:
                    this.binding.editParkingCoordinateLongitude.setError(null);
                    break;
                case R.id.edit_date_and_time:
                    this.binding.editDateAndTime.setError(null);
                    break;
            }
        }
    }

    private Boolean validateInput() {
        boolean isValid = true;

        if (this.binding.editBuildingCode.getText().toString().trim().isEmpty()) {
            this.binding.tvName.setError("Building Code can't be empty");
            isValid = false;
        }
        if (this.binding.editBuildingCode.getText().toString().trim().length() != 5
                || !this.checkAlphanumeric(this.binding.editBuildingCode.getText().toString().trim())){
            if (this.binding.editBuildingCode.getError() == null) {
                this.binding.tvName.setError("Building code must have 5 alphanumeric characters");
            }
            isValid = false;
        }
        if (this.binding.editParkingHours.getText().toString().trim().isEmpty()) {
            this.binding.tvParkingHours.setError("Parking Hours can't be empty");
            isValid = false;
        }

        if (this.binding.editSuitNumber.getText().toString().trim().isEmpty()) {
            this.binding.tvSuitNumber.setError("Suit Number can't be empty");
            isValid = false;
        }
        if ((this.binding.editSuitNumber.getText().toString().trim().length() < 2
                || this.binding.editSuitNumber.getText().length() > 5)
                || !this.checkAlphanumeric(this.binding.editSuitNumber.getText().toString().trim())) {
            if (this.binding.editSuitNumber.getError() == null) {
                this.binding.tvSuitNumber.setError("Suit number must be min 2 and max 5 alphanumeric characters");
            }
            isValid = false;
        }
        if (this.binding.editStreetAddress.getText().toString().trim().isEmpty()) {
            this.binding.tvStreetAddress.setError("Street Address can't be empty");
            isValid = false;
        }
        if (this.binding.editParkingCoordinateLatitude.getText().toString().trim().isEmpty()) {
            this.binding.tvParkingCoordinates.setError("Latitude can't be empty");
            isValid = false;
        }
        if (this.binding.editParkingCoordinateLongitude.getText().toString().trim().isEmpty()) {
            this.binding.tvParkingCoordinates.setError("Longitude can't be empty");
            isValid = false;
        }
        if (this.binding.editDateAndTime.getText().toString().trim().isEmpty()) {
            this.binding.tvDateAndTime.setError("Date and Time can't be empty");
            isValid = false;
        }

        if ((this.binding.editCarPlateNumber.getText().toString().trim().length() < 2
                || this.binding.editCarPlateNumber.getText().length() > 8)
                || !this.checkAlphanumeric(this.binding.editCarPlateNumber.getText().toString().trim())) {
            if (this.binding.editCarPlateNumber.getError() == null) {
                this.binding.tvCarPlateNumber.setError("Car plate number must be min 2 and max 8 alphanumeric characters");
            }
            isValid = false;
        }

        return isValid;
    }

    private boolean checkAlphanumeric (String value) {
        try {
            return value.matches("[A-Za-z0-9]+");
        } catch (Exception e) {
            return false;
        }
    }

    public void btnUpdateClicked(){

        buildingCode = this.binding.editBuildingCode.getText().toString();
        parkingHours = this.binding.editParkingHours.getText().toString();
        carPlateNumber = this.binding.editCarPlateNumber.getText().toString();
        suitNumber = this.binding.editSuitNumber.getText().toString();
        streetAddress = this.binding.editStreetAddress.getText().toString();
        parkingCoordinates = new GeoPoint(Double.parseDouble(this.binding.editParkingCoordinateLatitude.getText().toString().trim()),
                Double.parseDouble(this.binding.editParkingCoordinateLongitude.getText().toString().trim()) );
        dateAndTime = this.binding.editDateAndTime.getText().toString();




        this.parking.setCar_plate_number(carPlateNumber);
        this.parking.setBuilding_code(buildingCode);
        this.parking.setParking_hours(Integer.valueOf(parkingHours));
        this.parking.setSuit_number(suitNumber);
        this.parking.setCoordinate(parkingCoordinates);
        this.parking.setStreet_address(streetAddress);

        parkingViewModel.updateUserParking(UserViewModel.getInstance().userLiveData.getValue().getId(), this.parking);
    }


    public void btnGetDirectionClicked(){

        DetailParkingFragmentDirections.ActionDetailParkingFragmentToMapFragment action = DetailParkingFragmentDirections.actionDetailParkingFragmentToMapFragment();
        action.setParking(this.parking);
        NavHostFragment.findNavController(this).navigate(action);

    }

    private void deleteParking() {
        this.parkingViewModel.deleteUserParking(UserViewModel.getInstance().userLiveData.getValue().getId(), this.parking.getId());
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_detailParkingFragment_to_add_parking_fragment);
    }
}