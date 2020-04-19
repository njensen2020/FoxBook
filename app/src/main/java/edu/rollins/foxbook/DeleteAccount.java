package edu.rollins.foxbook;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteAccount extends AppCompatActivity {
    private DatabaseReference mDatabase;
    DatabaseHelper databaseHelper;
    EditText usernameEditText, passwordEditText;

    private static final String TAG = "DeleteAccount";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteaccount);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        databaseHelper = new DatabaseHelper(this);

        // Setting title of action bar
        getSupportActionBar().setTitle("Delete Your Account");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        EditText pinEditText = (EditText) findViewById(R.id.pinEditText);
        Button deleteAccountButton = (Button) findViewById(R.id.deleteAccountButton);

        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String pin = pinEditText.getText().toString();

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = databaseHelper.getID(usernameEditText.getText().toString(), passwordEditText.getText().toString());

                //delete from SQLite
                databaseHelper.deleteAccount(username, password, pin);

                Toast.makeText(DeleteAccount.this, id, Toast.LENGTH_SHORT).show();

                //delete from Firebase
                DatabaseReference delRef = mDatabase.child("users").child(id);
                delRef.removeValue();

                startActivity(new Intent(DeleteAccount.this, SplashActivity.class));
            }
        });
    }
}