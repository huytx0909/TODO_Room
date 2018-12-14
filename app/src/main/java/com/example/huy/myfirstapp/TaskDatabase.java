package com.example.huy.myfirstapp;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import static com.example.huy.myfirstapp.MainActivity.CURRENT_FULL_TIME;
import static com.example.huy.myfirstapp.MainActivity.taskDatabase;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    private static volatile TaskDatabase DATABASE_INSTANCE;

    static RoomDatabase.Callback callBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    //Insert Data
                    taskDatabase.taskDao().insert(new Task("Example",
                            CURRENT_FULL_TIME, "1"));
                }
            });
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    };

    static TaskDatabase getDatabaseInstance(final Context context) {
        if (DATABASE_INSTANCE==null) {
            synchronized (TaskDatabase.class) {
                if (DATABASE_INSTANCE==null){
                    DATABASE_INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskDatabase.class,
                            "Task.db").addCallback(callBack).build();
                }
            }
        }
        return DATABASE_INSTANCE;
    }
}

