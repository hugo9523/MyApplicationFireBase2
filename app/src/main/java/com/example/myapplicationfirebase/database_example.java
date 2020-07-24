package com.example.myapplicationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class database_example extends AppCompatActivity {
    FirebaseFirestore db ;

    void foo_insert()
    {
        try {
            Map<String, Object> user = new HashMap<>();
            user.put("first", "Ada");
            user.put("last", "Lovelace");
            user.put("born", "1815");


// Add a new document with a generated ID
            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Log.i("INFO", "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("INFO", "Error adding document", e);
                        }
                    });
        }catch (Exception e)
        {
            throw e;
        }
    }
    void select_users()
    {
        try {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> user= new HashMap<>();

                                user.putAll(document.getData());

                                Log.i("INFO", document.getId() + " => " + user);
                            }
                        } else {
                            Log.i("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }catch(Exception e)
        {
            throw e;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_database_example);
            db= FirebaseFirestore.getInstance();
            select_users();
            // Create a new user with a first and last name

        } catch (Exception e)
        {
            Log.e("Error",e.getMessage());
        }
    }
}