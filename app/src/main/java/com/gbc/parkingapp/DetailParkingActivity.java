package com.gbc.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gbc.parkingapp.databinding.ActivityDertailParkingBinding;
import com.gbc.parkingapp.databinding.ActivitySignUpBinding;
import com.gbc.parkingapp.model.User;
import com.gbc.parkingapp.viewmodel.ParkingViewModel;
import com.gbc.parkingapp.viewmodel.UserViewModel;

public class DetailParkingActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    private String id = this.userViewModel.userLiveData.getValue().getId();

    private final String TAG = this.getClass().getCanonicalName();
    private ActivityDertailParkingBinding binding;

    private ParkingViewModel parkingViewModel;

    private String buildingCode = ParkingViewModel.parkingInstance().getBuilding_code();
    private String parkingHours = ParkingViewModel.parkingInstance().getBuilding_code().toString();
    private String suitNumber = ParkingViewModel.parkingInstance().getSuit_number();
    private String streetAddress = ParkingViewModel.parkingInstance().getStreet_address();
   // private String parkingCoordinates;
   // private String dateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityDertailParkingBinding.inflate(this.getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.parkingViewModel = ParkingViewModel.getInstance();

        getParkingDetail();
        displayParkingDetail();
    }

    public void getParkingDetail(){
        parkingViewModel.getUserParkings(id);
    }

    public void displayParkingDetail() {

    }
}