package edu.rollins.foxbook;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class EventCursorAdapter extends CursorAdapter {
    public EventCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_eventselect, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        TextView tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        // Extract properties from cursor
        int body = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
        String priority = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
        priority = priority + " " + cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
        // Populate fields with extracted properties
        tvBody.setText("");
        tvPriority.setText(priority);
    }
}
