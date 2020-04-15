package edu.rollins.foxbook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Club extends AppCompatActivity {
    //class which displays a club's information into the club bio page template
    EventDatabaseHelper myDB;
    ClubDatabaseHelper myCDB;
    TextView name, eventsList;
    EditText contactEmail, bio;
    ImageView image;
    boolean clubView;   //boolean which determines if user viewing the page is a student or the club itself
    Button editPage, eventButton, logout_scrollupButton;

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

        //ensures the keyboard only appears in editting mode
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //clubDB call
        myDB = MainActivity.getDB();
        myCDB = MainActivity.getCDB();

        name = (TextView) findViewById(R.id.club_name);
        contactEmail = (EditText) findViewById(R.id.contact_email);
        contactEmail.setEnabled(false); //causes the EditText field to appear as a TextView outside of editting mode
        bio = (EditText) findViewById(R.id.club_bio);
        bio.setEnabled(false);
        image = (ImageView) findViewById(R.id.club_image);
        eventsList = (TextView) findViewById(R.id.club_events_list);
        editPage = (Button) findViewById(R.id.edit_page);
        eventButton = (Button) findViewById(R.id.event_button);
        logout_scrollupButton = (Button) findViewById(R.id.return_btn);

        //this conditional determines whether the page was reached via a club logging in or not
        if (getIntent().getStringExtra("club") != null) {
            savedExtra = getIntent().getStringExtra("club");
            name.setText(savedExtra);
            editPage.setVisibility(View.VISIBLE);   //allows club user to set and select edit_page button
            clubView = true;
        } else {
            eventButton.setText("Subscribe to Events");
            logout_scrollupButton.setText("Return to Top");
            editPage.setVisibility(View.GONE);  //prevents student user from selecting edit_page button
            clubView = false;
        }

        //fills in fields from club database
        Cursor clubCursor = myCDB.getClubData(name.getText().toString());
        if(clubCursor.getCount() == 0) {
            bio.setText("Failure loading club page");
        } else {
            clubCursor.moveToFirst();
            contactEmail.setText(clubCursor.getString(clubCursor.getColumnIndexOrThrow("EMAIL")));
            bio.setText(clubCursor.getString(clubCursor.getColumnIndexOrThrow("BIO")));
        }

        Cursor res = myDB.getClubSpecificData(name.getText().toString());                //call to query database for all events on selected date
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

        if(clubView) {
            editPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //edit club page
                    contactEmail.setEnabled(true);
                    bio.setEnabled(true);
                    logout_scrollupButton.setText("Save");
                    eventButton.setVisibility(View.GONE);
                }
            });

            //sends club to event page where they can either create new or edit existing events
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Club.this, Event.class);
                    intent.putExtra("club", name.getText().toString());
                    startActivity(intent);
                }
            });

            //does not go here but dear future Caitlin for adding clubs to a student's follow list use a Cursor? As an attribute of the account DB?

            logout_scrollupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(logout_scrollupButton.getText().equals("Save")) {
                        //updates club db by gettingText from email and bio
                        contactEmail.setEnabled(false);
                        bio.setEnabled(false);
                        logout_scrollupButton.setText("Logout");
                        eventButton.setVisibility(View.VISIBLE);
                    } else {
                        startActivity(new Intent(Club.this, SplashActivity.class));
                    }
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
