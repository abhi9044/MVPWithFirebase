package com.example.apoorv.conferenceattendancetracker.ui.attendeeList;

import android.content.Context;

import com.example.apoorv.conferenceattendancetracker.R;
import com.example.apoorv.conferenceattendancetracker.data.model.Attendee;
import com.example.apoorv.conferenceattendancetracker.utils.mvp.BasePresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.apoorv.conferenceattendancetracker.utils.Properties.ATTENDEE_DATABASE;

public class AttendeePresenter extends BasePresenter<AttendeeContract.View> implements AttendeeContract.Presenter {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private List<Attendee> attendeeList;

    public AttendeePresenter(AttendeeContract.View view) {
        this.view = view;
    }

    @Override
    public void getAttendeeList(final Context context) {
        if (view == null) {
            return;
        }
        attendeeList = new ArrayList<Attendee>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(ATTENDEE_DATABASE);
        mFirebaseDatabase.getReference().keepSynced(true);
        view.setProgressBar(true);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                attendeeList.clear();
                for (DataSnapshot attendee : dataSnapshot.getChildren()) {

                    attendeeList.add(new Attendee(attendee.getValue(Attendee.class).getId(),
                            attendee.getValue(Attendee.class).getName(),
                            attendee.getValue(Attendee.class).getCompany(),
                            attendee.getValue(Attendee.class).getType(),
                            attendee.getValue(Attendee.class).getCheckedIn()));
                }
                view.showAttendeeList(attendeeList);
                view.shouldShowPlaceholderText();
                view.setProgressBar(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.shouldShowPlaceholderText();
                view.showToastMessage(context.getResources().getString(R.string.server_error_message));
            }
        });
    }
}
