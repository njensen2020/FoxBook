package edu.rollins.foxbook;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Setting title of action bar
        getSupportActionBar().setTitle("Create an Account");

        // Setting registration button to link to registration activity
        Button createAccountButton = (Button) findViewById(R.id.createAccountButton);
        final TextView errText = (TextView) findViewById(R.id.errorTextView);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText)findViewById(R.id.usernameEditText)).getText().toString();
                String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();

                if (password.length() < 7) {
                    String err = "ERROR: Password Too Short";
                    errText.setText(err);
                }
                else {
                    startActivity(new Intent(RegisterActivity.this, Homepage.class));
                }
            }
        });
    }
}
