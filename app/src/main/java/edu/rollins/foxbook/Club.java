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
    DatabaseHelper databaseHelper;
    TextView name, eventsList;
    EditText contactEmail, bio;
    ImageView image;
    boolean clubView;   //boolean which determines if user viewing the page is a student or the club itself
    boolean following;  //boolean which shows whether the student user follows the club
    Button editPage, eventButton, logoutButton;

    String savedExtra;

    @Override
    public void onBackPressed() {
        if(clubView) {
            int a = 0;
        } else {
            if (getIntent().getStringExtra("user") != null) {
                String u = getIntent().getStringExtra("user");
                Intent intent = new Intent(Club.this, Homepage.class);
                intent.putExtra("user", u);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        databaseHelper = new DatabaseHelper(this);

        //ensures the keyboard only appears in editting mode
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //clubDB call
        myDB = SplashActivity.getDB();
        myCDB = SplashActivity.getCDB();

        name = (TextView) findViewById(R.id.club_name);
        contactEmail = (EditText) findViewById(R.id.contact_email);
        contactEmail.setEnabled(false); //causes the EditText field to appear as a TextView outside of editting mode
        bio = (EditText) findViewById(R.id.club_bio);
        bio.setEnabled(false);
        image = (ImageView) findViewById(R.id.club_image);
        eventsList = (TextView) findViewById(R.id.club_events_list);
        editPage = (Button) findViewById(R.id.edit_page);
        eventButton = (Button) findViewById(R.id.event_button);
        logoutButton = (Button) findViewById(R.id.return_btn);

        //this conditional determines whether the page was reached via a club logging in or not
        if (getIntent().getStringExtra("club") != null) {
            savedExtra = getIntent().getStringExtra("club");
            name.setText(savedExtra);
            editPage.setVisibility(View.VISIBLE);   //allows club user to set and select edit_page button
            clubView = true;
        } else {
            savedExtra = getIntent().getStringExtra("studentView");
            name.setText(savedExtra);
            String u = getIntent().getStringExtra("user");
            String[] userInfo = u.split(" ");
            following = databaseHelper.isFollowing(userInfo[0], userInfo[1], savedExtra);
            eventButton.setText((following ? "Unfollow" : "Subscribe to Events"));
            logoutButton.setVisibility(View.GONE); //prevents student users from selecting the logout button
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
                    logoutButton.setText("Save");
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

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(logoutButton.getText().equals("Save")) {
                        //updates club db by gettingText from email and bio
                        myCDB.updateData(name.getText().toString(), contactEmail.getText().toString(), bio.getText().toString(), "");

                        //resets button labels and text field restrictions for viewing mode
                        contactEmail.setEnabled(false);
                        bio.setEnabled(false);
                        logoutButton.setText("Logout");
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
                    if (getIntent().getStringExtra("user") != null) {
                        String u = getIntent().getStringExtra("user");
                        String[] userInfo = u.split(" ");
                        String user = userInfo[0];
                        String password = userInfo[1];

                        if(following) {
                            databaseHelper.unfollowClub(user, password, name.getText().toString());
                            Toast.makeText(Club.this, "You have unfollowed " + name.getText().toString(), Toast.LENGTH_LONG).show();
                            eventButton.setText("Subscribe to Events");
                        } else {
                            databaseHelper.followClub(user, password, name.getText().toString());
                            Toast.makeText(Club.this, "You now follow " + name.getText().toString(), Toast.LENGTH_LONG).show();
                            eventButton.setText("Unfollow");
                        }
                    }
                }
            });
        }

    }
}
