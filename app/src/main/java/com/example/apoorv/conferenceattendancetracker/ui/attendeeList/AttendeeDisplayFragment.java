package com.example.apoorv.conferenceattendancetracker.ui.attendeeList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.apoorv.conferenceattendancetracker.R;
import com.example.apoorv.conferenceattendancetracker.data.model.Attendee;
import com.example.apoorv.conferenceattendancetracker.ui.qrscanner.QRCheckinActivity;
import com.example.apoorv.conferenceattendancetracker.utils.mvp.BaseView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.apoorv.conferenceattendancetracker.utils.Properties.ATTENDEE_DATABASE;

public class AttendeeDisplayFragment extends BaseView {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        photos = new ArrayList<>();
//        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
//        MainUiThread mainUiThread = MainUiThread.getInstance();
//        DatabaseDefinition databaseDefinition = FlowManager.getDatabase(LocalDatabase.class);
//        DataRepository dataRepository = Injection.provideDataRepository(mainUiThread,
//                threadExecutor, databaseDefinition);
//        presenter = new PhotosPresenter(this, dataRepository, threadExecutor, mainUiThread);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_display, container, false);
        ButterKnife.bind(this,view);
        getFirebaseInstance();
        setUi();
        UpdateMyAttendeeList();
        return view;
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
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.server_error_message), Toast.LENGTH_SHORT).show();
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
        layoutManager = new LinearLayoutManager(getContext());
        attendeeDisplayRecyclerView.setLayoutManager(layoutManager);
        attendeeDisplayAdapter = new AttendeeDisplayAdapter(attendeeList, getContext());
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

}
