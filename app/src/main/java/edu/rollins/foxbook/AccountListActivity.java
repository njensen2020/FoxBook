package edu.rollins.foxbook;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountlist);
        listView = (ListView) findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);

        populateListView();

        // Setting title of action bar
        getSupportActionBar().setTitle("User Accounts");

    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in ListView.");

        Cursor data = databaseHelper.getData();
        ArrayList<String> dataList = new ArrayList<>();
        while (data.moveToNext()) {
            dataList.add(data.getString(1) + " " + data.getString(2) + " (" + data.getString(3) + ", " + data.getString(4) + ")");
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
    }
}
