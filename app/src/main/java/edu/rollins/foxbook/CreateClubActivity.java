package edu.rollins.foxbook;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateClubActivity extends AppCompatActivity {
	//class which acts as the registration page for club accounts
	
    DatabaseReference mDatabase;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    ClubDatabaseHelper clubHelper;
    EditText name, email, bio, password, pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_clubs); //links .java file with activity_create_clubs.xml
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //locks the screen in portrait mode

        // Setting title of action bar
        getSupportActionBar().setTitle("Create A Club");

        //get Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

		//keeps the keyboard from being displayed until an editing field is selected
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        name = (EditText) findViewById(R.id.nameEditText);
        email = (EditText) findViewById(R.id.email);
        bio = (EditText) findViewById(R.id.bioEditText);
        password = (EditText) findViewById(R.id.password);
        pin = (EditText) findViewById(R.id.pin_number);

        clubHelper = SplashActivity.getCDB(); //gets club SQL database

        Button createClubButton = (Button) findViewById(R.id.createClubButton);
		
        createClubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(password.getText().toString().length() >= 7) {
                    if (pin.getText().toString().length() == 6) {
                        //generate unique id from timestamp
                        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                        String id = s.format(new Date());

                        //add data to SQLite database
                        boolean insertData = databaseHelper.addData(id, "x", "x", name.getText().toString(), password.getText().toString(), pin.getText().toString(), "Club");
                        boolean insertClub = clubHelper.insertData(name.getText().toString(), email.getText().toString(), bio.getText().toString(), "x");

                        //add data to Firebase under users, then under clubs
                        DatabaseReference nuRef = mDatabase.child("users").child(id);
                        nuRef.child("firstname").setValue("x"); //clubs do not have a firstname or lastname, so x's are used as placeholders
                        nuRef.child("lastname").setValue("x");
                        nuRef.child("username").setValue(name.getText().toString());
                        nuRef.child("password").setValue(password.getText().toString());
                        nuRef.child("PIN").setValue(pin.getText().toString());
                        nuRef.child("type").setValue("Club");

                        DatabaseReference ncRef = mDatabase.child("clubs").child(name.getText().toString());
                        ncRef.child("name").setValue(name.getText().toString());
                        ncRef.child("email").setValue(email.getText().toString());
                        ncRef.child("bio").setValue(bio.getText().toString());


                        if(insertData && insertClub) {
                            Toast.makeText(CreateClubActivity.this, "New club registered", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateClubActivity.this, "Club registration failed", Toast.LENGTH_LONG).show();
                        }

                        //start club activity
                        Intent intent = new Intent(CreateClubActivity.this, Club.class);
                        intent.putExtra("club", name.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(CreateClubActivity.this, "Incorrect PIN Length", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CreateClubActivity.this, "Incorrect Password Length", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}