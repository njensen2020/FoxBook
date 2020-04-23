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

public class AccountListActivity extends AppCompatActivity {
	//class used to populate listview with a student user's followed clubs

    private static final String TAG = "AccountListActivity";
    DatabaseHelper databaseHelper;
    private ListView listView;
    String user, password;

    @Override
    public void onBackPressed() {
		//when selecting the back button, user returns to the homepage
        if (getIntent().getStringExtra("user") != null) {
            String u = getIntent().getStringExtra("user");
            Intent intent = new Intent(AccountListActivity.this, Homepage.class);
            intent.putExtra("user", u);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountlist); 	//links this .java file with activity_accountlist.xml
        listView = (ListView) findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);		//connects this file with the user account SQL database

		//ensures page is reached properly with added intent "user"
		//"user" holds the currently logged in user's username and password
        if (getIntent().getStringExtra("user") != null) {
            String u = getIntent().getStringExtra("user");
            String[] userInfo = u.split(" ");
            user = userInfo[0];
            password = userInfo[1];
        }
        populateListView();

        // Setting title of action bar
        getSupportActionBar().setTitle("My Clubs");

    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in ListView.");

		//call to the user SQL database to get all the clubs followed by the user
        Cursor data = databaseHelper.getClubsFollowed(user, password);
        ArrayList<String> dataList = new ArrayList<>();
        data.moveToFirst();	//cursor objects must moveToFirst before use because they start at index -1 and need to be moved to index 0 to access information
        String c = data.getString(0); 	//since there is only one user calling the method, only the data at index 0 is needed
        String[] clubs = c.split("_"); 	//clubs followed are stored in a string with each club separated by an underscore
        for(String a : clubs) {
            dataList.add(a);
        }

		//adapter populates listView with the contents of the ArrayList dataList
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        //on click listener for selecting clubs
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clubName = (String) parent.getItemAtPosition(position);

				//get the name of the selected club to properly populate the Club page
				//get the username and password of the user to send to Club page as well
				//these extra intents ensure the correct Club is loaded and the follow/unfollow button displays the correct option
                Intent intent = new Intent(AccountListActivity.this, Club.class);
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
