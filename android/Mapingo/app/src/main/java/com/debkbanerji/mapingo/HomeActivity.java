package com.debkbanerji.mapingo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    //private DatabaseReference mRootReef;
    private Button makeOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        mRootReef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference mTestRef = mRootReef.child("test");
//        mTestRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("TESTING", dataSnapshot.getValue().toString());
//                Toast.makeText(getBaseContext(), dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        makeOrderButton = (Button) findViewById(R.id.make_order_button);
        makeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOrder();

            }
        });


    }

    // OPqiWs6UXmffghgHcJDXVgGOmC92
    private void makeOrder() {
        Intent makeOrderIntent = new Intent(HomeActivity.this, MakeOrderActivity.class);
        makeOrderIntent.putExtra("storeUID", "OPqiWs6UXmffghgHcJDXVgGOmC92");
        startActivity(makeOrderIntent);

    }

}
