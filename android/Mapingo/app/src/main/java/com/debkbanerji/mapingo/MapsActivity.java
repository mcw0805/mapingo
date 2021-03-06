package com.debkbanerji.mapingo;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ChildEventListener mShopRefChildEventListener;
    private DatabaseReference mShopRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Emory University.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Emory and move the camera
//        LatLng emory = new LatLng(33.7925, -84.324);
//        mMap.addMarker(new MarkerOptions().position(emory).title("Marker in Emory"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(emory));

        mShopRef = FirebaseDatabase.getInstance().getReference().child("shops");
        mShopRefChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.d("CHILDADDED", dataSnapshot.toString());
                Map<Object, Object> shopLoc = (Map<Object, Object>) dataSnapshot.getValue();
                Double latitude = (Double) shopLoc.get("latitude");
                Double longitude = (Double) shopLoc.get("longitude");
                LatLng shopPosition = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(shopPosition).title((String) shopLoc.get("name")));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(shopPosition));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mShopRef.addChildEventListener(mShopRefChildEventListener);
    }
}
