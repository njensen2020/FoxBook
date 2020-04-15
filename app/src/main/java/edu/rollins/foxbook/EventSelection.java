package edu.rollins.foxbook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class EventSelection extends AppCompatActivity {
    static EventDatabaseHelper myDB;
    String clubName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventselect);
        myDB = MainActivity.getDB();

        // Get access to the underlying writable database
        SQLiteDatabase db = myDB.getWritableDatabase();
        Cursor eventCursor;

        if(getIntent().getStringExtra("club") != null) {
            clubName = getIntent().getStringExtra("club");
            //Query database for club specific items, get a cursor back
            eventCursor = db.rawQuery("SELECT rowid _id, * FROM event_table WHERE club LIKE \'" + clubName + "\'", null);
        } else {
            // Query for items from the database and get a cursor back
            eventCursor = db.rawQuery("SELECT rowid _id, * FROM event_table", null);
        }

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

               Intent intent = new Intent(EventSelection.this, Event.class);
               intent.putExtra("event", eventID);
               startActivity(intent);
           }
        });
    }
}

