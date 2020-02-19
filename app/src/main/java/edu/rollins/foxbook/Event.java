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
        setContentView(R.layout.event_layout);
        myDB = new EventDatabaseHelper(this);

        editDate = (EditText)findViewById(R.id.editDate);
        editTitle = (EditText)findViewById(R.id.editTitle);
        editTime = (EditText)findViewById(R.id.editTime);
        editLocation = (EditText)findViewById(R.id.editLocation);
        editDescription = (EditText)findViewById(R.id.editDescription);
        createButton = (Button)findViewById(R.id.createButton);

        AddData();
    }

    public void AddData() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted;
                if(editDescription.getText().toString() != null) {
                    isInserted = myDB.insertData(editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), editDescription.getText().toString());
                } else {
                    isInserted = myDB.insertData(editDate.getText().toString(), editTime.getText().toString(), editTitle.getText().toString(), editLocation.getText().toString(), "");
                }

                if(isInserted == true) {
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
