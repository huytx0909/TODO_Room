package com.example.huy.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;


public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    public static String chosenDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int realMonth = month + 1;
                chosenDate = year + "-" + realMonth + "-" + dayOfMonth;
                Toast.makeText(CalendarActivity.this, chosenDate, Toast.LENGTH_SHORT).show();
                DBManager.setPickedDate();


                Intent returnDateIntent = getIntent();
                returnDateIntent.putExtra("date", chosenDate);
                setResult(RESULT_OK, returnDateIntent);
                finish();
            }
        });
    }
}
