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
    public static final String COL_3 = "CLUB";         
    public static final String COL_4 = "TITLE";
    public static final String COL_5 = "LOCATION";
    public static final String COL_6 = "DESCRIPTION";
    public static final String COL_7 = "ID";      //primary key
    public static final String COL_8 = "FILTER";

    public EventDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {              //creates database with given table
        db.execSQL("create table " + TABLE_NAME + " ("+ COL_1 + " TEXT, " + COL_2 + " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT, " + COL_5 + " TEXT, " + COL_6 + " TEXT, " + COL_7 + " TEXT PRIMARY KEY, " + COL_8 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

	//inserts a new event into the SQL database
    public boolean insertData(String id, String date, String time, String club, String title, String location, String description, String filter) {        //inserts new event into database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, date);
        contentValues.put(COL_2, time);
        contentValues.put(COL_3, club);
        contentValues.put(COL_4, title);
        contentValues.put(COL_5, location);
        contentValues.put(COL_6, description);
        contentValues.put(COL_7, id);
        contentValues.put(COL_8, filter);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1) {                                          //returns true or false to tell if insertion was successful or not
            return false;
        } else {
            return true;
        }
    }

	//updates event with the given id in the database
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

	//returns whether their is an event in the database with the given id
    public boolean inDatabase(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +TABLE_NAME + " where " + COL_7 + " like \'" + id + "\'", null);
        if(res.getCount() == 0) {   //nothing in database under that id
            return false;
        } else {
            return true;
        }
    }

	//returns the id of the event with the following attributes
    public String getID(String club, String title, String time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + COL_7 + " from " + TABLE_NAME + " where " + COL_3 + " like \'" + club + "\' and " + COL_4 + " like \'" + title + "\' and " + COL_2 + " like \'" + time + "\' and " + COL_1 + " like \'" + date + "\'", null);
        if(res.getCount() > 0) {
            res.moveToFirst();
            String id = res.getString(0);
            return id;
        } else {
            return "x";
        }
    }
	
	//queries database and returns everything
    public Cursor getAllData() {            
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

	//returns the data of the event with the given id
    public Cursor getIDSpecificData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +TABLE_NAME + " where " + COL_7 + " like \'" + id + "\'", null);
        return res;
    }
	
	//queries database and returns all information on events from a specific day
    public Cursor getDateSpecificData(String date) {             
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where " + COL_1 + " like \'" + date + "\'", null);
        return res;
    }

	//returns all events for the given club
    public Cursor getClubSpecificData(String club) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where " + COL_3 + " like \'" + club + "\'", null);
        return res;
    }
}
