package edu.rollins.foxbook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ClubListActivity extends AppCompatActivity {
	//class for displaying all clubs within a listView

    private static final String TAG = "AccountListActivity";
    ClubDatabaseHelper clubDatabaseHelper;
    private ListView listView;

    @Override
    public void onBackPressed() {
		//back button takes user to the homepage, preserves user as extra intent
        if (getIntent().getStringExtra("user") != null) {
            String u = getIntent().getStringExtra("user");
            Intent intent = new Intent(ClubListActivity.this, Homepage.class);
            intent.putExtra("user", u);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountlist);	//links .java file to the activity_accountlist.xml
        listView = (ListView) findViewById(R.id.listView);
        clubDatabaseHelper = SplashActivity.getCDB();	//gets the club database

        // Setting title of action bar
        getSupportActionBar().setTitle("Select A Club");

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in ListView.");

        Cursor data = clubDatabaseHelper.getAllClubData();
        ArrayList<String> dataList = new ArrayList<>();

        //displays list of every club name within the database
        while (data.moveToNext()) {
            dataList.add(data.getString(0));
        }

		//adapter populates listView with all the clubs
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        //on click listener for selecting clubs
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clubName = (String) parent.getItemAtPosition(position);

				//sends user to the selected club's page, preserving club name and which student user
                Intent intent = new Intent(ClubListActivity.this, Club.class);
                intent.putExtra("studentView", clubName);
                if (getIntent().getStringExtra("user") != null) {
                    String u = getIntent().getStringExtra("user");
                    intent.putExtra("user", u);
                }
                startActivity(intent);
            }
        });
    }
}