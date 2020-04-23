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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Club extends AppCompatActivity {
    //class which displays a club's information into the club bio page template
    DatabaseReference mDatabase;
    EventDatabaseHelper myDB;
    ClubDatabaseHelper myCDB;
    DatabaseHelper databaseHelper;
    TextView name, eventsList;
    EditText contactEmail, bio;
    boolean clubView;   //boolean which determines if user viewing the page is a student or the club itself
    boolean following;  //boolean which shows whether the student user follows the club
    Button editPage, eventButton, logoutButton;

    String savedExtra;

    @Override
    public void onBackPressed() {
		//if user is a club, the back button is disabled because the page is reached through logging in
        if(clubView) {
            int a = 0;
        } else { //if user is a student, the back button takes them to Homepage with the extra intent of the user
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
        setContentView(R.layout.activity_club);		//links .java file with activity_club.xml
        databaseHelper = new DatabaseHelper(this);	//holds information in user account SQL database

        //ensures the keyboard only appears in editing mode
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //SQLite database calls
        myDB = SplashActivity.getDB();
        myCDB = SplashActivity.getCDB();

        //retrieve instance of Firebase DatabaseReference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name = (TextView) findViewById(R.id.club_name);
        contactEmail = (EditText) findViewById(R.id.contact_email);
        contactEmail.setEnabled(false); //causes the EditText field to appear as a TextView outside of editing mode
        bio = (EditText) findViewById(R.id.club_bio);
        bio.setEnabled(false);
        eventsList = (TextView) findViewById(R.id.club_events_list);
        editPage = (Button) findViewById(R.id.edit_page);
        eventButton = (Button) findViewById(R.id.event_button);
        logoutButton = (Button) findViewById(R.id.return_btn);

        //this conditional determines whether the page was reached via a club logging in or not
        if (getIntent().getStringExtra("club") != null) {
            savedExtra = getIntent().getStringExtra("club");
            name.setText(savedExtra);
            editPage.setVisibility(View.VISIBLE);   //allows club user to see and select edit_page button
            clubView = true;
        } else {
            savedExtra = getIntent().getStringExtra("studentView");
            name.setText(savedExtra);
            String u = getIntent().getStringExtra("user");
            String[] userInfo = u.split(" ");
            following = databaseHelper.isFollowing(userInfo[0], userInfo[1], savedExtra);
            eventButton.setText((following ? "Unfollow" : "Subscribe to Events"));	//changes eventButton into a follow/unfollow button
            logoutButton.setVisibility(View.GONE); //prevents student users from seeing or selecting the logout button
            editPage.setVisibility(View.GONE);  //prevents student user from seeing or selecting edit_page button
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

        Cursor res = myDB.getClubSpecificData(name.getText().toString());               //call to query database for all events from this club
        if (res.getCount() == 0) {                                                      //conditional to throw 'no events' message if there aren't any events in the database for given club
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
                    contactEmail.setEnabled(true);			//enables editing of the editTexts
                    bio.setEnabled(true);
                    logoutButton.setText("Save");			//in editing mode, logout button becomes a save button
                    eventButton.setVisibility(View.GONE);	//causes the event management button to disappear
					editPage.setVisibility(View.GONE);		//causes the edit button to disappear
                }
            });

            //sends club to event page where they can either create new or edit existing events
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Club.this, Event.class);
                    intent.putExtra("club", name.getText().toString());	//sends the club name so events are linked to the proper club
                    startActivity(intent);
                }
            });

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(logoutButton.getText().equals("Save")) {		//this means user is in editing mode
                        //updates club SQL db by gettingText from email and bio
                        myCDB.updateData(name.getText().toString(), contactEmail.getText().toString(), bio.getText().toString(), "");
                        //updates club in firebase
                        mDatabase.child("clubs").child(name.getText().toString()).child("email").setValue(contactEmail.getText().toString());
                        mDatabase.child("clubs").child(name.getText().toString()).child("bio").setValue(bio.getText().toString());

                        //resets button labels and text field restrictions for viewing mode
                        contactEmail.setEnabled(false);
                        bio.setEnabled(false);
                        logoutButton.setText("Logout");
                        eventButton.setVisibility(View.VISIBLE);
						editPage.setVisibility(View.VISIBLE);
                    } else { //logs club out, returning them to splash page
                        startActivity(new Intent(Club.this, SplashActivity.class));
                    }
                }
            });
        } else {	//viewing page as student user
            eventButton.setOnClickListener(new View.OnClickListener() {	//for students, event button acts as a follow/unfollow button
                @Override
                public void onClick(View v) {
                    if (getIntent().getStringExtra("user") != null) {
                        String u = getIntent().getStringExtra("user");
                        String[] userInfo = u.split(" ");
                        String user = userInfo[0];
                        String password = userInfo[1];

                        if(following) {
							//call to unfollow within SQL database
                            databaseHelper.unfollowClub(user, password, name.getText().toString());

                            //update Firebase
                            Cursor res = databaseHelper.getClubsFollowed(user, password);
                            String c = "";
                            if(res.getCount() != 0) {
                                res.moveToFirst();
                                c = res.getString(0);
                            }
                            mDatabase.child("users").child(databaseHelper.getID(user, password)).child("club").setValue(c);

							//sends confirmation toast message
                            Toast.makeText(Club.this, "You have unfollowed " + name.getText().toString(), Toast.LENGTH_LONG).show();
                            eventButton.setText("Subscribe to Events");
                            following = false;
                        } else {
							//call to follow within SQL database
                            databaseHelper.followClub(user, password, name.getText().toString());

                            //update Firebase
                            Cursor res = databaseHelper.getClubsFollowed(user, password);
                            String c = "";
                            if(res.getCount() != 0) {
                                res.moveToFirst();
                                c = res.getString(0);
                            }
                            mDatabase.child("users").child(databaseHelper.getID(user, password)).child("club").setValue(c);

							//sends confirmation toast message
                            Toast.makeText(Club.this, "You now follow " + name.getText().toString(), Toast.LENGTH_LONG).show();
                            eventButton.setText("Unfollow");
                            following = true;
                        }
                    }
                }
            });
        }

    }
}
