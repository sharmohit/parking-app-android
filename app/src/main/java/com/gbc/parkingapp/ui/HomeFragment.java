/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbc.parkingapp.ParkingAdapter;
import com.gbc.parkingapp.databinding.FragmentHomeBinding;
import com.gbc.parkingapp.model.Parking;
import com.gbc.parkingapp.viewmodel.ParkingViewModel;
import com.gbc.parkingapp.viewmodel.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ParkingViewModel parkingViewModel;
    private ParkingAdapter adapter;
    private ArrayList<Parking> parkingArrayList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.adapter = new ParkingAdapter(this.getContext(), this, this.parkingArrayList);
        this.parkingViewModel = ParkingViewModel.getInstance();

        this.parkingViewModel.getParkingListLiveData().observe(this, new Observer<List<Parking>>() {
            @Override
            public void onChanged(List<Parking> parkings) {
                parkingArrayList.clear();
                parkingArrayList.addAll(parkings);
                adapter.notifyDataSetChanged();

                if (parkings.isEmpty()) {
                    binding.tvNoParking.setVisibility(View.VISIBLE);
                    binding.tvAddParking.setVisibility(View.VISIBLE);
                } else {
                    binding.tvNoParking.setVisibility(View.INVISIBLE);
                    binding.tvAddParking.setVisibility(View.INVISIBLE);
                }
                binding.progressIndicator.setVisibility(View.INVISIBLE);
            }
        });

        if (this.parkingViewModel.getParkingListLiveData().getValue() == null) {
            this.parkingViewModel.getUserParkings(UserViewModel.getInstance()
                    .userLiveData.getValue().getId());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentHomeBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.binding.progressIndicator.setVisibility(View.VISIBLE);
        this.binding.rvParkings.setAdapter(this.adapter);
        this.binding.rvParkings.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}