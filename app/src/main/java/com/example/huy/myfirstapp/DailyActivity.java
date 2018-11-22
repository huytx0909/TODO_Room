package com.example.huy.myfirstapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_DESCRIPTION;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_TIME;

public class DailyActivity extends AppCompatActivity {
    private TextView theDate;
    private ListView dailyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        dailyListView = findViewById(R.id.listView_Daily);

        Intent getDateIntent = getIntent();
        String date = getDateIntent.getStringExtra("date");
        theDate = findViewById(R.id.text_thedate);
        theDate.setText(date + "\n" + "TO DO LISTT:");


        String description = null;
        String time = null;
        ArrayList<String> values = new ArrayList<>();
        Cursor cursor = DBManager.dailyFetch();
        if (cursor.moveToFirst()) {
            description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
            String formattedTime = time.substring(11);
            String timeAndDesc = formattedTime+"\n"+description;
            values.add(timeAndDesc);
            while (cursor.moveToNext()) {
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                formattedTime = time.substring(11);
                timeAndDesc = formattedTime+"\n"+description;
                values.add(timeAndDesc);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, values);


        dailyListView.setAdapter(adapter);
        dailyListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}
