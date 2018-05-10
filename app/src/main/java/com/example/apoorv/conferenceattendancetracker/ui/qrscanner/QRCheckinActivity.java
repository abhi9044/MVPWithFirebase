package com.example.apoorv.conferenceattendancetracker.ui.qrscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.apoorv.conferenceattendancetracker.R;
import com.example.apoorv.conferenceattendancetracker.data.model.Attendee;
import com.example.apoorv.conferenceattendancetracker.ui.attendeeList.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.apoorv.conferenceattendancetracker.Utils.Properties.ATTENDEE_DATABASE;
import static com.example.apoorv.conferenceattendancetracker.Utils.Properties.BASEURL;

public class QRCheckinActivity extends AppCompatActivity {

    /*
        Variables
        qrScan -- refers to IntentIntegrator required to resolve the scan of QR Code
        mDatabaseReference -- refers to the reference to the api end point where CRUD operations takes place
        updatedData -- refers to the scanned object of Attendee needed to be updated
        obj -- JSONObject returned as result of QR CODE Scan
     */
    private IntentIntegrator qrScan;
    private DatabaseReference mDatabaseReference;
    private Attendee updatedData;
    private String updatedId;
    private JSONObject obj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcheckin);
        getMyFirebaseInstance();
        initialiseQRScan();
    }

    //Initialise Qr scan
    private void initialiseQRScan() {
        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();
    }

    // Initialise Firebase reference to be used for CRUD Operations
    private void getMyFirebaseInstance() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(ATTENDEE_DATABASE);
    }

    /*
    @funtion onActivity result called as a result of QR Scan
    @result --  contains the result after scan
    @result is then handled for null and not null conditions so as to update the Attendee object or show an error message
    and hence return to the main activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, getResources().getString(R.string.result_not_found), Toast.LENGTH_LONG).show();
                startActivity(new Intent(QRCheckinActivity.this, MainActivity.class));
            } else {
                makeMyUpdatedObject(result);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // set the values of the updated object using its id and call  @function updateMyStatus
    private void makeMyUpdatedObject(IntentResult result) {
        try {
            obj = new JSONObject(result.getContents());
            updatedData = new Attendee(obj.getString("id"), obj.getString("name"), obj.getString("company"), obj.getString("type"), obj.getBoolean("checkedIn"));
            updatedId = obj.getString("id");
            updateMyStatus(updatedData, updatedId);
        } catch (JSONException e) {
            e.printStackTrace();
            doErrorHandling(e);
        }
    }

    //Error handling in case of parse exception
    private void doErrorHandling(JSONException e) {
        e.printStackTrace();
        Toast.makeText(this, getResources().getString(R.string.scan_error), Toast.LENGTH_LONG).show();
        startActivity(new Intent(QRCheckinActivity.this, MainActivity.class));
    }

    /* Function is called to perform update operation on the checkedIn value of the given object and update the list
     *
     @param updatedData -- refers to the Attendee object to be updated
     @param updatedId -- refers to the fetched id from qr scan of the updatedData  and the value is updated using mDatabaseReference
     */
    private void updateMyStatus(Attendee updatedData, String updatedId) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(BASEURL + ATTENDEE_DATABASE + "/" + updatedId);
        mDatabaseReference.setValue(updatedData);
        Toast.makeText(this, updatedData.getName() + " Checked In!!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(QRCheckinActivity.this, MainActivity.class));
    }
}
