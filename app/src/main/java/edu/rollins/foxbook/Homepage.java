package edu.rollins.foxbook;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Homepage extends AppCompatActivity {

    // Disabling the back button
    @Override
    public void onBackPressed() {
        int a = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Setting title of action bar
        getSupportActionBar().setTitle("Home");

        //Button addEventButton = (Button) findViewById(R.id.addEventButton);
        //addEventButton.setOnClickListener(new View.OnClickListener() {
            //public void onClick(View v) {
          //      startActivity(new Intent(Homepage.this, Event.class));
        //    }
        //});

        Button calendarButton = (Button) findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this, CalendarActivity.class));
            }
        });

        // Button to take the user to account credentials page
        Button viewAccountButton = (Button) findViewById(R.id.viewAccountButton);
        viewAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Taking user to new activity
                startActivity(new Intent(Homepage.this, AccountListActivity.class));
            }
        });

        //right now this button crashes app because ClubListActivity is not implemented yet
        Button viewClubButton = (Button) findViewById(R.id.viewClubButton);
        viewClubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this, ClubListActivity.class));
            }
        });

        Button deleteAccountButton = (Button) findViewById(R.id.deleteAccountButton);
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Taking user to the delete account page
                startActivity(new Intent(Homepage.this, DeleteAccount.class));
            }
        });

        // Setting log out button to direct back to the main activity
        Button logOutButton = (Button) findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Logging user out
                startActivity(new Intent(Homepage.this, SplashActivity.class));
            }
        });
    }
}
