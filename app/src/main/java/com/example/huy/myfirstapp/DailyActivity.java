package com.example.huy.myfirstapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_DESCRIPTION;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_TIME;

public class DailyActivity extends AppCompatActivity {
    private TextView theDate;
    public DBManager dbManager;
    private ListView dailyListView;
    String description;
    String time;
    TaskAdapter adapter;
    ArrayList<Task> taskArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        dailyListView = findViewById(R.id.listView_Daily);

        Intent getDateIntent = getIntent();
        String date = getDateIntent.getStringExtra("date");
        theDate = findViewById(R.id.text_thedate);
        theDate.setText(date + "\n" + "TO DO LIST:");

        dbManager = new DBManager(this);
        dbManager.open();
        getData();

    }

    public void getData() {
        Cursor cursor = dbManager.dailyFetch();
        if (cursor.moveToFirst()) {
            description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
            String shortTime = time.substring(11);
            taskArrayList.add(new Task(description, shortTime));
            while (cursor.moveToNext()) {
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                shortTime = time.substring(11);
                taskArrayList.add(new Task(description, shortTime));
            }
            adapter = new TaskAdapter(taskArrayList, DailyActivity.this);
            dailyListView.setAdapter(adapter);
            dailyListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }
    }
}
