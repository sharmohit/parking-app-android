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
import com.gbc.parkingapp.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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

    public void signUpUser(User user, MutableLiveData<User> userLiveData){

        try{

            db.collection(COLLECTION_NAME)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: Document added successfully, User ID : " + documentReference.getId());
                            // Set User Id in User singleton

                            user.setId(documentReference.getId());
                           // id = documentReference.getId();

                            userLiveData.postValue(user);
                            Log.d(TAG, "Fetching User  : " + userLiveData.getValue().toString());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Error creating document on Firestore, User ID : " + e.getLocalizedMessage());
                        }
                    });

        }catch(Exception ex){
            Log.e(TAG, "addFriend: " + ex.getLocalizedMessage() );
        }
    }

    public void updateUser(User user, MutableLiveData<User> userLiveData) {

        Map<String, Object> data = new HashMap<>();
        data.put("name", user.getName());
        data.put("email",  user.getEmail());
        data.put("password",  user.getPassword());
        data.put("phone", user.getPhone());
        data.put("car_plate_number",  user.getCar_plate_number());

        try {
            this.db.collection(COLLECTION_NAME)
                    .document(user.getId())
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            userLiveData.postValue(user);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            userLiveData.postValue(new User());
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "updateUser: " + e.getLocalizedMessage());
        }
    }

    public void deleteUser(String id) {

        db.collection(COLLECTION_NAME).document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
}
