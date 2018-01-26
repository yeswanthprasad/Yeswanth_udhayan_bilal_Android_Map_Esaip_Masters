package com.example.yeswanth.mapapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.Text;

/**
 * Created by yeswanth on 24/01/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "city_table";
    private static final String col1 = "ID";
    private static final String col2 = "name";




    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + col2 + " TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean addData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col2, item);

        Log.d(TAG, "addDATA: Adding " + item + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }
        else {
            return true;
        }

    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + col1 + " FROM " + TABLE_NAME + " WHERE " + col2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateName(String newName, int id,String oldName ){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + col2 + " = '" + newName + "' WHERE " + col1 + " = '" + id + "'" + " AND " + col2 + " = '" + oldName + "'";
        Log.d(TAG, "Update Name: query: " + query);
        Log.d(TAG, "Update Name: Setting Name to: " + newName);
        db.execSQL(query);

    }

    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + col1 + " = '" + id + "'"+ " AND " + col2 + " = '" + name + "'";
        Log.d(TAG, "Deleting Name: query: " + query);
        Log.d(TAG, "Delete Name: Deleting Name to: " + name + " From Database");
        db.execSQL(query);
    }


}
