package edu.rollins.foxbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClubDatabaseHelper extends SQLiteOpenHelper {
    //creates and accesses database of clubs
    public static final String DATABASE_NAME = "Club.db";
    public static final String TABLE_NAME = "club_table";
    public static final String COL_1 = "NAME";              //primary key
    public static final String COL_2 = "EMAIL";
    public static final String COL_3 = "BIO";

    public ClubDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {                       //creates database with given table
        db.execSQL("create table " + TABLE_NAME + " ("+ COL_1 + " TEXT, " + COL_2 + " TEXT, " + COL_3 + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String email, String bio, String image) {        //inserts new club into database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, email);
        contentValues.put(COL_3, bio);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1) {                                          //returns true or false to tell if insertion was successful or not
            return false;
        } else {
            return true;
        }
    }

    //allows clubs to edit their pages in the case of adding a new contact email or updating their bio
    public boolean updateData(String name, String email, String bio, String image) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_2 + " = \'" + email + "\' WHERE " + COL_1 + " = \'" + name + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_3 + " = \"" + bio + "\" WHERE " + COL_1 + " = \'" + name + "\'");

        return true;
    }

	//check to see if a club with the given name is already in the database
    public boolean inDatabase(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +TABLE_NAME + " where " + COL_1 + " like \'" + id + "\'", null);
        if(res.getCount() == 0) {   //nothing in database under that id
            return false;
        } else {
            return true;
        }
    }

	//returns all club data for all clubs
    public Cursor getAllClubData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

	//returns all club data for the given club
    public Cursor getClubData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +TABLE_NAME + " where " + COL_1 + " like \'" + name + "\'", null);
        return res;
    }
}
