/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.gbc.parkingapp.databinding.ParkingRowLayoutBinding;
import com.gbc.parkingapp.model.Parking;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingViewHolder> {
    private ArrayList<Parking> parkings;
    private Context context;
    private Fragment fragment;

    public ParkingAdapter(Context context, androidx.fragment.app.Fragment fragment, ArrayList<Parking> parkings) {
        this.context = context;
        this.fragment = fragment;
        this.parkings = parkings;
    }

    @Override
    public ParkingViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ParkingRowLayoutBinding binding =
                ParkingRowLayoutBinding.inflate(LayoutInflater.from(this.context), viewGroup,
                        false);
        return new ParkingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ParkingViewHolder parkingViewHolder, final int position) {
        Parking parking = this.parkings.get(position);
        parkingViewHolder.bind(this.context, this.fragment, parking);
    }

    @Override
    public int getItemCount() {
        return this.parkings.size();
    }
}
