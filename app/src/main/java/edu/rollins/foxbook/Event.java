package edu.rollins.foxbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Event extends AppCompatActivity {
    static EventDatabaseHelper myDB;
    EditText editDate, editTitle, editTime, editLocation, editDescription;
    Button createButton, editButton;
    Spinner chooseFilter;
    String filter, savedExtra;
    int id;

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
        editButton = (Button)findViewById(R.id.editButton);
        chooseFilter = (Spinner)findViewById(R.id.chooseFilter);
        //String[] options = new String[]{"Sports", "Arts", "STEM", "Entertainment", "Greek", "Community"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_event, options);
        //adapter.setDropDownViewResource(R.layout.activity_event);
        //chooseFilter.setAdapter(adapter);

        chooseFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filter = "";
            }
        });

        //should theoretically get the selected even info passed from EvenSelection
        if(getIntent().getStringExtra("event") != null) {
            savedExtra = getIntent().getStringExtra("event");
            //now to parse . . . but how????
            //editDescription.setText(savedExtra, TextView.BufferType.EDITABLE);
            editDescription.setText(savedExtra);
            id = 0; //<--- figure out how to put id attribute into this variable!!!!!!!!!

        } else {
            savedExtra = "";
            id = 0;
        }

        AddData();                        //call to addData method to add to database on button click
        EditData();                       //call to editData method which will send you to EventSelection on button click
    }

    public void AddData() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted;
                if(filter == "") {
                    Toast.makeText(Event.this, "Select an event type", Toast.LENGTH_LONG).show();
                } else {

                    if(id == 0) {          //inserts new data into database
                        if (editDescription.getText().toString() != null) {          //conditional checks to see if description was added during event creation - adds empty string if not
                            isInserted = myDB.insertData(editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), editDescription.getText().toString(), filter);
                        } else {
                            isInserted = myDB.insertData(editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), "", filter);
                        }
                    } else {               //updates existing data in the database (DOES NOT WORK YET QUOD ID)
                        if (editDescription.getText().toString() != null) {          //conditional checks to see if description was added during event creation - adds empty string if not
                            isInserted = myDB.updateData(id, editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), editDescription.getText().toString(), filter);
                        } else {
                            isInserted = myDB.updateData(id, editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), "", filter);
                        }
                    }

                    if (isInserted == true) {                                    //conditional to display popup telling whether data insertion was successful
                        Toast.makeText(Event.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Event.this, "Data NOT Inserted", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void EditData() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( Event.this, EventSelection.class));
            }
        });
    }

    public static EventDatabaseHelper getDB() {
        return myDB;
    }

}
