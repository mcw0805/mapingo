package com.debkbanerji.mapingo;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderConfirmationActivity extends AppCompatActivity implements LocationListener {

    private TextView refNumValue;
    private TextView latitudeText;
    private TextView longitudeText;
    private Button finishButton;
    private DatabaseReference mRootRef;
    private DatabaseReference mNumOrdersRef;

    /*
   Variables to enable GPS functionality
    */
    private boolean signalFound;
    private double gpsLatitude;
    private double gpsLongitude;
    private LocationManager locationManager;

    private static final String[] INITIAL_PERMS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final int INITIAL_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        /*
        Request GPS Permissions
         */
        if (Build.VERSION.SDK_INT >= 23 && PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }

        // setting up LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    500,   // Interval in milliseconds
                    10, this);
        } catch (SecurityException e) {
            // Toast.makeText(getBaseContext(), "Security exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        latitudeText = (TextView) findViewById(R.id.latitude_text);
        longitudeText = (TextView) findViewById(R.id.longitude_text);

        refNumValue = (TextView) findViewById(R.id.ref_num_value);

        Intent intent = getIntent();
        String orderKey = intent.getStringExtra("orderKey");
        String storeUID = intent.getStringExtra("storeUID");


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mNumOrdersRef = mRootRef.child("shops").child(storeUID).child("orders").child(orderKey).child("orderNum");
        mNumOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                refNumValue.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        finishButton = (Button) findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHome();

            }
        });

    }

    private void backHome() {
        Intent finishIntent = new Intent(OrderConfirmationActivity.this, HomeActivity.class);
        startActivity(finishIntent);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        gpsLatitude = location.getLatitude();
        gpsLongitude = location.getLongitude();
        signalFound = true;
        Log.d("UPDATE", "GPSUPDATE");

        latitudeText.setText(Double.toString(gpsLatitude));
        longitudeText.setText(Double.toString(gpsLongitude));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
