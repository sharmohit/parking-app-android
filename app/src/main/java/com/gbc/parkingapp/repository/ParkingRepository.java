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

import java.util.ArrayList;
import java.util.List;

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
                            if (task.isSuccessful()) {
                                List<DocumentChange> documentChanges = task.getResult().getDocumentChanges();
                                if (!documentChanges.isEmpty()) {
                                    List<Parking> parkingList = new ArrayList<>();
                                    for (DocumentChange documentChange : documentChanges) {
                                        Parking parking = documentChange.getDocument().toObject(Parking.class);
                                        parking.setId(documentChange.getDocument().getId());
                                        parkingList.add(parking);
                                        switch (documentChange.getType()) {
                                            case ADDED:
                                                Log.d(TAG, "onComplete: Parking Added: " + documentChange.getDocument().getData());
                                                break;
                                            case MODIFIED:
                                                Log.d(TAG, "onComplete: Parking Updated: " + documentChange.getDocument().getData());
                                                break;
                                            case REMOVED:
                                                Log.d(TAG, "onComplete: Parking Removed: " + documentChange.getDocument().getData());
                                                break;
                                        }
                                    }
                                    Log.d(TAG, "onComplete: ParkinList: " + parkingList.toString());
                                    parkingListLiveData.postValue(parkingList);
                                } else {
                                    Log.d(TAG, "onComplete: No parkings found");
                                }
                            } else {
                                Log.d(TAG, "onComplete: Failed: Unable to fetch parkings");
                            }
                        }
                    });

        } catch (Exception e) {
            Log.e(TAG, "getUserParkings: " + e.getLocalizedMessage());
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
}
