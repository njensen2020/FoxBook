package edu.rollins.foxbook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

import edu.rollins.foxbook.Config.ClubList;

public class Event extends AppCompatActivity {
    static EventDatabaseHelper myDB;
    EditText editDate, editTitle, editTime, editLocation, editDescription;
    Button createButton, editButton;
    Spinner chooseFilter;
    String filter, savedExtra;
    boolean editor;
    DatePickerDialog m_CalanderDialog;
    ArrayAdapter<String> m_ClubAdapter;
    ArrayList<String> m_ClubList;
    @Override
    public void onBackPressed() {
        startActivity(new Intent( Event.this, Homepage.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Caitlin/Mohamed - I think I figured out why the calendar/events weren't working. It's got something to do with this statement. -Neil
        // myDB = EventDatabaseHelper.getInstance(this);                       //call to EventDatabaseHelper which links Event.java to the event database;

        m_ClubList = ClubList.getInstance().m_ClibList;

        editDate = (EditText)findViewById(R.id.editDate);
        editTitle = (EditText)findViewById(R.id.editTitle);
        editTime = (EditText)findViewById(R.id.editTime);
        editLocation = (EditText)findViewById(R.id.editLocation);
        editDescription = (EditText)findViewById(R.id.editDescription);
        createButton = (Button)findViewById(R.id.createButton);
        editButton = (Button)findViewById(R.id.editButton);
        chooseFilter = (Spinner)findViewById(R.id.chooseFilter);
        Calendar mcurrentDate = Calendar.getInstance();
        m_CalanderDialog = new DatePickerDialog(this, listener, mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH), mcurrentDate.get(Calendar.DAY_OF_MONTH));

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

        AddData();                        //call to addData method to add to database on button click
        EditData();                       //call to editData method which will send you to EventSelection on button click

        if(getIntent().getStringExtra("event") != null) {
            savedExtra = getIntent().getStringExtra("event");   //savedExtra holds an event's ID passed via EventSelection
            Cursor eventCursor = myDB.getIDSpecificData(savedExtra);
            if(eventCursor.getCount() == 0) {
                editDescription.setText("Editor failure");
            } else {
                eventCursor.moveToFirst(); //necessary because cursors automatically start at index -1, which does not hold data
                editDate.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("DATE")));
                editTitle.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("TITLE")));
                editTime.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("TIME")));
                editLocation.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("LOCATION")));
                editDescription.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("DESCRIPTION")));
                selectSpinnerItemByValue(chooseFilter, eventCursor.getString(eventCursor.getColumnIndexOrThrow("FILTER")));
            }
            editor = true;
        } else {
            savedExtra = "";
            editor = false;
        }

    }

    public void AddData() {

        m_ClubAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_ClubList);
        chooseFilter.setAdapter(m_ClubAdapter);
        editTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Event.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted;
                if(filter == "") {
                    Toast.makeText(Event.this, "Select an event type", Toast.LENGTH_LONG).show();
                } else {

                    if(!editor) {          //inserts new data into database
                        if (editDescription.getText().toString() != null) {          //conditional checks to see if description was added during event creation - adds empty string if not
                            isInserted = myDB.insertData(editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), editDescription.getText().toString(), filter);
                        } else {
                            isInserted = myDB.insertData(editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), "", filter);
                        }
                    } else {               //updates existing data in the database (DOES NOT WORK YET QUOD ID)
                        if (editDescription.getText().toString() != null) {          //conditional checks to see if description was added during event creation - adds empty string if not
                            isInserted = myDB.updateData(savedExtra, editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), editDescription.getText().toString(), filter);
                        } else {
                            isInserted = myDB.updateData(savedExtra, editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), "", filter);
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
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_CalanderDialog.show();
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
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = (monthOfYear+1) + "/" + dayOfMonth + "/" + year;
            editDate.setText(date);
        }
    };
    public void selectSpinnerItemByValue(Spinner spnr, String value) {
        for (int position = 0; position < m_ClubList.size(); position++) {
            if(m_ClubList.get(position).equals(value)) {
                chooseFilter.setSelection(position);
                return;
            }
        }
    }

    public static EventDatabaseHelper getDB() {
        return myDB;
    }

}
