package com.example.huy.myfirstapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_DESCRIPTION;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_ID;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_TIME;
import static com.example.huy.myfirstapp.DatabaseHelper.TABLE_NAME;
import static com.example.huy.myfirstapp.MainActivity.CURRENT_DATE_FORMATTED;
import static com.example.huy.myfirstapp.CalendarActivity.chosenDate;

public class DBManager {
    private DatabaseHelper databaseHelper;

    private Context context;

    private static SQLiteDatabase database;

    public DBManager(Context c) {
        this.context = c;
    }

    public DBManager open() throws SQLiteException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public void insert(String description, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_TIME, time);
        database.insert(TABLE_NAME, null, contentValues);
    }

    public int update(long _id, String description, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_TIME, time);
        return database.update(TABLE_NAME, contentValues,COLUMN_ID + "=" + _id, null);
    }

    public void delete(long _id) {
        database.delete(TABLE_NAME, COLUMN_ID + "=" + _id, null);
    }

    public Cursor fetch() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(date);

        String[] columns = new String[] {COLUMN_ID, COLUMN_DESCRIPTION, COLUMN_TIME};
        Cursor cursor = database.query(TABLE_NAME, columns, COLUMN_TIME+" LIKE '"+ formattedDate +"%'",null,null,null,
                null);

        if (cursor!=null) {
            cursor.moveToFirst();
        } else {
            Log.e("Wrong!", "null Cursor");
        }
        return cursor;
    }

    public static Cursor dailyFetch() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(date);

        String[] columns = new String[] {COLUMN_ID, COLUMN_DESCRIPTION, COLUMN_TIME};
        Cursor cursor = database.query(TABLE_NAME, columns, COLUMN_TIME+" LIKE '"+ chosenDate +"%'",null,null,null,
                null);

        if (cursor!=null) {
            cursor.moveToFirst();
        } else {
            Log.e("Wrong!", "null Cursor");
        }
        return cursor;
    }

    public static void setPickedDate() {
                freshListViewDaily();
                database.execSQL("insert into tasks(description, appointedTime) values(\"Daily-1\"," +
                        " \""+chosenDate+"T13:22\")");
    }

    public static void insertDummyMain() {
        database.execSQL("insert into tasks(description, appointedTime) values(\"Main-1\"," +
                " \"" + CURRENT_DATE_FORMATTED + "T13:22\")");
        database.execSQL("insert into tasks(description, appointedTime) values(\"Main-2\"," +
                " \"" + CURRENT_DATE_FORMATTED + "T14:50\")");
    }

    private static void freshListViewDaily() {
        database.execSQL("delete from tasks where "+COLUMN_TIME+" LIKE '%"+chosenDate+"%'");
    }

    public static void freshListViewMain() {
        database.execSQL("delete from tasks where "+COLUMN_TIME+" Like '%"+CURRENT_DATE_FORMATTED+"%'");
    }


}
