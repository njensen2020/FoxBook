package edu.rollins.foxbook;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
	//class for registering a new student user
	
    private DatabaseReference mDatabase;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);	//links .java file to activity_register.xml
		
        final Button createAccountButton = (Button) findViewById(R.id.createAccountButton);
        final EditText firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        final EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        final EditText pinEditText = (EditText) findViewById(R.id.pinEditText);
        final TextView errText = (TextView) findViewById(R.id.errorTextView);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		//gets Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Setting title of action bar
        getSupportActionBar().setTitle("Create an Account");

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newFirst = firstNameEditText.getText().toString();
                String newLast = lastNameEditText.getText().toString();
                String newUsername = usernameEditText.getText().toString();
                String newPassword = passwordEditText.getText().toString();
                String newPIN = pinEditText.getText().toString();

                if (newPassword.length() >= 7) {
                    if (newPIN.length() == 6) {
                        addData(newFirst, newLast, newUsername, newPassword, newPIN);	//call to addData method
						//sends student user to the homepage
                        Intent intent = new Intent(RegisterActivity.this, Homepage.class);
                        String user = newUsername + " " + newPassword;
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                    else{
                        String pinError = "ERROR: Incorrect PIN Length";
                        errText.setText(pinError);
                    }
                }
                else {
                    String passwordError = "ERROR: Incorrect Password Length";
                    errText.setText(passwordError);
                }
            }
        });
    }

    public void addData(String first, String last, String username, String password, String pin) {
        //generate unique id from timestamp
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String id = s.format(new Date());

        //adds to SQLite database
        boolean insertData = databaseHelper.addData(id, first, last, username, password, pin, "Student");

        //adds to Firebase
        DatabaseReference nuRef = mDatabase.child("users").child(id);
        nuRef.child("firstname").setValue(first);
        nuRef.child("lastname").setValue(last);
        nuRef.child("username").setValue(username);
        nuRef.child("password").setValue(password);
        nuRef.child("PIN").setValue(pin);
        nuRef.child("type").setValue("Student");
        nuRef.child("club").setValue("");

        if (insertData) {
            toastMessage("New user registered!");
        }
        else {
            toastMessage("Registration failed.");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
