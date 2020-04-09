package edu.rollins.foxbook;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ClubListActivity extends AppCompatActivity {

    private static final String TAG = "AccountListActivity";
    ClubDatabaseHelper clubDatabaseHelper;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountlist);
        listView = (ListView) findViewById(R.id.listView);
        clubDatabaseHelper = new ClubDatabaseHelper(this);

        // Setting title of action bar
        getSupportActionBar().setTitle("Select A Club");

        // Placeholder until ListView functionality is implemented for the clubs
        int i = 0;
    }
}
