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

public class RegisterActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Button createAccountButton = (Button) findViewById(R.id.createAccountButton);
        final EditText firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        final EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        final EditText pinEditText = (EditText) findViewById(R.id.pinEditText);
        final TextView errText = (TextView) findViewById(R.id.errorTextView);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                        addData(newFirst, newLast, newUsername, newPassword, newPIN);
                        startActivity(new Intent(RegisterActivity.this, Homepage.class));
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
        boolean insertData = databaseHelper.addData(first, last, username, password, pin);

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
