package edu.rollins.foxbook;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    static EventDatabaseHelper eventHelper;
    static ClubDatabaseHelper clubHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        eventHelper = new EventDatabaseHelper(this);
        clubHelper = new ClubDatabaseHelper(this);

        // Setting title of action bar
        getSupportActionBar().setTitle("Sign In");

        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button resetPasswordButton = (Button) findViewById(R.id.forgotPasswordButton);
        final TextView errText = (TextView) findViewById(R.id.errorTextView);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Logging user in
                String username = ((EditText)findViewById(R.id.usernameEditText)).getText().toString();
                String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();

                // Password too short
                if(password.length() <= 7) {
                    String err = "ERROR: Invalid login credentials";
                    errText.setText(err);
                }

                // Logging user in, temporary until persistent storage is added
                else {
                    Boolean userExists = databaseHelper.checkLogin(username, password);

                    if(!userExists) {
                        String err = "ERROR: Invalid login credentials";
                        errText.setText(err);
                    }
                    else {
                        boolean clubUser = databaseHelper.isClub(username, password);
                        if(clubUser) {
                            Intent intent = new Intent(MainActivity.this, Club.class);
                            intent.putExtra("club", username);
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(MainActivity.this, Homepage.class));
                        }
                    }
                }
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
