/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.gbc.parkingapp.databinding.ActivityMainBinding;
import com.gbc.parkingapp.helper.LocationHelper;
import com.gbc.parkingapp.model.Parking;
import com.gbc.parkingapp.viewmodel.ParkingViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(this.getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.locationHelper = LocationHelper.getInstance();

        BottomNavigationView navView = this.binding.bottomNavView;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NotNull NavController navController, @NotNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.map_fragment
                        || navDestination.getId() == R.id.loginFragment
                || navDestination.getId() == R.id.signUpFragment) {
                    navView.setVisibility(View.GONE);
                } else {
                    navView.setVisibility(View.VISIBLE);

                    if (navDestination.getId() == R.id.home_fragment) {
                        BadgeDrawable badge = navView.getBadge(R.id.home_fragment);
                        if (badge != null) {
                            badge.setVisible(false);
                        }
                    }
                }
            }
        });
        ParkingViewModel.getInstance().getNewParkingLiveData().observe(this, new Observer<Parking>() {
            @Override
            public void onChanged(Parking parking) {
                if (parking != null && !parking.getId().isEmpty()) {
                    BadgeDrawable badge = navView.getOrCreateBadge(R.id.home_fragment);
                    badge.setVisible(true);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == this.locationHelper.REQUEST_CODE_LOCATION) {
            this.locationHelper.isLocationPermissionGranted = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);

            if (!this.locationHelper.isLocationPermissionGranted) {
                Snackbar.make(this.binding.getRoot(), "Location Access not Granted. Please Try Again.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}