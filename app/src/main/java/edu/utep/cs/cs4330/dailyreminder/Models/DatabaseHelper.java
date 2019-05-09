package edu.utep.cs.cs4330.dailyreminder.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final String TABLE_NAME = "tasks_items";
    private static final String[] COL = {"t_id", "t_name", "i_date_created", "i_date_end", "i_priority", "t_file_1", "t_file_2", "t_description", "t_finished"};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME +" (t_id INTEGER PRIMARY KEY AUTOINCREMENT, t_name TEXT, i_date_created TEXT, i_date_end TEXT, i_priority INTEGER, t_file_1 FLOAT,  t_file_2 FLOAT," +
                " t_description TEXT, t_finished INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, Date date_created, Date date_end, int priority, String file1, String file2, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL[1], name);
        contentValues.put(COL[2], String.valueOf(date_created));
        contentValues.put(COL[3], String.valueOf(date_end));
        contentValues.put(COL[4], priority);
        contentValues.put(COL[5], file1);
        contentValues.put(COL[6], file2);
        contentValues.put(COL[7], description);
        contentValues.put(COL[8], 0);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    private Cursor fetchData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor response = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return response;
    }

    private Integer deletedata(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "t_id = ?", new String[] {id});
    }

    private boolean editData(String id, String name, String description, String start_date, String end_date, String priority, int finished) {
        //{"i_id", "i_name", "i_price", "i_weblink", "i_image", "i_newprice"};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL[1], name);
        contentValues.put(COL[2], start_date);
        contentValues.put(COL[3], end_date);
        contentValues.put(COL[4], priority);
        contentValues.put(COL[7], description);
        contentValues.put(COL[8], finished);
        db.update(TABLE_NAME, contentValues, "t_id = ?", new String[] {id});
        return true;
    }

    public boolean edit(String id, String name, String description, String start_date, String end_date, String priority, int finished) {
        return editData(id, name, description, start_date, end_date, priority, finished);
    }

    public Cursor fetchAllData(){
        return fetchData();
    }

    public Integer delete(String id) {
        return deletedata(id);
    }

    public String lastId(){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + "sqlite_sequence";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        return cursor.getString(0);
    }

}
