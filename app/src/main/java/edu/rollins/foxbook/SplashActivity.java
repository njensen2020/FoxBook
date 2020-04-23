package edu.rollins.foxbook;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
	//the class for the opening screen of FoxBook
	
    static EventDatabaseHelper eventHelper;     //event SQLite database
    static ClubDatabaseHelper clubHelper;       //club SQLite database
    DatabaseHelper databaseHelper = new DatabaseHelper(this);   //account SQLite database
    private DatabaseReference mDatabase;        //firebase database

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

        //retrieve instance of Firebase DatabaseReference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //populate SQLite databases from Firebase
        //account population
        DatabaseReference accRef = mDatabase.child("users");
        accRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()) { //user key level
                    String id = databaseHelper.getID((String)snap.child("username").getValue(), (String)snap.child("password").getValue());
                    if(!(databaseHelper.inDatabase(id))) { //checks if user is already in SQLite database
                        databaseHelper.addData((String)snap.getKey(), (String)snap.child("firstname").getValue(), (String)snap.child("lastname").getValue(), (String)snap.child("username").getValue(), (String)snap.child("password").getValue(), (String)snap.child("PIN").getValue(), (String)snap.child("type").getValue());
                    } else {    //if already in SQLite, update to ensure most current version
                        databaseHelper.updateData(id, (String)snap.child("firstname").getValue(), (String)snap.child("lastname").getValue(), (String)snap.child("username").getValue(), (String)snap.child("password").getValue(), (String)snap.child("PIN").getValue(), (String)snap.child("type").getValue(), (String)snap.child("club").getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //event population
        DatabaseReference evRef = mDatabase.child("events");
        evRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()) {   //event key level
                    String id = eventHelper.getID((String)snap.child("club").getValue(), (String)snap.child("title").getValue(), (String)snap.child("time").getValue(), (String)snap.child("date").getValue());
                    if(!(eventHelper.inDatabase(id))) {    //checks if event is already in SQLite database
                        eventHelper.insertData((String) snap.getKey(), (String) snap.child("date").getValue(), (String) snap.child("time").getValue(), (String) snap.child("club").getValue(), (String) snap.child("title").getValue(), (String) snap.child("location").getValue(), (String) snap.child("description").getValue(), (String) snap.child("filter").getValue());
                    } else {    //if already in SQLite database, update to ensure most current values
                        eventHelper.updateData(id, (String) snap.child("date").getValue(), (String) snap.child("time").getValue(), (String) snap.child("title").getValue(), (String) snap.child("location").getValue(), (String) snap.child("description").getValue(), (String) snap.child("filter").getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //club population
        final DatabaseReference clubRef = mDatabase.child("clubs");
        clubRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()) {   //club key level
                    if(!(clubHelper.inDatabase((String)snap.child("name").getValue()))) {   //checks if club is already in SQLite database
                        clubHelper.insertData((String)snap.child("name").getValue(), (String)snap.child("email").getValue(), (String)snap.child("bio").getValue(), "");
                    } else {    //if so, update to ensure most current information
                        clubHelper.updateData((String)snap.child("name").getValue(), (String)snap.child("email").getValue(), (String)snap.child("bio").getValue(), "");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
