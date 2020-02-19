package edu.rollins.foxbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity{
    private static final String TAG = "CalendarActivity";

    CalendarView nCalendarView;
    TextView myDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        nCalendarView = (CalendarView) findViewById(R.id.calendarView);
        myDate = (TextView)findViewById(R.id.myDate);

        nCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView nCalendarView, int year, int month, int dayOfMonth) {
                String date = month + "/" + dayOfMonth + "/" + year;
                myDate.setText(date);
                Log.d(TAG, "onSelectedDayChange: " + date);
            }
        });
    }

}
