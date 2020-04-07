package edu.rollins.foxbook;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteAccount extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteaccount);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        databaseHelper = new DatabaseHelper(this);

        // Setting title of action bar
        getSupportActionBar().setTitle("Delete Your Account");

        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        EditText pinEditText = (EditText) findViewById(R.id.pinEditText);
        Button deleteAccountButton = (Button) findViewById(R.id.deleteAccountButton);

        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String pin = pinEditText.getText().toString();

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                databaseHelper.deleteAccount(username, password, pin);
                startActivity(new Intent(DeleteAccount.this, SplashActivity.class));
            }
        });
    }
}