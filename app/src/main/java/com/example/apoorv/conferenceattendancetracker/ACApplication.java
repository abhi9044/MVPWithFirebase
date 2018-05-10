package com.example.apoorv.conferenceattendancetracker;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.apoorv.conferenceattendancetracker.Utils.ApplicationUtils.ATTENDEE_DATABASE;

public class ACApplication extends Application {

    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mAttendeeDatabaseReference;

    @Override
    public void onCreate() {
        super.onCreate();

        //For Initial Establishment of reference to the Firebase Database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        if (!FirebaseApp.getApps(this).isEmpty()) {
            mFirebaseDatabase.setPersistenceEnabled(true);
        }
        mAttendeeDatabaseReference = FirebaseDatabase.getInstance().getReference().child(ATTENDEE_DATABASE);
    }
}
