package com.example.apoorv.conferenceattendancetracker.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.apoorv.conferenceattendancetracker.R;
import com.example.apoorv.conferenceattendancetracker.ui.attendeeList.AttendeeDisplayFragment;
import com.example.apoorv.conferenceattendancetracker.ui.qrscanner.QRCheckinActivity;
import com.example.apoorv.conferenceattendancetracker.utils.BaseFragmentInteractionListener;
import com.example.apoorv.conferenceattendancetracker.utils.FoaBaseActivity;
import com.example.apoorv.conferenceattendancetracker.utils.NetworkHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * The container responsible for showing and destroying relevant {@link Fragment}, handling
 * back and up navigation using the Fragment back stack and maintaining global state
 * and event subscriptions. This is based on the Fragment Oriented Architecture explained here
 * http://vinsol.com/blog/2014/09/15/advocating-fragment-oriented-applications-in-android/
 */
public class MainActivity extends FoaBaseActivity implements BaseFragmentInteractionListener {

    @BindView(R.id.fragmentPlaceHolder)
    FrameLayout fragmentPlaceholder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvOfflineMode)
    TextView tvOfflineMode;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    private IntentFilter connectivityIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        showFragment(AttendeeDisplayFragment.class);
        connectivityIntentFilter = new IntentFilter(CONNECTIVITY_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityBroadcastReceiver, connectivityIntentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(connectivityBroadcastReceiver);
        super.onPause();
    }


    BroadcastReceiver connectivityBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkHelper.getInstance().isNetworkAvailable(context)) {
                tvOfflineMode.setVisibility(View.VISIBLE);
            } else {
                tvOfflineMode.setVisibility(View.GONE);
            }
        }
    };


    @Override
    public void resetToolBarScroll() {
        appBarLayout.setExpanded(true, true);
    }

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
                Intent intent = new Intent(MainActivity.this, QRCheckinActivity.class);
                startActivity(intent);
                break;

            // if menu Refresh is selected
            case R.id.refresh:
//                mFirebaseDatabase.getReference().keepSynced(true);
//                Toast.makeText(this,getResources().getString(R.string.refresh),Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
