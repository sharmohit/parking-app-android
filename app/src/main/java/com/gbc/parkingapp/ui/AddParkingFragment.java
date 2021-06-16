/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp.ui;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbc.parkingapp.R;
import com.gbc.parkingapp.databinding.FragmentAddParkingBinding;
import com.gbc.parkingapp.helper.LocationHelper;
import com.gbc.parkingapp.model.Parking;
import com.gbc.parkingapp.viewmodel.ParkingViewModel;
import com.gbc.parkingapp.viewmodel.UserViewModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddParkingFragment extends Fragment implements View.OnFocusChangeListener, ChipGroup.OnCheckedChangeListener {
    private FragmentAddParkingBinding binding;
    private UserViewModel userViewModel;
    private ParkingViewModel parkingViewModel;
    private LocationHelper locationHelper;
    private Location lastLocation;
    private LocationCallback locationCallback;
    private int parkingHours;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userViewModel = UserViewModel.getInstance();
        this.parkingViewModel = ParkingViewModel.getInstance();
        this.locationHelper = LocationHelper.getInstance();
        this.parkingHours = -1;
        this.parkingViewModel.getNewParkingLiveData().observe(this, new Observer<Parking>() {
            @Override
            public void onChanged(Parking parking) {
                if (parking != null) {
                    if (parking.getId().isEmpty()) {
                        Snackbar.make(getView(), "Unable to add Parking.", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(getView(), "Parking added successfully.", Snackbar.LENGTH_SHORT).show();
                        clearAllInput();
                        parkingViewModel.getParkingListLiveData().getValue().add(0, parking);
                    }
                }
                binding.progressIndicator.setVisibility(View.INVISIBLE);
            }
        });
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

        this.binding.progressIndicator.setVisibility(View.INVISIBLE);
        this.binding.btnAddParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    fetchParkingLocation();
                }
                binding.layoutAddParking.clearFocus();
            }
        });

        this.binding.editBuildingCode.setOnFocusChangeListener(this::onFocusChange);
        this.binding.editCarPlate.setOnFocusChangeListener(this::onFocusChange);
        this.binding.editSuitNumber.setOnFocusChangeListener(this::onFocusChange);
        this.binding.editAddress.setOnFocusChangeListener(this::onFocusChange);
        this.binding.chipGroupHours.setOnCheckedChangeListener(this::onCheckedChanged);
        this.binding.labelAddress.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationHelper.checkPermissions(getContext());

                if (locationHelper.isLocationPermissionGranted) {
                    binding.progressIndicator.setVisibility(View.VISIBLE);
                    initiateLocationListener();
                    locationHelper.getLastLocation(getContext()).observe(getViewLifecycleOwner(), new Observer<Location>() {
                        @Override
                        public void onChanged(Location location) {
                            lastLocation = location;
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        this.locationHelper.stopLocationUpdates(getActivity().getBaseContext(), this.locationCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.parkingViewModel.getNewParkingLiveData().removeObservers(this);
        this.parkingViewModel.getNewParkingLiveData().setValue(null);
        this.binding = null;
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

    private void fetchParkingLocation() {
        this.binding.progressIndicator.setVisibility(View.VISIBLE);
        if (this.lastLocation == null)
        {
            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    LatLng latLng = locationHelper.getLocation(getContext(), binding.editAddress
                            .getText().toString().trim());

                    handler.post(() -> {
                        addParking(latLng);
                    });
                }
            });
        } else {
            this.addParking(new LatLng(this.lastLocation.getLatitude(),this.lastLocation.getLongitude()));
        }
    }

    private void addParking(LatLng latLng) {
        if (latLng == null) {
            Snackbar.make(getView(), "Unable to process address.", Snackbar.LENGTH_SHORT).show();
        } else {
            Parking parking = new Parking();
            parking.setBuilding_code(binding.editBuildingCode.getText().toString().trim());
            parking.setParking_hours(parkingHours);
            parking.setCar_plate_number(binding.editCarPlate.getText().toString().trim());
            parking.setSuit_number(binding.editSuitNumber.getText().toString().trim());
            parking.setStreet_address(binding.editAddress.getText().toString().trim());
            parking.setDate_time(Timestamp.now());
            parking.setCoordinate(
                    new GeoPoint(latLng.latitude, latLng.longitude));
            parkingViewModel.addUserParking(userViewModel.userLiveData.getValue().getId(), parking);
        }
    }

    private Boolean validateInput() {
        boolean isValid = true;
        if (this.binding.editBuildingCode.getText().toString().trim().length() != 5
        || !this.checkAlphanumeric(this.binding.editBuildingCode.getText().toString().trim())){
            if (this.binding.editBuildingCode.getError() == null) {
                this.binding.labelBuildingCode.setError("Building code must have 5 alphanumeric characters");
            }
            isValid = false;
        }
        if ((this.binding.editCarPlate.getText().toString().trim().length() < 2
                || this.binding.editCarPlate.getText().length() > 8)
                || !this.checkAlphanumeric(this.binding.editCarPlate.getText().toString().trim())) {
            if (this.binding.editCarPlate.getError() == null) {
                this.binding.labelCarPlate.setError("Car plate number must be min 2 and max 8 alphanumeric characters");
            }
            isValid = false;
        }
        if ((this.binding.editSuitNumber.getText().toString().trim().length() < 2
                || this.binding.editSuitNumber.getText().length() > 5)
                || !this.checkAlphanumeric(this.binding.editSuitNumber.getText().toString().trim())) {
            if (this.binding.editSuitNumber.getError() == null) {
                this.binding.labelSuitNumber.setError("Suit number must be min 2 and max 5 alphanumeric characters");
            }
            isValid = false;
        }
        if (this.binding.editAddress.getText().toString().trim().isEmpty()) {
            if (this.binding.editAddress.getError() == null) {
                this.binding.labelAddress.setError("Address can't be empty");
            }
            isValid = false;
        }
        if (this.parkingHours < 0) {
            if (this.binding.tvParkingHours.getError() == null) {
                this.binding.tvParkingHours.setError("");
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

    private void clearAllInput() {
        this.binding.editBuildingCode.getText().clear();
        this.binding.editCarPlate.getText().clear();
        this.binding.editSuitNumber.getText().clear();
        this.binding.editAddress.getText().clear();
        this.binding.chipGroupHours.clearCheck();
    }

    private void initiateLocationListener() {
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location loc : locationResult.getLocations()) {
                    lastLocation = loc;
                    binding.editAddress.setText(locationHelper.getAddress(getContext(), loc));
                    locationHelper.stopLocationUpdates(getActivity().getBaseContext(), locationCallback);
                }
                binding.progressIndicator.setVisibility(View.INVISIBLE);
            }
        };

        this.locationHelper.requestLocationUpdates(getActivity().getBaseContext(), this.locationCallback);
    }
}