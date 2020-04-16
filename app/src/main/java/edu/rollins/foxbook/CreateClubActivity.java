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

public class CreateClubActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    ClubDatabaseHelper clubHelper;
    EditText name, email, bio, password, pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_clubs);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Setting title of action bar
        getSupportActionBar().setTitle("Create A Club");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        name = (EditText) findViewById(R.id.nameEditText);
        email = (EditText) findViewById(R.id.email);
        bio = (EditText) findViewById(R.id.bioEditText);
        password = (EditText) findViewById(R.id.password);
        pin = (EditText) findViewById(R.id.pin_number);

        clubHelper = SplashActivity.getCDB();


        //public boolean insertData(String name, String email, String bio, String image)

        Button createClubButton = (Button) findViewById(R.id.createClubButton);
        createClubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(password.getText().toString().length() >= 7) {
                    if (pin.getText().toString().length() == 6) {
                        //add Data
                        boolean insertData = databaseHelper.addData("x", "x", name.getText().toString(), password.getText().toString(), pin.getText().toString(), "Club");
                        boolean insertClub = clubHelper.insertData(name.getText().toString(), email.getText().toString(), bio.getText().toString(), "x");

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