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
        final UserStorage userStorage = new UserStorage(this);

        // User loggedInUser = UserStorage.getLoggedInUser();
        // TextView isLoggedIn = (TextView) findViewById(R.id.loggedInTextView);

        // String loggedIn = "Logged In: " + loggedInUser.username;
        // isLoggedIn.setText(loggedIn);

        // Setting title of action bar
        getSupportActionBar().setTitle("Coming Soon...");

        // Setting log out button to direct back to the main activity
        Button logOutButton = (Button) findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Logging user out
                userStorage.clearUserData();
                userStorage.setLoggedInUser(false);
                startActivity(new Intent(Homepage.this, SplashActivity.class));
            }
        });
    }
}
