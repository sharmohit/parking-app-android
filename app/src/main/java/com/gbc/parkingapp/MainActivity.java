// Group: Project Groups 12
// Name: Mohit Sharma
// Student ID: 101342267
// Group Member: Javtesh Singh Bhullar
// Member ID: 101348129

package com.gbc.parkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //this.db = FirebaseFirestore.getInstance();
        //this.fetchUsers();
        //this.addTestUser();
    }

    private void fetchUsers() {

        final String TAG = "TEST";

        try {
            this.db.collection("users").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                Log.d(TAG, "onSuccess: " + documentSnapshot.getData().toString());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                        }
                    });

        } catch (Exception e) {
            Log.e(TAG, "fetchUsers: " + e.getLocalizedMessage());
        }
    }

    private void addTestUser() {

        final String TAG = "TEST";

        Map<String, String> data = new HashMap<>();
        data.put("Test", "Success");

        try {
            this.db.collection("users").add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: Added data, Id: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Add data failed: " + e);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "addTestUser: " + e);
        }
    }
}