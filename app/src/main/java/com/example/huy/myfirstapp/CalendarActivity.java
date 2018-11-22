package com.example.huy.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;


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
                String formattedDate = year + "-" + realMonth + "-" + dayOfMonth;
                chosenDate = formattedDate;
                DBManager.setPickedDate();

                Intent returnDateIntent = getIntent();
                returnDateIntent.putExtra("date", formattedDate);
                setResult(RESULT_OK, returnDateIntent);
                finish();
            }
        });
    }
}
