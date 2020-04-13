package edu.rollins.foxbook;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreateClubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_clubs);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Setting title of action bar
        getSupportActionBar().setTitle("Create A Club");

        // CAITLIN: I saw you were on your way to implementing image support and uploading, so I left a blank space above the name for the user to select that.
        // I'm leaving it for you to implement, since you were on that track for the Clubs page itself.
        // - Neil
        // Think I'm going to merge this with registration? Like, turn this into how to create a club for login purposes as opposed to the hard-coded clubs or drop-down club list.
        // - Caitlin

        Button createClubButton = (Button) findViewById(R.id.createClubButton);
        createClubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i = 0;
            }
        });
    }
}