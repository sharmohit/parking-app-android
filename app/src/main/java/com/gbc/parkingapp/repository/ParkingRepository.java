/*
Group: Project Groups 12
Name: Mohit Sharma
Student ID: 101342267
Group Member: Javtesh Singh Bhullar
Member ID: 101348129
 */

package com.gbc.parkingapp.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.gbc.parkingapp.model.Parking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingRepository {
    private final String TAG = this.getClass().getCanonicalName();
    private final String COLLECTION_USER = "users";
    private final String COLLECTION_PARKING = "parkings";
    private final String QUERY_PARKING_BY = "date_time";
    private FirebaseFirestore db;

    public ParkingRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void getUserParkings(String userId, MutableLiveData<List<Parking>> parkingListLiveData) {
        try {
            this.db.collection(COLLECTION_USER)
                    .document(userId)
                    .collection(COLLECTION_PARKING)
                    .orderBy(QUERY_PARKING_BY, Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<Parking> parkingList = new ArrayList<>();
                            if (task.isSuccessful()) {
                                List<DocumentChange> documentChanges = task.getResult().getDocumentChanges();
                                if (!documentChanges.isEmpty()) {
                                    for (DocumentChange documentChange : documentChanges) {
                                        Parking parking = documentChange.getDocument().toObject(Parking.class);
                                        parking.setId(documentChange.getDocument().getId());
                                        parkingList.add(parking);
                                    }
                                    Log.d(TAG, "onComplete: ParkinList: " + parkingList.toString());
                                    parkingListLiveData.postValue(parkingList);
                                } else {
                                    Log.d(TAG, "onComplete: No parkings found");
                                    parkingListLiveData.postValue(parkingList);
                                }
                            } else {
                                Log.d(TAG, "onComplete: Failed: Unable to fetch parkings");
                            }
                        }
                    });

        } catch (Exception e) {
            Log.e(TAG, "getUserParkings: " + e.getLocalizedMessage());
            List<Parking> parkingList = new ArrayList<>();
            parkingListLiveData.postValue(parkingList);
        }
    }

    public void addUserParking(String userId, Parking parking, MutableLiveData<Parking> newParkingLiveData) {
        try {
            this.db.collection(COLLECTION_USER)
                    .document(userId)
                    .collection(COLLECTION_PARKING)
                    .add(parking)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            parking.setId(documentReference.getId());
                            newParkingLiveData.postValue(parking);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Unable to add parking " + e.getLocalizedMessage());
                            newParkingLiveData.postValue(new Parking());
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "addUserParking: " + e.getLocalizedMessage());
        }
    }
    
    public void deleteUserParkingById(String userId, String parkingId, MutableLiveData<List<Parking>> parkingListLiveData ) {
        
        try {
            this.db.collection(COLLECTION_USER).document(userId)
                    .collection(COLLECTION_PARKING)
                    .document(parkingId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: Delete Parking");
                            for (int i = 0; i < parkingListLiveData.getValue().size(); i++)
                            {
                                if (parkingListLiveData.getValue().get(i).getId().contentEquals(parkingId))
                                {
                                    Log.d(TAG, "onEvent: Removed Existing Parking");
                                    parkingListLiveData.getValue().remove(i);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Unable to delete user parkings" + e.getLocalizedMessage());
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "deleteUserParkingById: " + e.getLocalizedMessage());
        }
    }

    public void updateUserParking(String userId, Parking parking, MutableLiveData<Parking> parkingLiveData) {

        Map<String, Object> data = new HashMap<>();
        data.put("building_code", parking.getBuilding_code());
        data.put("parking_hours", parking.getParking_hours());
        data.put("suit_number", parking.getSuit_number());
        data.put("street_address", parking.getStreet_address());
        data.put("date_time", parking.getDate_time());
        data.put("coordinate", parking.getCoordinate());
        data.put("car_plate_number", parking.getCar_plate_number());

        try {
            this.db.collection(COLLECTION_USER)
                    .document(userId)
                    .collection(COLLECTION_PARKING)
                    .document(parking.getId())
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: Updated parking successfully");
                            parkingLiveData.postValue(parking);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Unable to update parking" + e);
                            parkingLiveData.postValue(new Parking());
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "updateUserParking: " + e.getLocalizedMessage());
        }
    }
}
