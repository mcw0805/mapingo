package com.debkbanerji.mapingo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    private Button backHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        backHomeButton = (Button) findViewById(R.id.about_button);
        backHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackHome();

            }
        });
    }

    private void goBackHome() {
        Intent goBackHomeIntent = new Intent(AboutActivity.this, HomeActivity.class);
        startActivity(goBackHomeIntent);
    }
}
