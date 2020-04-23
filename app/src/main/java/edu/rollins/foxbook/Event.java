package edu.rollins.foxbook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Event extends AppCompatActivity {
	//class which allows club users to create and/or edit events
	
    private DatabaseReference mDatabase;
    EventDatabaseHelper myDB;
    EditText editDate, editTitle, editTime, editLocation, editDescription;
    Button createButton, editButton;
    Spinner chooseFilter;
    String filter, savedExtra, clubName;
    boolean editor;

    DatePickerDialog m_CalendarDialog;

    @Override
    public void onBackPressed() {	//back button takes user to their club page, preserving club name
        Intent intent = new Intent(Event.this, Club.class);
        intent.putExtra("club", clubName);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);	//links .java file to activity_event.xml

        mDatabase = FirebaseDatabase.getInstance().getReference(); //gets Firebase reference
        myDB = SplashActivity.getDB();           //call to EventDatabaseHelper which links Event.java to the event database

        editDate = (EditText)findViewById(R.id.editDate);
        editTitle = (EditText)findViewById(R.id.editTitle);
        editTime = (EditText)findViewById(R.id.editTime);
        editLocation = (EditText)findViewById(R.id.editLocation);
        editDescription = (EditText)findViewById(R.id.editDescription);
        createButton = (Button)findViewById(R.id.createButton);
        editButton = (Button)findViewById(R.id.editButton);
        chooseFilter = (Spinner)findViewById(R.id.chooseFilter);

        Calendar mcurrentDate = Calendar.getInstance();
        m_CalendarDialog = new DatePickerDialog(this, listener, mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH), mcurrentDate.get(Calendar.DAY_OF_MONTH));

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

        if(getIntent().getStringExtra("club") != null) {
            clubName = getIntent().getStringExtra("club");
        }

        if(getIntent().getStringExtra("event") != null) {		//if statement for when in editing mode
            savedExtra = getIntent().getStringExtra("event");   //savedExtra holds an event's ID passed via EventSelection
            Cursor eventCursor = myDB.getIDSpecificData(savedExtra);
            if(eventCursor.getCount() == 0) {
                editDescription.setText("Editor failure");
            } else {	//populate text fields with the information of the selected event
                eventCursor.moveToFirst(); //necessary because cursors automatically start at index -1, which does not hold data
                editDate.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("DATE")));
                editTitle.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("TITLE")));
                editTime.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("TIME")));
                editLocation.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("LOCATION")));
                editDescription.setText(eventCursor.getString(eventCursor.getColumnIndexOrThrow("DESCRIPTION")));
                clubName = eventCursor.getString(eventCursor.getColumnIndexOrThrow("CLUB"));
            }
            editor = true;
        } else {
            savedExtra = "";
            editor = false;
        }

        AddData();                        //call to addData method to add to database on button click
        EditData();                       //call to editData method which will send you to EventSelection on button click
    }

    public void AddData() {
        editTime.setOnClickListener(new View.OnClickListener() {	//causes clock to appear when selecting an event's time

            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
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
                        //generate unique id from timestamp
                        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                        String id = s.format(new Date());

                        if (editDescription.getText().toString() != null) {          //conditional checks to see if description was added during event creation - adds empty string if not
                            //add to SQLite database
                            isInserted = myDB.insertData(id, editDate.getText().toString(), editTime.getText().toString(), clubName, editTitle.getText().toString(), editLocation.getText().toString(), editDescription.getText().toString(), filter);

                            //add data to Firebase under events
                            DatabaseReference nuRef = mDatabase.child("events").child(id);
                            nuRef.child("date").setValue(editDate.getText().toString());
                            nuRef.child("time").setValue(editTime.getText().toString());
                            nuRef.child("club").setValue(clubName);
                            nuRef.child("title").setValue(editTitle.getText().toString());
                            nuRef.child("location").setValue(editLocation.getText().toString());
                            nuRef.child("description").setValue(editDescription.getText().toString());
                            nuRef.child("filter").setValue(filter);

                        } else {
							//add to SQLite database
                            isInserted = myDB.insertData(id, editDate.getText().toString(), editTime.getText().toString(), clubName, editTitle.getText().toString(), editLocation.getText().toString(), "", filter);

                            //add data to Firebase under events
                            DatabaseReference nuRef = mDatabase.child("events").child(id);
                            nuRef.child("date").setValue(editDate.getText().toString());
                            nuRef.child("time").setValue(editTime.getText().toString());
                            nuRef.child("club").setValue(clubName);
                            nuRef.child("title").setValue(editTitle.getText().toString());
                            nuRef.child("location").setValue(editLocation.getText().toString());
                            nuRef.child("filter").setValue(filter);

                        }
                    } else {               //updates existing data in the database
                        if (editDescription.getText().toString() != null) {          //conditional checks to see if description was added during event creation - adds empty string if not
                            //updates SQLite database
                            isInserted = myDB.updateData(savedExtra, editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), editDescription.getText().toString(), filter);

                            //updates Firebase
                            DatabaseReference nuRef = mDatabase.child("events").child(savedExtra);
                            nuRef.child("date").setValue(editDate.getText().toString());
                            nuRef.child("time").setValue(editTime.getText().toString());
                            nuRef.child("club").setValue(clubName);
                            nuRef.child("title").setValue(editTitle.getText().toString());
                            nuRef.child("location").setValue(editLocation.getText().toString());
                            nuRef.child("description").setValue(editDescription.getText().toString());
                            nuRef.child("filter").setValue(filter);

                        } else {
                            //updates SQLite database
                            isInserted = myDB.updateData(savedExtra, editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), "", filter);

                            //updates Firebase
                            DatabaseReference nuRef = mDatabase.child("events").child(savedExtra);
                            nuRef.child("date").setValue(editDate.getText().toString());
                            nuRef.child("time").setValue(editTime.getText().toString());
                            nuRef.child("club").setValue(clubName);
                            nuRef.child("title").setValue(editTitle.getText().toString());
                            nuRef.child("location").setValue(editLocation.getText().toString());
                            nuRef.child("description").setValue(" ");
                            nuRef.child("filter").setValue(filter);

                        }
                    }

                    if (isInserted == true) {            //conditional to display pop-up telling whether data insertion was successful
                        Toast.makeText(Event.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Event.this, "Data NOT Inserted", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        editDate.setOnClickListener(new View.OnClickListener() { //causes a calendar to appear when entering date
            @Override
            public void onClick(View v) {
                m_CalendarDialog.show();
            }
        });
    }

    public void EditData() {	//sends user to a page consisting of all events their club has created so they can pick which to edit
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Event.this, EventSelection.class);
                intent.putExtra("club", clubName);
                startActivity(intent);
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
}
