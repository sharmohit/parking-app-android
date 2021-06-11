// Group: Project Groups 12
// Name: Mohit Sharma
// Student ID: 101342267
// Group Member: Javtesh Singh Bhullar
// Member ID: 101348129

package com.gbc.parkingapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gbc.parkingapp.model.Parking;
import com.gbc.parkingapp.repository.ParkingRepository;

import java.util.List;

public class ParkingViewModel extends AndroidViewModel {

    private static ParkingViewModel instance;
    private final ParkingRepository parkingRepository = new ParkingRepository();
    private MutableLiveData<List<Parking>> parkingListLiveData = new MutableLiveData<>();

    public static ParkingViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new ParkingViewModel(application);
        }

        return instance;
    }

    public ParkingViewModel(Application application) {
        super(application);
    }

    public void getUserParkings(String userId) {
        this.parkingRepository.getUserParkings(userId, this.parkingListLiveData);
    }

    public MutableLiveData<List<Parking>> getParkingListLiveData() {
        return parkingListLiveData;
    }
}
