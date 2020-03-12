package edu.rollins.foxbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // Setting up user account table
    private static final String DB_NAME = "Accounts.db";
    private static final String TABLE_NAME = "Account_Info";
    private static final String COL1 = "ID";
    private static final String COL2 = "FirstName";
    private static final String COL3 = "LastName";
    private static final String COL4 = "Username";
    private static final String COL5 = "Password";
    private static final String COL6 = "PIN";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, FirstName TEXT, LastName TEXT, Username TEXT, Password TEXT, PIN TEXT) ";
        db.execSQL(createTable);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String first, String last, String username, String password, String pin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, first);
        contentValues.put(COL3, last);
        contentValues.put(COL4, username);
        contentValues.put(COL5, password);
        contentValues.put(COL6, pin);

        Log.d(TAG, "addData: Adding " + first + ", " + last + ", " + username + ", " + password + ", " + pin + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public boolean checkLogin(String thisUsername, String thisPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor name = db.rawQuery("SELECT username, password FROM " + TABLE_NAME + " WHERE username='" + thisUsername + "' AND password='" + thisPassword + "'", null);

        if(name.getCount() <= 0) {
            name.close();
            return false;
        }
        else {
            name.close();
            return true;
        }
    }

    public void updatePassword(String password, String pin) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlString = "UPDATE Account_Info SET " + COL5 + "= '" + password + "' WHERE " + COL6 + "= '" + pin + "'";
        db.execSQL(sqlString);
    }
}