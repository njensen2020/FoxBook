package edu.rollins.foxbook;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Club extends AppCompatActivity {
    //class which displays a club's information into the club bio page template
    TextView name, contactEmail, bio;
    ImageView image;
    ListView eventsList;

    String savedExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        name = (TextView) findViewById(R.id.club_name);
        contactEmail = (TextView) findViewById(R.id.contact_email);
        bio = (TextView) findViewById(R.id.club_bio);
        image = (ImageView) findViewById(R.id.club_image);
        eventsList = (ListView) findViewById(R.id.club_events_list);


        //this conditional will receive the club and event info from the databases and fill in the boxes appropriately
        if (getIntent().getStringExtra("club") != null) {
            savedExtra = getIntent().getStringExtra("club");
            //now to parse . . . but how????
            bio.setText(savedExtra);
        }
    }
}
