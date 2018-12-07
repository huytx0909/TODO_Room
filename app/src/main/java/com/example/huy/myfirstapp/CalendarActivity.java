package com.example.huy.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

import static com.example.huy.myfirstapp.MainActivity.YEAR_MONTH_DAY;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    public static String CHOSEN_DATE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int realMonth = month + 1;
                if (dayOfMonth < 10) {
                    CHOSEN_DATE = year + "-" + realMonth + "-" + "0" + dayOfMonth;
                } else {
                    CHOSEN_DATE = year + "-" + realMonth + "-" + dayOfMonth;
                }
                Toast.makeText(CalendarActivity.this, CHOSEN_DATE, Toast.LENGTH_SHORT).show();
                if (CHOSEN_DATE.equals(YEAR_MONTH_DAY)) {
                    Intent backToHome = new Intent(CalendarActivity.this, MainActivity.class);
                    CalendarActivity.this.startActivity(backToHome);
                } else {
                    DBManager.setPickedDate();
                    Intent returnDateIntent = getIntent();
                    returnDateIntent.putExtra("date", CHOSEN_DATE);
                    setResult(RESULT_OK, returnDateIntent);
                }
                finish();
            }
        });
    }
}
