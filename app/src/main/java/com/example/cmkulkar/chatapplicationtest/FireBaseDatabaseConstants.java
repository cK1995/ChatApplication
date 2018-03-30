package com.example.cmkulkar.chatapplicationtest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseDatabaseConstants {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef;

    public void setDatabaseReference(String refrence) {
        dbRef = database.getReference(refrence);
    }

    public DatabaseReference getDatabaseReference() {
        return dbRef;
    }
}
