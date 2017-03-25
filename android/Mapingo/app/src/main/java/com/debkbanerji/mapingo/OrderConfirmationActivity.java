package com.debkbanerji.mapingo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderConfirmationActivity extends AppCompatActivity {

    private TextView refNumValue;
    private Button finishButton;
    private DatabaseReference mRootRef;
    private DatabaseReference mNumOrdersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

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
}
