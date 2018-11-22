package com.example.huy.myfirstapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_DESCRIPTION;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_ID;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_TIME;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_1 = 123;

    private DatabaseHelper database;
    private DBManager dbManager;
    private ListView mainListView;
    private String formattedNewTime;
    private String fullFormattedNewTime;
    private String newDescription;
    private String description;
    private String time;
    private ListView listViewTaskMain;
    ArrayList<Task> taskArrayList = new ArrayList<>();
    TaskAdapter adapter;
    public static String CURRENT_DATE_FORMATTED;


    String[] columnNames = new String[]{COLUMN_ID, COLUMN_DESCRIPTION, COLUMN_TIME};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText((this), "onCreate() called!",
                Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_main);
        database = new DatabaseHelper(this);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        CURRENT_DATE_FORMATTED = df.format(date);

        listViewTaskMain = findViewById(R.id.list_view_Main);

        TextView theDate = findViewById(R.id.Main_Date);
        theDate.setText(CURRENT_DATE_FORMATTED);

        dbManager = new DBManager(this);
        dbManager.open();
        getData();
    }


    public void getData() {
        Cursor cursor = dbManager.fetch();
        if (cursor.moveToFirst()) {
            description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
            String shortTime = time.substring(11);
            taskArrayList.add(new Task(description, shortTime));
            while (cursor.moveToNext()) {
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                taskArrayList.add(new Task(description, shortTime));

                mainListView = findViewById(R.id.list_view_Main);

                adapter = new TaskAdapter(taskArrayList, MainActivity.this);
                mainListView.setAdapter(adapter);
                mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        Toast.makeText((this), "onStop() called",
                Toast.LENGTH_LONG).show();
        super.onStop();
    }


    public void goCalendar(View view) {
        Intent goCalendarIntent = new Intent(this, CalendarActivity.class);
        startActivityForResult(goCalendarIntent, REQUEST_CODE_1);
    }

    public void addTask(View view) {
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);

        int currentMinute = rightNow.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        int selectedHour = hourOfDay;
                        int selectedMinute = minute;
                        formattedNewTime = hourOfDay + ":" + minute;
                        fullFormattedNewTime = CURRENT_DATE_FORMATTED+"T"+formattedNewTime;
                        getDescription();
                    }
                }, currentHourIn24Format, currentMinute, true);
        timePickerDialog.show();
    }



    public boolean getDescription() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        final EditText editText = new EditText(MainActivity.this);
        alert.setTitle("Add new task: ");
        alert.setMessage("Enter your description for the chosen time: ");

        alert.setView(editText);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newDescription = editText.getText().toString();
                Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_LONG).show();
                notifyList();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newDescription = null;
            }
        });
        alert.show();
        if (newDescription == null) {
            return false;
        }
        return true;
    }

    public void notifyList() {
        taskArrayList.add(new Task(newDescription, formattedNewTime));
        dbManager.insert(newDescription, fullFormattedNewTime);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_1) {
            if (resultCode == RESULT_OK) {
                String date = data.getStringExtra("date");
                if (date != null) {
                    Intent dailyIntent = new Intent(this, DailyActivity.class);
                    dailyIntent.putExtra("date", date);
                    startActivity(dailyIntent);
                }
            }
        }
    }
}


