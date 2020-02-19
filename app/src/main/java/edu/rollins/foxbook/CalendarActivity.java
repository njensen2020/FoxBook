package edu.rollins.foxbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity{
    private static final String TAG = "CalendarActivity";

    EventDatabaseHelper myDB;
    CalendarView nCalendarView;
    TextView myDate;
    TextView myEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        nCalendarView = (CalendarView) findViewById(R.id.calendarView);
        myDate = (TextView)findViewById(R.id.myDate);
        myEvents = (TextView)findViewById(R.id.myEvents);
        myDB = Event.getDB();

        nCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView nCalendarView, int year, int month, int dayOfMonth) {
                String date = month + "/" + dayOfMonth + "/" + year;
                myDate.setText(date);
                myEvents.setText(myDB.getAllData().toString());
                Log.d(TAG, "onSelectedDayChange: " + date);
            }
        });
    }


}
