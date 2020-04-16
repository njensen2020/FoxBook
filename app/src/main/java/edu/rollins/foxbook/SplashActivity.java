package edu.rollins.foxbook;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    static EventDatabaseHelper eventHelper;
    static ClubDatabaseHelper clubHelper;

    // Disabling the back button
    @Override
    public void onBackPressed() {
        int a = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        eventHelper = new EventDatabaseHelper(this);
        clubHelper = new ClubDatabaseHelper(this);

        Button registerButton = (Button) findViewById(R.id.forgotPasswordButton);
        Button loginButton = (Button) findViewById(R.id.loginButton);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Setting title of action bar
        getSupportActionBar().setTitle("");

        // Setting registration button to link to registration page
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
            }
        });

        // Setting club creation button to link to club creation page
        Button createClubButton = (Button) findViewById(R.id.createClubButton);
        createClubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, CreateClubActivity.class));
            }
        });

        // Setting login button to link to login page (MainActivity)
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });
    }

    public static EventDatabaseHelper getDB() {
        return eventHelper;
    }

    public static ClubDatabaseHelper getCDB() {
        return clubHelper;
    }
}
