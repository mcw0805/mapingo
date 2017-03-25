package com.debkbanerji.mapingo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MakeOrderActivity extends AppCompatActivity {

    private ListView menuLv;
    private ListView orderLv;
    private List<String> menuItems;
    private List<String> orderItems;
    private ArrayAdapter<String> menuAdapter;
    private ArrayAdapter<String> orderAdapter;
    private Button submitOrderButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);

        menuItems = new ArrayList<>();
        menuItems.add("stuff1");
        menuItems.add("stuff2");
        menuItems.add("stuff2");
        menuItems.add("stuff2");
        menuItems.add("stuff2");
        menuItems.add("stuff2");

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


    }

    private void submitOrder() {
        Intent submitOrderIntent = new Intent(MakeOrderActivity.this, OrderConfirmationActivity.class);
        startActivity(submitOrderIntent);
    }
}