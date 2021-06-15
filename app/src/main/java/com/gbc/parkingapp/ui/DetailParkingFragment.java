package com.gbc.parkingapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.firestore.GeoPoint;

import java.text.DateFormat;


public class DetailParkingFragment extends Fragment {

    private final String TAG = this.getClass().getCanonicalName();
    private FragmentDetailParkingBinding binding;

    private ParkingViewModel parkingViewModel;
    private Parking parking;

    private String buildingCode ;
    private String ParkingHours ;
    private String SuitNumber ;
    private String StreetAddress ;
    private GeoPoint ParkingCoordinates ;
    private String DateAndTime;


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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.parking != null) {
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

        this.binding.editDateAndTime.setEnabled(false);

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
            this.binding.tvName.setError("Name can't be empty");
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

        return isValid;
    }

    public void btnUpdateClicked(){

    }


    public void btnGetDirectionClicked(){

        DetailParkingFragmentDirections.ActionDetailParkingFragmentToMapFragment action = DetailParkingFragmentDirections.actionDetailParkingFragmentToMapFragment();
        action.setParking(this.parking);
        NavHostFragment.findNavController(this).navigate(action);

    }
}