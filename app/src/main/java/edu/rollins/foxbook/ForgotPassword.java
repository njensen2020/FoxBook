package edu.rollins.foxbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        // Setting title of action bar
        getSupportActionBar().setTitle("Reset Password");

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
                    databaseHelper.updatePassword(password, pin);
                    startActivity(new Intent(ForgotPassword.this, Homepage.class));
                }
            }
        });
    }
}
