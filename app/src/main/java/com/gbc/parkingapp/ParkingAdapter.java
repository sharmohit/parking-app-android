// Group: Project Groups 12
// Name: Mohit Sharma
// Student ID: 101342267
// Group Member: Javtesh Singh Bhullar
// Member ID: 101348129

package com.gbc.parkingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.gbc.parkingapp.databinding.RowLayoutBinding;
import com.gbc.parkingapp.model.Parking;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<Parking> parkings;
    private Context context;

    public ParkingAdapter(Context context, ArrayList<Parking> parkings) {
        this.context = context;
        this.parkings = parkings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RowLayoutBinding binding =
                RowLayoutBinding.inflate(LayoutInflater.from(this.context), viewGroup,
                        false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Parking parking = this.parkings.get(position);
        viewHolder.bind(this.context, parking);
    }

    @Override
    public int getItemCount() {
        return this.parkings.size();
    }
}
