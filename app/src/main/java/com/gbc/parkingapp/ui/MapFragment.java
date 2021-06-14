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
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbc.parkingapp.databinding.FragmentMapBinding;
import com.gbc.parkingapp.model.Parking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private final String TAG = this.getClass().getCanonicalName();
    private MapView mapView;
    private GoogleMap googleMap;
    private FragmentMapBinding binding;
    private Parking parking;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentMapBinding.inflate(inflater, container, false);

        this.mapView = this.binding.mapView;
        this.mapView.onCreate(savedInstanceState);
        this.parking = MapFragmentArgs.fromBundle(getArguments()).getParking();
        if (this.parking != null) {
            this.binding.tvAddress.setText(this.parking.getStreet_address());
        }

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Unable to initialize map " + e.getLocalizedMessage());
        }

        this.mapView.getMapAsync(this::onMapReady);
        this.binding.fabBack.setOnClickListener(this::onClick);

        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: " + parking.getId() );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.setupMapAppearance(this.googleMap);

        if (this.parking == null) {
            return;
        }

        LatLng sydney = new LatLng(this.parking.getCoordinate().getLatitude(),
                this.parking.getCoordinate().getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        this.googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        this.mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.mapView.onLowMemory();
    }

    private void setupMapAppearance(GoogleMap googleMap) {
        googleMap.setBuildingsEnabled(true);
        googleMap.setIndoorEnabled(false);
        googleMap.setTrafficEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        UiSettings myUiSettings = googleMap.getUiSettings();
        myUiSettings.setZoomControlsEnabled(true);
        myUiSettings.setZoomGesturesEnabled(true);
        myUiSettings.setScrollGesturesEnabled(true);
        myUiSettings.setRotateGesturesEnabled(true);
        myUiSettings.setMyLocationButtonEnabled(false);
    }

    @Override
    public void onClick(View v) {
        NavHostFragment.findNavController(this).popBackStack();
    }
}