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

        // Setting title of action bar
        getSupportActionBar().setTitle("Welcome to FoxBook");

        // Setting registration button to link to registration activity
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        // Setting login button to test for hardcoded password, actual password implementation coming soon
        Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView errText = (TextView) findViewById(R.id.errorTextView);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = ((EditText)findViewById(R.id.usernameEditText)).getText().toString();
                String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();

                if ((username.equals("admin"))&&(password.equals("TarNation"))) {
                    startActivity(new Intent(MainActivity.this, Homepage.class));
                }

                else {
                    String err = "ERROR: Invalid Username/Password";
                    errText.setText(err);
                }
            }
        });
    }
}
