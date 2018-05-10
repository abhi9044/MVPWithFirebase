package com.example.apoorv.conferenceattendancetracker.ui.attendeeList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.apoorv.conferenceattendancetracker.QRCheckinActivity;
import com.example.apoorv.conferenceattendancetracker.R;
import com.example.apoorv.conferenceattendancetracker.data.model.Attendee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.apoorv.conferenceattendancetracker.Utils.Properties.ATTENDEE_DATABASE;

public class MainActivity extends AppCompatActivity {

    /*
    Variables  attendeeDisplayRecyclerView-- responsible for displaying the list
               mProgressBar -- responsible for displaying progress to the user once any api call starts
               layoutManager -- setting the recycler view's layout manager
               attendeeDisplayAdapter -- adapter that takes in @param attendeeList and Context of the activity
               mFirebaseDatabase -- refers to the realtime firebase database linked to the application
               mDatabaseReference -- refers to the reference to the api end point where CRUD operations takes place
     */
    @BindView(R.id.attendee_recycler_view)
    RecyclerView attendeeDisplayRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private RecyclerView.LayoutManager layoutManager;
    private AttendeeDisplayAdapter attendeeDisplayAdapter;
    private List<Attendee> attendeeList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFirebaseInstance();
        setUi();
        UpdateMyAttendeeList();
    }

    /*On any crud operation performed it updates the list by listening to
     *@function onDatachange -- calls @function setMyAttendanceObject
     *@function onCancelled -- Displays the error message if api cancelled
     */
    private void UpdateMyAttendeeList() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                attendeeList.clear();
                for (DataSnapshot attendee : dataSnapshot.getChildren()) {

                    setMyAttendanceObject(attendee);
                }

                setMyAdapter();
                closeProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set recycler view with the adapter and notify about the data set changed
    private void setMyAdapter() {
        attendeeDisplayRecyclerView.setAdapter(attendeeDisplayAdapter);
        attendeeDisplayAdapter.notifyDataSetChanged();
    }

    /*
       @param attendee -- datasnapshot updated
       is as the new attendee object and updated
     */
    private void setMyAttendanceObject(DataSnapshot attendee) {
        attendeeList.add(new Attendee(attendee.getValue(Attendee.class).getId(),
                attendee.getValue(Attendee.class).getName(),
                attendee.getValue(Attendee.class).getCompany(),
                attendee.getValue(Attendee.class).getType(),
                attendee.getValue(Attendee.class).getCheckedIn()));
    }

    //Binding the views and initialising layout manager ,recycler view and display adapter for the list
    private void setUi() {
        attendeeList = new ArrayList<>();
        ButterKnife.bind(this);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        attendeeDisplayRecyclerView.setLayoutManager(layoutManager);
        attendeeDisplayAdapter = new AttendeeDisplayAdapter(attendeeList, MainActivity.this);
        showProgressBar();
    }

    //TO show progress bar
    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    //Hide progress bar
    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    // Initialising the firebase instance
    private void getFirebaseInstance() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(ATTENDEE_DATABASE);
        mFirebaseDatabase.getReference().keepSynced(true);
    }

    /*
    @param menu -- menu created is inflated using menu inflater
     contains options -- Refresh
                      -- Check In
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //if menu option Check In is selected
            case R.id.checkin:
                startActivity(new Intent(MainActivity.this, QRCheckinActivity.class));
                break;
            // if menu Refresh is selected
            case R.id.refresh:
                mFirebaseDatabase.getReference().keepSynced(true);
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.refresh),Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
