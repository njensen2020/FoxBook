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

    private static final String TAG = "AccountListActivity";
    DatabaseHelper databaseHelper;
    private ListView listView;
    String user, password;

    @Override
    public void onBackPressed() {
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
        setContentView(R.layout.activity_accountlist);
        listView = (ListView) findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);

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

        //Cursor data = databaseHelper.getData();
        Cursor data = databaseHelper.getClubsFollowed(user, password);
        ArrayList<String> dataList = new ArrayList<>();
        data.moveToFirst();
            //dataList.add(data.getString(1) + " " + data.getString(2) + " (" + data.getString(3) + ", " + data.getString(4) + ")");
        String c = data.getString(0);
        String[] clubs = c.split("_");
        for(String a : clubs) {
            dataList.add(a);
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        //on click listener for selecting clubs
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clubName = (String) parent.getItemAtPosition(position);

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
