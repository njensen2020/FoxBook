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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventselect);
        myDB = Event.getDB();

        // Get access to the underlying writeable database
        SQLiteDatabase db = myDB.getWritableDatabase();
        // Query for items from the database and get a cursor back
        Cursor eventCursor = db.rawQuery("SELECT  * FROM event_table", null);

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
           public void onItemClick(AdapterView<?> adapter, View v, int position, long l) {
               //figure out what long is for and what ItemClicked should be (Event obj?)
               Event item = (Event) adapter.getItemAtPosition(position);

               Intent intent = new Intent(EventSelection.this, Event.class);
               //based on item add into to intent????
               startActivity(intent);
           }
        });
    }
}

