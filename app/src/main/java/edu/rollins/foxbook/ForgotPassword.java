package edu.rollins.foxbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPassword extends AppCompatActivity {
	//class which allows user to change their password

    private DatabaseReference mDatabase;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);	//links .java file with activity_forgotpassword.xml

        // Setting title of action bar
        getSupportActionBar().setTitle("Reset Password");

		//get Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initializing elements
        Button submitButton = (Button) findViewById(R.id.submitButton);
        EditText pinEditText = (EditText) findViewById(R.id.pinEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.newPasswordText);
        final TextView errText = (TextView) findViewById(R.id.errorTextView);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Getting entered credentials
                String password = ((EditText)findViewById(R.id.newPasswordText)).getText().toString();
                String pin = ((EditText)findViewById(R.id.pinEditText)).getText().toString();

                // New password is too short
                if(password.length() <= 7) {
                    String err = "ERROR: New password too short";
                    errText.setText(err);
                }

                // Password is long enough
                else {
                    //updates SQLite database
                    String info = databaseHelper.updatePassword(password, pin);
                    String[] account = info.split("_");

                    //updates Firebase
                    mDatabase.child("users").child(account[0]).child("password").setValue(password);

                    if(account[1].equals("Club")) {	//sends club users to their club page
                        Intent intent = new Intent(ForgotPassword.this, Club.class);
                        intent.putExtra("club", account[2]);
                        startActivity(intent);
                    } else {	//student users are sent to the homepage
                        Intent intent = new Intent(ForgotPassword.this, Homepage.class);
                        String user = account[2] + " " + password;
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
