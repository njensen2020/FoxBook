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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final UserStorage userStorage = new UserStorage(this);

        // Setting title of action bar
        getSupportActionBar().setTitle("Sign In");

        // Setting login button to test for hardcoded password, actual password implementation coming soon
        Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView errText = (TextView) findViewById(R.id.errorTextView);
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
                    User user = new User(username, password);
                    userStorage.storeUserData(user);
                    userStorage.setLoggedInUser(true);
                    startActivity(new Intent(MainActivity.this, Homepage.class));
                }
            }
        });
    }
}
