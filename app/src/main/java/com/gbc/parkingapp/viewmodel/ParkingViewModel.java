/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gbc.parkingapp.model.Parking;
import com.gbc.parkingapp.model.User;
import com.gbc.parkingapp.repository.ParkingRepository;

import java.util.List;

public class ParkingViewModel extends ViewModel {

    private static ParkingViewModel instance;
    private final ParkingRepository parkingRepository = new ParkingRepository();
    private MutableLiveData<Parking> newParkingLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Parking>> parkingListLiveData = new MutableLiveData<>();

    private static final Parking parkingInstance = new Parking();
    public static Parking parkingInstance() {
        return parkingInstance;
    }

    public static ParkingViewModel getInstance() {
        if (instance == null) {
            instance = new ParkingViewModel();
        }

        return instance;
    }

    public ParkingViewModel() {
        super();
    }

    public void getUserParkings(String userId) {
        this.parkingRepository.getUserParkings(userId, this.parkingListLiveData);
    }

    public void addUserParking(String userId, Parking parking){
        this.parkingRepository.addUserParking(userId, parking, this.newParkingLiveData);
    }

    public void updateUserParking(String userId, Parking parking){
        this.parkingRepository.updateUserParking(userId,parking, this.newParkingLiveData);
    }

    public void deleteUserParking(String userId, String parkingId) {
        this.parkingRepository.deleteUserParkingById(userId, parkingId, this.parkingListLiveData);
    }

    public MutableLiveData<Parking> getNewParkingLiveData() {
        return newParkingLiveData;
    }

    public MutableLiveData<List<Parking>> getParkingListLiveData() {
        return parkingListLiveData;
    }
}
