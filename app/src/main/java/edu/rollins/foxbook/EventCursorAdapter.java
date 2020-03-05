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
            String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
            int priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority"));
            // Populate fields with extracted properties
            tvBody.setText(body);
            tvPriority.setText(String.valueOf(priority));
        }

        @Override
        public Event getItem(int position) {
            //what should items be??
            return items.get(position);
    }
}
