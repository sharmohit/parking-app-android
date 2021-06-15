/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.gbc.parkingapp.databinding.ParkingRowLayoutBinding;
import com.gbc.parkingapp.model.Parking;

import java.text.DateFormat;

public class ParkingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ParkingRowLayoutBinding binding;
    private Context context;

    public ParkingViewHolder(ParkingRowLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = binding.getRoot().getContext();
        this.binding.getRoot().setOnClickListener(this);
    }

    public void bind(Context context, Parking parking) {
        this.binding.tvCarPlate.setText(parking.getCar_plate_number());
        this.binding.tvDateTime.setText(DateFormat.getDateInstance(DateFormat.FULL)
                .format(parking.getDate_time().toDate()) + " at " + DateFormat.getTimeInstance(DateFormat.SHORT)
                .format(parking.getDate_time().toDate()));
        this.binding.tvLocation.setText(parking.getStreet_address());
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            //TODO Navigate to parking details
        }
    }
}
