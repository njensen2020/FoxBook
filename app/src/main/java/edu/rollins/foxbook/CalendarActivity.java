package edu.rollins.foxbook;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity{
    //add line to lock orientation
    private static final String TAG = "CalendarActivity";

    EventDatabaseHelper myDB;
    CalendarView nCalendarView;
    TextView myDate;
    TextView myEvents;
    boolean flag;                           //attempt to prevent crashing when db empty - true if database exists, false otherwise

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Setting title of action bar
        getSupportActionBar().setTitle("Calendar");

        nCalendarView = (CalendarView) findViewById(R.id.calendarView);
        myDate = (TextView)findViewById(R.id.myDate);
        myEvents = (TextView)findViewById(R.id.myEvents);
        myDB = Event.getDB();
        if(myDB == null) {
            flag = false;
        } else {
            flag = true;
        }

        nCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {             //listener to notice when a new date is selected on calendar
            @Override
            public void onSelectedDayChange(@NonNull CalendarView nCalendarView, int year, int month, int dayOfMonth) {
                String date = (month+1) + "/" + dayOfMonth + "/" + year;                            //puts date selected into formatted string; month must be increased one as it is given from 0 to 11
                myDate.setText(date);
                if(flag) {                                                                          //only query DB if DB exists
                    //Cursor res = myDB.getAllData();                                               //call to query database for all events when any data is selected - included for test purposes
                    Cursor res = myDB.getDateSpecificData(date);                                    //call to query database for all events on selected date
                    if (res.getCount() == 0) {                                                      //conditional to throw 'no events' message if there aren't any events in the database for given date
                        myEvents.setText("No events to display for " + date);
                    } else {
                        StringBuffer buff = new StringBuffer();                                     //start of process of taking returned database information and turning it into a readable string for printing
                        while (res.moveToNext()) {
                            buff.append("Date: " + res.getString(0) + "\n");
                            buff.append("Time: " + res.getString(1) + "\n");
                            buff.append("Title: " + res.getString(3) + "\n");
                            buff.append("Location: " + res.getString(4) + "\n");
                            buff.append("Description: " + res.getString(5) + "\n\n");
                        }
                        myEvents.setText(buff.toString()); //format for readability
                    }
                } else {
                    myEvents.setText("No events to display for " + date);                           //still throw no events even when DB empty
                }
            }
        });
    }


}
