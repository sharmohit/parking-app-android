// Group: Project Groups 12
// Name: Mohit Sharma
// Student ID: 101342267
// Group Member: Javtesh Singh Bhullar
// Member ID: 101348129

package com.gbc.parkingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.gbc.parkingapp.databinding.ActivityHomeBinding;
import com.gbc.parkingapp.databinding.ActivityLoginBinding;
import com.gbc.parkingapp.model.Parking;
import com.gbc.parkingapp.viewmodel.ParkingViewModel;
import com.gbc.parkingapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getCanonicalName();
    private ActivityHomeBinding binding;
    private ParkingViewModel parkingViewModel;
    private ParkingAdapter adapter;
    private ArrayList<Parking> parkingArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeBinding.inflate(this.getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.adapter = new ParkingAdapter(this, this.parkingArrayList);
        this.binding.rvParkings.setAdapter(this.adapter);
        this.binding.rvParkings.setLayoutManager(new LinearLayoutManager(this));

        this.parkingViewModel = ParkingViewModel.getInstance(getApplication());
        this.parkingViewModel.getParkingListLiveData().observe(this, new Observer<List<Parking>>() {
            @Override
            public void onChanged(List<Parking> parkings) {
                parkingArrayList.clear();
                parkingArrayList.addAll(parkings);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.parkingViewModel.getUserParkings(UserViewModel.getInstance(getApplication())
                .userLiveData.getValue().getId());
    }
}