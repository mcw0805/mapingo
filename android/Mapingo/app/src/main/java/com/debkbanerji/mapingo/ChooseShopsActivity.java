package com.debkbanerji.mapingo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ChooseShopsActivity extends AppCompatActivity {

    private ListView restaurantLv;
    private List<String> restaurantList;
    private ArrayAdapter<String> restaurantAdapter;
    private DatabaseReference mRootRef;
    private DatabaseReference mShopRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_shops);

        restaurantList = new ArrayList<>();

        restaurantLv = (ListView) findViewById(R.id.restaurant_list);

        restaurantAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restaurantList);

        restaurantLv.setAdapter(restaurantAdapter);
    }


}
