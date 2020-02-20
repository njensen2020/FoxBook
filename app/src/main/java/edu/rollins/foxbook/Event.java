package edu.rollins.foxbook;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Event extends AppCompatActivity {
    static EventDatabaseHelper myDB;
    EditText editDate, editTitle, editTime, editLocation, editDescription;
    Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        myDB = new EventDatabaseHelper(this);                       //call to EventDatabaseHelper which links Event.java to the event database

        editDate = (EditText)findViewById(R.id.editDate);
        editTitle = (EditText)findViewById(R.id.editTitle);
        editTime = (EditText)findViewById(R.id.editTime);
        editLocation = (EditText)findViewById(R.id.editLocation);
        editDescription = (EditText)findViewById(R.id.editDescription);
        createButton = (Button)findViewById(R.id.createButton);

        AddData();                                                          //call to addData method to add to database on button click
    }

    public void AddData() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted;
                if(editDescription.getText().toString() != null) {          //conditional checks to see if description was added during event creation - adds empty string if not
                    isInserted = myDB.insertData(editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), editDescription.getText().toString());
                } else {
                    isInserted = myDB.insertData(editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), "");
                }

                if(isInserted == true) {                                    //conditional to display popup telling whether data insertion was successful
                    Toast.makeText(Event.this, "Data Inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Event.this, "Data NOT Inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static EventDatabaseHelper getDB() {
        return myDB;
    }
}
