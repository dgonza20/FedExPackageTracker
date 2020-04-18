package com.example.hw4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static int numData = 0;
    public static final String DATABASE_NAME = "tracking.db";
    public static final String TABLE_NAME = "packages";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "TRACK_ID";
    public static final String COL_3 = "ORIGIN";
    public static final String COL_4 = "DESTI";
    public static final String COL_5 = "CURRLOC";
    public static final String COL_6 = "DELIVERED";


    // Creation of the database if it doesnt exist
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME  +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TRACK_ID INTEGER, ORIGIN INTEGER, DESTI INTEGER, CURRLOC INTEGER, DELIVERED INTEGER)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Insert data to the the table
    public boolean insertData(Integer track_id, Integer SourceInt,Integer DestInt, Integer CurrentInt, Integer DelivInt) {
        //Random r = new Random();
        //Integer track_id = r.nextInt(9999 - 1000) + 1000;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, track_id);     // Track ID
        contentValues.put(COL_3,SourceInt);     // Source
        contentValues.put(COL_4,DestInt);       // Destination
        contentValues.put(COL_5,CurrentInt);    // Current
        contentValues.put(COL_6,DelivInt);      // Delivered or Not

        long result = sqLiteDatabase.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    // Get all data from the table
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    // Get current locations column of the tracking ID
    public Cursor getCurrentLocations(String trackID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select CURRLOC from "+ TABLE_NAME  +
                " where TRACK_ID=" + trackID,null);
        return res;
    }

    // Update current location of a specific ID
    public boolean updateData(String id,Integer current) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_5,current);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    // Delete a row from table
    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    // Delete table from DB
    public boolean deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        return true;
    }

    // Delete DB
    public boolean deleteDB(Context context){
        context.deleteDatabase(DATABASE_NAME);
        return true;
    }

    // Get the latest Current loc and Delivery info of tracking ID
    public Cursor getCurrentAndDeliv(String trackID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select CURRLOC, DELIVERED from "+ TABLE_NAME  +
                " where TRACK_ID=" + trackID + " ORDER BY ID DESC LIMIT 1",null);
        return res;
    }

    // Check if a package is delivered or not
    public boolean IsDelivered(String trackID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT DELIVERED from "+ TABLE_NAME  +
                " where TRACK_ID=" + trackID + " ORDER BY ID DESC LIMIT 1",null);

        if(res.getCount() == 0){
            return false;
        }
        else{
            res.moveToNext();
            Integer DelivInt = Integer.parseInt(res.getString(0));

            if(DelivInt == 1) return true;
            else return false;
        }
    }

    // Get the latest row of a tracking ID
    public Cursor getLatestTrack(String trackID){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * from " + TABLE_NAME  +
                " WHERE TRACK_ID=" + trackID + " ORDER BY ID DESC LIMIT 1",null);
        return  res;
    }
}
