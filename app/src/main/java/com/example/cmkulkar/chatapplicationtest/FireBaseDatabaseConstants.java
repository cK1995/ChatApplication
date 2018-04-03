package com.example.cmkulkar.chatapplicationtest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseDatabaseConstants {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef;

    public void setDatabaseReference(String reference) {
            dbRef = database.getReference(reference);
    }

    public void setDatabaseReference() {
        dbRef = database.getReference();
    }

    public void setChild(DatabaseReference parentRef,String child) {
        dbRef = parentRef.child(child);
    }

    public DatabaseReference getDatabaseReference() {
        return dbRef;
    }
}
