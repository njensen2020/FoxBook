package edu.rollins.foxbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	//SQLite database for storing user accounts

    private static final String TAG = "DatabaseHelper";

    // Setting up user account table
    private static final String DB_NAME = "Accounts.db";
    private static final String TABLE_NAME = "Account_Info";
    private static final String COL1 = "ID";	//primary key
    private static final String COL2 = "FirstName";
    private static final String COL3 = "LastName";
    private static final String COL4 = "Username";
    private static final String COL5 = "Password";
    private static final String COL6 = "PIN";
    private static final String COL7 = "Type";
    private static final String COL8 = "Club";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID TEXT PRIMARY KEY, FirstName TEXT, LastName TEXT, Username TEXT, Password TEXT, PIN TEXT, Type TEXT, Club TEXT) ";
        db.execSQL(createTable);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

	//add a new account to the database
    public boolean addData(String id, String first, String last, String username, String password, String pin, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, id);
        contentValues.put(COL2, first);
        contentValues.put(COL3, last);
        contentValues.put(COL4, username);
        contentValues.put(COL5, password);
        contentValues.put(COL6, pin);
        contentValues.put(COL7, type);
        contentValues.put(COL8, "None");	//accounts do not start off following any clubs

        Log.d(TAG, "addData: Adding " + first + ", " + last + ", " + username + ", " + password + ", " + pin + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

	//updates an account's information in the database
    public boolean updateData(String id, String firstname, String lastname, String username, String password, String pin, String type, String club) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL2 + " = \'" + firstname + "\' WHERE " + COL1 + " = \'" + id + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL3 + " = \'" + lastname + "\' WHERE " + COL1 + " = \'" + id + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL4 + " = \'" + username + "\' WHERE " + COL1 + " = \'" + id + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL5 + " = \'" + password + "\' WHERE " + COL1 + " = \'" + id + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL6 + " = \'" + pin + "\' WHERE " + COL1 + " = \'" + id + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL7 + " = \'" + type + "\' WHERE " + COL1 + " = \'" + id + "\'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL8 + " = \'" + club + "\' WHERE " + COL1 + " = \'" + id + "\'");

        return true;
    }

	//checks to see if a user with the given id exists in the database
    public boolean inDatabase(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +TABLE_NAME + " where " + COL1 + " like \'" + id + "\'", null);
        if(res.getCount() == 0) {   //nothing in database under that id
            return false;
        } else {
            return true;
        }
    }

	//method to get the id of a user with the given username and password
    public String getID(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT ID FROM " + TABLE_NAME + " WHERE username='" + username + "' AND password='" + password + "'", null);
        if(res.getCount() > 0) {
            res.moveToFirst();
            String id = res.getString(0);
            return id;
        } else {
            return "x";
        }
    }

	//returns all data in the database
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

	//checks a user's login credentials based on their username and password
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

	//returns whether a user is a club or not, and therefor a student
    public boolean isClub(String thisUsername, String thisPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor name = db.rawQuery("SELECT type FROM " + TABLE_NAME + " WHERE username='" + thisUsername + "' AND password='" + thisPassword + "'", null);

        if(name.getCount() > 0) {
            name.moveToFirst();
            String type = name.getString(0);
            if(type.equals("Club")) {
                name.close();
                return true;
            }
            name.close();
            return false;
        } else {
            name.close();
            return false;
        }
    }

	//updates the password of the user with the given pin number, returns id, username, and type of account
    public String updatePassword(String password, String pin) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlString = "UPDATE Account_Info SET " + COL5 + "= '" + password + "' WHERE " + COL6 + "= '" + pin + "'";
        db.execSQL(sqlString);

        Cursor res = db.rawQuery("SELECT ID, type, username FROM " + TABLE_NAME + " WHERE PIN='" + pin + "'", null);
        res.moveToFirst();
        String id = res.getString(0);
        id = id + "_" + res.getString(1) + "_" + res.getString(2);
        return id;
    }

	//checks to see if a user in the database has the given pin number
    public boolean checkPIN(String thisPin) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor pin = db.rawQuery("SELECT pin FROM " + TABLE_NAME + " WHERE pin='" + thisPin + "'", null);

        if(pin.getCount() <= 0) {
            pin.close();
            return false;
        }

        else {
            pin.close();
            return true;
        }
    }

	//deletes the user with the given username, password, and pin number from the database
    public void deleteAccount(String username, String password, String pin) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlString = "DELETE FROM Account_Info WHERE username LIKE '%" + username + "%'";
        db.execSQL(sqlString);
    }

	//method which adds the given club to the user's followed clubs
    public void followClub(String thisUsername, String thisPassword, String club) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor followedClubs = db.rawQuery("SELECT club FROM " + TABLE_NAME + " WHERE username='" + thisUsername + "' AND password='" + thisPassword + "'", null);

        if(followedClubs.getCount() > 0) {
            //add new club to existing list of followed clubs
            followedClubs.moveToFirst();
            if(followedClubs.getString(0).equals("None")) {
                db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL8 + " = \'" + club + "\' WHERE " + COL4 + " = \'" + thisUsername + "\' AND " + COL5 + " = \'" + thisPassword + "\'");
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append(followedClubs.getString(0));
                sb.append("_");
                sb.append(club);

                //update database
                db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL8 + " = \'" + sb.toString() + "\' WHERE " + COL4 + " = \'" + thisUsername + "\' AND " + COL5 + " = \'" + thisPassword + "\'");
            }
        } else {
            //student currently doesn't follow any clubs, update database, replacing default None with the given club
            db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL8 + " = \'" + club + "\' WHERE " + COL4 + " = \'" + thisUsername + "\' AND " + COL5 + " = \'" + thisPassword + "\'");

        }
    }
	
	//removes the given club from the user's followed clubs
    public void unfollowClub(String thisUsername, String thisPassword, String club) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor original = getClubsFollowed(thisUsername, thisPassword);

        if(original.getCount() > 0) {
            //iterate through followed clubs, checking for deleted one
            original.moveToFirst();
            StringBuffer sb = new StringBuffer();
            String[] followed = original.getString(0).split("_");
            for(String a : followed) {
                if(!a.equals(club) && !(a.isEmpty())) {
                    sb.append(a);
                    sb.append("_");
                }
            }

            //if the string buffer ends up without club names and only underscores, make it empty before updating
            if(sb.toString().equals("_")) {
                sb.delete(0, sb.length());
                sb.append("");
            }

            //update database
            db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL8 + " = \'" + sb.toString() + "\' WHERE " + COL4 + " = \'" + thisUsername + "\' AND " + COL5 + " = \'" + thisPassword + "\'");
        }
    }

	//returns whether or not the given user is following the given club
    public boolean isFollowing(String thisUsername, String thisPassword, String club) {
        Cursor original = getClubsFollowed(thisUsername, thisPassword);

        if(original.getCount() > 0) {
            //iterate through followed clubs, checking for the one given
            original.moveToFirst();
            String[] followed = original.getString(0).split("_");
            boolean flag = false;
            for (String a : followed) {
                if(a.equals(club)) {
                    flag = true;
                }
            }
            return flag;
        }
        return false;
    }

	//returns a cursor containing the clubs followed by the given user
    public Cursor getClubsFollowed(String thisUsername, String thisPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL8 + " FROM " + TABLE_NAME + " WHERE " + COL4 + " LIKE \'" + thisUsername + "\' AND " + COL5 + " LIKE \'" + thisPassword + "\'", null);
        return data;
    }
}
