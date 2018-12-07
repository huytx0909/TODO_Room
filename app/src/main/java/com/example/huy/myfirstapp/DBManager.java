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
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_STATUS;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_TIME;
import static com.example.huy.myfirstapp.DatabaseHelper.TABLE_NAME;
import static com.example.huy.myfirstapp.CalendarActivity.CHOSEN_DATE;
import static com.example.huy.myfirstapp.MainActivity.YEAR_MONTH_DAY;

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

    public int update(String oldDescription, String newDescription, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESCRIPTION, newDescription);
        contentValues.put(COLUMN_TIME, time);
        return database.update(TABLE_NAME, contentValues, COLUMN_DESCRIPTION + "= '" + oldDescription + "'", null);
    }

    public void delete(String description) {
        database.delete(TABLE_NAME, COLUMN_DESCRIPTION + "= '" + description + "'", null);
    }

    Cursor fetch() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(date);

        String[] columns = new String[]{COLUMN_ID, COLUMN_DESCRIPTION, COLUMN_TIME, COLUMN_STATUS};
        Cursor cursor = database.query(TABLE_NAME, columns, COLUMN_TIME + " LIKE '" + formattedDate + "%'", null, null, null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.e("Wrong!", "null Cursor");
        }
        return cursor;
    }


    Cursor dailyFetch() {
        String[] columns = new String[]{COLUMN_ID, COLUMN_DESCRIPTION, COLUMN_TIME, COLUMN_STATUS};
        Cursor cursor = database.query(TABLE_NAME, columns, COLUMN_TIME + " LIKE '" + CHOSEN_DATE + "%'", null, null, null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.e("Wrong!", "null Cursor");
        }
        return cursor;
    }

    static void setPickedDate() {
        database.execSQL("delete from " +  TABLE_NAME + " where " + COLUMN_DESCRIPTION + " LIKE '%Example%'");
        database.execSQL("insert or replace into tasks(description, appointedTime) values(\"Example description\"," +
                " \"" + CHOSEN_DATE + "T13:22\")");
    }

    public void insertMain() {
        database.execSQL("delete from " +  TABLE_NAME + " where " + COLUMN_DESCRIPTION + " LIKE '%Example%'");
        database.execSQL("insert into tasks(description, appointedTime, status) values(\"Example Description\"," +
                " \"" + YEAR_MONTH_DAY + "T13:22\", \"1\")");
    }

    static void setStatus(Boolean isChecked, String description) {
        ContentValues contentValues = new ContentValues();
        if (isChecked) {
            contentValues.put(COLUMN_STATUS, "1");
            database.update(TABLE_NAME, contentValues, COLUMN_DESCRIPTION + "= '" + description + "'", null);
        } else {
            contentValues.put(COLUMN_STATUS, "0");
            database.update(TABLE_NAME, contentValues, COLUMN_DESCRIPTION + "= '" + description + "'", null);
        }
    }


}
