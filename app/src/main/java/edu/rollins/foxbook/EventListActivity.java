package edu.rollins.foxbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EventListActivity extends AppCompatActivity {
    EventDatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventlist);
        myDB = SplashActivity.getDB();

        // Get access to the underlying writeable database
        SQLiteDatabase db = myDB.getWritableDatabase();
        // Query for items from the database and get a cursor back
        Cursor eventCursor = db.rawQuery("SELECT rowid _id, * FROM event_table", null);

        // Find ListView to populate
        ListView lvItems = (ListView) findViewById(R.id.lvItems);
        // Setup cursor adapter using cursor from last step
        EventCursorAdapter eventAdapter = new EventCursorAdapter(this, eventCursor);
        // Attach cursor adapter to the ListView
        lvItems.setAdapter(eventAdapter);

        // Switch to new cursor and update contents of ListView
        eventAdapter.changeCursor(eventCursor);

        //on click listener for selecting events
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Cursor item = (Cursor) adapter.getItemAtPosition(position);
                String eventID = item.getString(item.getColumnIndexOrThrow("ID"));
                String title = item.getString(item.getColumnIndexOrThrow("TITLE"));
                String date = item.getString(item.getColumnIndexOrThrow("DATE"));
                String time = item.getString(item.getColumnIndexOrThrow("TIME"));
                String location = item.getString(item.getColumnIndexOrThrow("LOCATION"));
                String description = item.getString(item.getColumnIndexOrThrow("DESCRIPTION"));
                String club = item.getString(item.getColumnIndexOrThrow("CLUB"));
                String filter = item.getString(item.getColumnIndexOrThrow("FILTER"));

                AlertDialog.Builder builder = new AlertDialog.Builder(EventListActivity.this);
                StringBuffer buff = new StringBuffer();
                buff.append(club + "\n");
                buff.append("Date: " + date + "\n");
                buff.append("Time: " + time + "\n");
                buff.append("Location: " + location + "\n");
                buff.append("Filter: " + filter + "\n");
                buff.append("Description: " + description + "\n\n");

                builder.setTitle(title).setMessage(buff.toString());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
