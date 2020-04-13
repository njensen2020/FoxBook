package edu.rollins.foxbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDatabaseHelper  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Event.db";
    public static final String TABLE_NAME = "event_table";
    public static final String COL_1 = "DATE";
    public static final String COL_2 = "TIME";
    public static final String COL_3 = "CLUB";              //this will be club name & it's the primary key of the club database table
    public static final String COL_4 = "TITLE";
    public static final String COL_5 = "LOCATION";
    public static final String COL_6 = "DESCRIPTION";
    public static final String COL_7 = "ID";                //primary key
    public static final String COL_8 = "FILTER";

    public EventDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {                       //creates database with given table
        db.execSQL("create table " + TABLE_NAME + " ("+ COL_1 + " TEXT, " + COL_2 + " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT, " + COL_5 + " TEXT, " + COL_6 + " TEXT, " + COL_7 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_8 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String date, String time, String club, String title, String location, String description, String filter) {        //inserts new event into database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, date);
        contentValues.put(COL_2, time);
        contentValues.put(COL_3, club);                               //club is empty for now, in future editions club name will be gained based on who is creating event
        contentValues.put(COL_4, title);
        contentValues.put(COL_5, location);
        contentValues.put(COL_6, description);
        contentValues.put(COL_8, filter);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1) {                                          //returns true or false to tell if insertion was successful or not
            return false;
        } else {
            return true;
        }
    }

    public boolean updateData(String id, String date, String time, String title, String location, String description, String filter) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_1 + " = \'" + date + "\' WHERE " + COL_7 + " = \'" + id + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_2 + " = \'" + time + "\' WHERE " + COL_7 + " = \'" + id + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_4 + " = \'" + title + "\' WHERE " + COL_7 + " = \'" + id + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_5 + " = \'" + location + "\' WHERE " + COL_7 + " = \'" + id + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_6 + " = \'" + description + "\' WHERE " + COL_7 + " = \'" + id + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_8 + " = \'" + filter + "\' WHERE " + COL_7 + " = \'" + id + "\'");

        return true;
    }

    public Cursor getAllData() {                                    //queries database and returns everything
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor getIDSpecificData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +TABLE_NAME + " where " + COL_7 + " like \'" + id + "\'", null);
        return res;
    }

    public Cursor getDateSpecificData(String date) {                //queries database and returns all information on events from a specific day, ordered by time of day
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where " + COL_1 + " like \'" + date + "\'", null);
        return res;
    }

    public Cursor getClubSpecificData(String club) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where " + COL_3 + " like \'" + club + "\'", null);
        return res;
    }
}
