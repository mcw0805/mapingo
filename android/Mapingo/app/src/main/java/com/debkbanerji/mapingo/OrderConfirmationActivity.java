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
import android.os.Handler;
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

import java.util.Timer;
import java.util.TimerTask;

public class OrderConfirmationActivity extends AppCompatActivity implements LocationListener {

    private TextView refNumValue;
    private TextView latitudeText;
    private TextView longitudeText;
    private TextView distanceText;
    private Button finishButton;
    private DatabaseReference mRootRef;
    private DatabaseReference mShopRef;
    private DatabaseReference mOrderRef;
    private DatabaseReference mNumOrdersRef;
    private DatabaseReference mDistanceRef;
    private DatabaseReference mSpeedRef;
    private DatabaseReference mETARef;

    /*
   Variables to enable GPS functionality
    */
    private boolean signalFound;
    private double gpsLatitude = -1000;
    private double gpsLongitude = -1000;
    private LocationManager locationManager;

    private static final String[] INITIAL_PERMS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final int INITIAL_REQUEST = 1337;

    private double shopLatitude = -1000;
    private double shopLongitude = -1000;

    private double oldDistance = -1;
    private long oldTime = -1;
    private final int interval = 1000;
    int writeSpeedCount = 0;
    private boolean hasWrittenSpeed;

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
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    0,   // Interval in milliseconds
//                    0, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    500,   // Interval in milliseconds
                    10, this);
        } catch (SecurityException e) {
            // Toast.makeText(getBaseContext(), "Security exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


        latitudeText = (TextView) findViewById(R.id.latitude_text);
        longitudeText = (TextView) findViewById(R.id.longitude_text);
        distanceText = (TextView) findViewById(R.id.distance_text);

        refNumValue = (TextView) findViewById(R.id.ref_num_value);

        Intent intent = getIntent();
        String orderKey = intent.getStringExtra("orderKey");
        String storeUID = intent.getStringExtra("storeUID");

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mShopRef = mRootRef.child("shops").child(storeUID);
        mOrderRef = mShopRef.child("orders").child(orderKey);
        mDistanceRef = mOrderRef.child("distance");
        mSpeedRef = mOrderRef.child("speed");
        mETARef = mOrderRef.child("ETA");
        mNumOrdersRef = mOrderRef.child("orderNum");
        mDistanceRef.setValue("Unknown");
        mSpeedRef.setValue("Unknown");
        mETARef.setValue("Unknown");
        mOrderRef.child("latitude").setValue("Unknown");
        mOrderRef.child("longitude").setValue("Unknown");
        mNumOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double databaseOrderNum = MakeOrderActivity.convertDouble(dataSnapshot.getValue());
                databaseOrderNum++;
                Integer displayNum = databaseOrderNum.intValue();
                refNumValue.setText(displayNum.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mShopRef.child("latitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopLatitude = MakeOrderActivity.convertDouble(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mShopRef.child("longitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopLongitude = MakeOrderActivity.convertDouble(dataSnapshot.getValue());
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

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                writeSpeed();
//            }
//        }, (long) interval);  //the time is in miliseconds

//        final Handler handler = new Handler();
//        Timer timer = new Timer();
//        TimerTask testing = new TimerTask() {
//            @Override
//            public void run() {
////                writeSpeed();
//                handler.post(new Runnable() {
//                    public void run() {
//                        Log.d("speed", "WRITING SPEED");
//                        if (signalFound) {
//                            Double savedOldDistance = oldDistance;
//                            Double newDistance = getDistance();
//                            oldDistance = newDistance;
//                            if (savedOldDistance >= 0) {
//                                Double speed = (newDistance - savedOldDistance) / (interval / 1000.0); //speed in m/s
//                                Double eta = newDistance / speed;
//                                mSpeedRef.setValue(speed);
//                                mETARef.setValue(eta);
//                            }
//                        }
//                    }
//
//                });
//
//            }
//        };
//        timer.schedule(testing, interval);

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
//        Log.d("UPDATE", "GPSUPDATE");

        latitudeText.setText(Double.toString(gpsLatitude));
        longitudeText.setText(Double.toString(gpsLongitude));
        int distance = (int) getDistance();
        if (distance > 0) {
            distanceText.setText(Integer.toString(distance) + " metres");
            writeDistance(distance);
        } else {
            distanceText.setText("Unknown");
        }
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

    public double getDistance() {

        Log.d("DIST", "SHOP LATITUDE" + Double.toHexString(shopLatitude));
        Log.d("DIST", "SHOP LONGITUDE" + Double.toHexString(shopLongitude));
        Log.d("DIST", "GPS LATITUDE" + Double.toHexString(gpsLatitude));
        Log.d("DIST", "GPS LONGITUDE" + Double.toHexString(gpsLongitude));

        if (gpsLatitude > -1000 && gpsLongitude > -1000 && shopLatitude > -1000 && shopLongitude > -1000) {
            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(shopLatitude - gpsLatitude);
            double dLng = Math.toRadians(shopLongitude - gpsLongitude);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(gpsLatitude)) * Math.cos(Math.toRadians(shopLatitude)) *
                            Math.sin(dLng / 2) * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double dist = (double) (earthRadius * c);
            return dist;
        } else {
            return -1;
        }
    }

    public void writeDistance(int distance) { // distance in meters
        if (distance > 0) {
            mDistanceRef.setValue(distance);
        } else {
            mDistanceRef.setValue("Unknown");
        }
        mOrderRef.child("latitude").setValue(gpsLatitude);
        mOrderRef.child("longitude").setValue(gpsLongitude);
//        writeSpeedCount++;
//        if (!hasWrittenSpeed) {
//            writeSpeed();
//        }
    }

//    private void writeSpeed() {
//        Log.d("speed", "WRITING SPEED");
//        if (signalFound) {
//            Double savedOldDistance = oldDistance;
//            Long savedOldTime = oldTime;
//            Double newDistance = getDistance();
//            oldDistance = newDistance;
//            oldTime = System.currentTimeMillis();
//            Long timedifference = System.currentTimeMillis() - savedOldTime;
//            if (savedOldDistance >= 0) {
//                hasWrittenSpeed = true;
//                Double speed = (newDistance - savedOldDistance) / (interval / timedifference); //speed in m/s
//                Double eta = newDistance / speed;
//                mSpeedRef.setValue(speed);
//                mETARef.setValue(eta);
//            }
//        }
//    }
}
