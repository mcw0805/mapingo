package com.debkbanerji.mapingo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseShopsActivity extends AppCompatActivity {

    private ListView restaurantLv;
    private List<String> restaurantList;
    private ArrayAdapter<String> restaurantAdapter;
    private DatabaseReference mRootRef;
    private DatabaseReference mShopsRef;
    private Map<String, String> restaurantMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_shops);

        restaurantList = new ArrayList<>();
        restaurantLv = (ListView) findViewById(R.id.restaurant_list);
        restaurantMap = new HashMap<>();

        restaurantAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restaurantList);
        restaurantLv.setAdapter(restaurantAdapter);

        mRootRef = FirebaseDatabase.getInstance().getReference();


        mShopsRef = mRootRef.child("shops");
        mShopsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name = (String) ((Map) dataSnapshot.getValue()).get("name");

                restaurantAdapter.add(name);
                restaurantAdapter.notifyDataSetChanged();
                restaurantMap.put(name, dataSnapshot.getKey());
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
        });
        restaurantLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = restaurantList.get(position);
                goToRestaurantMenu(restaurantMap.get(name));
            }
        });

    }

    private void goToRestaurantMenu(String shopKey) {
        Intent goToRestaurantMenuIntent = new Intent(ChooseShopsActivity.this, MakeOrderActivity.class);
        goToRestaurantMenuIntent.putExtra("storeUID", shopKey);
        startActivity(goToRestaurantMenuIntent);
        finish();
    }


}
