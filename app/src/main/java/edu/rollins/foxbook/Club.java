package edu.rollins.foxbook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Club extends AppCompatActivity {
    //class which displays a club's information into the club bio page template
    EventDatabaseHelper myDB;
    TextView name, contactEmail, bio, eventsList;
    ImageView image;
    boolean clubView;   //boolean which determines if user viewing the page is a student or the club itself
    Button eventButton, logout_scrollupButton;

    String savedExtra;

    @Override
    public void onBackPressed() {
        if(clubView) {
            int a = 0;
        } else {
            startActivity(new Intent( Club.this, Homepage.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        myDB = MainActivity.getDB();

        name = (TextView) findViewById(R.id.club_name);
        contactEmail = (TextView) findViewById(R.id.contact_email);
        bio = (TextView) findViewById(R.id.club_bio);
        image = (ImageView) findViewById(R.id.club_image);
        eventsList = (TextView) findViewById(R.id.club_events_list);
        eventButton = (Button) findViewById(R.id.event_button);
        logout_scrollupButton = (Button) findViewById(R.id.return_btn);

        //this conditional determines whether the page was reached via a club logging in or not
        if (getIntent().getStringExtra("club") != null) {
            savedExtra = getIntent().getStringExtra("club");
            name.setText(savedExtra);
            clubView = true;
        } else {
            eventButton.setText("Subscribe to Events");
            logout_scrollupButton.setText("Return to Top");
            clubView = false;
        }

        Cursor res = myDB.getClubSpecificData(name.getText().toString());                                    //call to query database for all events on selected date
        if (res.getCount() == 0) {                                                      //conditional to throw 'no events' message if there aren't any events in the database for given date
            eventsList.setText("No upcoming events");
        } else {
            StringBuffer buff = new StringBuffer();                                     //start of process of taking returned database information and turning it into a readable string for printing
            while (res.moveToNext()) {
                buff.append("Date: " + res.getString(0) + "\n");
                buff.append("Time: " + res.getString(1) + "\n");
                buff.append("Title: " + res.getString(3) + "\n");
                buff.append("Location: " + res.getString(4) + "\n");
                buff.append("Description: " + res.getString(5) + "\n\n");
            }
            eventsList.setText(buff.toString()); //format for readability
        }

        /*if(getIntent().getStringExtra("event") != null) {
            savedExtra = getIntent().getStringExtra("event");   //savedExtra holds an event's ID passed via EventSelection
            Cursor eventCursor = myDB.getIDSpecificData(savedExtra);
            if(eventCursor.getCount() == 0) {
                editDescription.setText("Editor failure");
            } else {
                eventCursor.moveToFirst(); //necessary because cursors automatically start at index -1, which does not hold data
                editDate.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("DATE")));
                editTitle.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("TITLE")));
                editTime.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("TIME")));
                editLocation.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("LOCATION")));
                editDescription.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("DESCRIPTION")));
            }
            editor = true;
        } else {
            savedExtra = "";
            editor = false;*/

        if(clubView) {
            //sends club to event page where they can either create new or edit existing events
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Club.this, Event.class);
                    intent.putExtra("club", name.getText().toString());
                    startActivity(intent);
                    //startActivity(new Intent( Club.this, Event.class));
                }
            });

            logout_scrollupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Club.this, SplashActivity.class));
                }
            });
        } else {
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //code for following to come . . .
                    Toast.makeText(Club.this, "You now follow " + name.getText().toString(), Toast.LENGTH_LONG).show();
                }
            });

            logout_scrollupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add functionality to scroll back up to top of page
                }
            });
        }

    }
}
