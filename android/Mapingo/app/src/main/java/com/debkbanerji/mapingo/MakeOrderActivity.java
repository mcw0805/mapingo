package com.debkbanerji.mapingo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MakeOrderActivity extends AppCompatActivity {

    private ListView menuLv;
    private ListView orderLv;
    private List<String> menuItems;
    private List<String> orderItems;
    private ArrayAdapter<String> menuAdapter;
    private ArrayAdapter<String> orderAdapter;
    private Button submitOrderButton;
    private DatabaseReference mRootRef;
    private DatabaseReference mShopRef;
    private DatabaseReference mMenuItemRef;
    private DatabaseReference mOrdersRef;
    private DatabaseReference mNumOrdersRef;
    private int numOrders;
    private String storeUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);

        menuItems = new ArrayList<>();
        orderItems = new ArrayList<>();

        menuLv = (ListView) findViewById(R.id.menu_items);
        orderLv = (ListView) findViewById(R.id.order_items);

        menuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems);
        orderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderItems);

        menuLv.setAdapter(menuAdapter);
        orderLv.setAdapter(orderAdapter);

        menuLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderItems.add(menuItems.get(position));
                orderAdapter.notifyDataSetChanged();
            }
        });

        submitOrderButton = (Button) findViewById(R.id.submit_order_button);
        submitOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();

            }
        });

        mRootRef = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        storeUID = intent.getStringExtra("storeUID");


        mShopRef = mRootRef.child("shops").child(storeUID);

        mOrdersRef = mShopRef.child("orders");

        mNumOrdersRef = mShopRef.child("num-orders");
        mNumOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numOrders = (int) convertDouble(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMenuItemRef = mShopRef.child("menu");
        mMenuItemRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("CHILDADDED", dataSnapshot.toString());
                String name = (String) ((Map) dataSnapshot.getValue()).get("name");
                double price = convertDouble(((Map) dataSnapshot.getValue()).get("price"));
                menuAdapter.add(name + "      $" + Double.toString(price));
                menuAdapter.notifyDataSetChanged();
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


    }

    private void submitOrder() {
        Intent submitOrderIntent = new Intent(MakeOrderActivity.this, OrderConfirmationActivity.class);
        int orderNum = numOrders;
        Order order = new Order(orderItems, orderNum + 1);

        String orderKey = mOrdersRef.push().getKey();

        mOrdersRef.child(orderKey).setValue(order);

        mNumOrdersRef.setValue(numOrders + 1);

        Toast.makeText(this, "Order submitted", Toast.LENGTH_SHORT).show();

        submitOrderIntent.putExtra("orderKey", orderKey);
        submitOrderIntent.putExtra("storeUID", storeUID);

        startActivity(submitOrderIntent);
    }

    /**
     * Checks if the value is a long -> if so, converts to double
     *
     * @param longValue - an object that is thought to be a long
     * @return double A double that is converted form the long param
     */
    private static double convertDouble(Object longValue) {
        double valueTwo = -1; // whatever to state invalid!

        if (longValue instanceof Long) {
            valueTwo = ((Long) longValue).doubleValue();
        } else if (longValue instanceof Double) {
            valueTwo = (double) longValue;
        }

        return valueTwo;
    }
}