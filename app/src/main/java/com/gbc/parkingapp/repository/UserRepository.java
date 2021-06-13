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

import com.gbc.parkingapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepository {
    private final String TAG = this.getClass().getCanonicalName();
    private final String COLLECTION_NAME = "users";
    private FirebaseFirestore db;

    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void getUserByEmail(String email, MutableLiveData<User> userLiveData) {
        
        try {
            this.db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!task.getResult().getDocuments().isEmpty()) {
                                    DocumentSnapshot documentSnapshot = task.getResult()
                                            .getDocuments().get(0);
                                    User fetchedUser = (User) documentSnapshot.toObject(User.class);
                                    fetchedUser.setId(documentSnapshot.getId());
                                    Log.d(TAG, "onComplete: " + fetchedUser.toString());
                                    userLiveData.postValue(fetchedUser);
                                } else {
                                    Log.d(TAG, "onComplete: No user found");
                                    userLiveData.postValue(new User());
                                }
                            } else {
                                Log.d(TAG, "onComplete: Error while fetching user");
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "getUserByEmail: " + e);
        }
    }
}
