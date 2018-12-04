package com.example.huy.myfirstapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_DESCRIPTION;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_STATUS;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_TIME;
import static com.example.huy.myfirstapp.CalendarActivity.chosenDate;

public class DailyActivity extends AppCompatActivity {
    private TextView theDate;
    public DBManager dbManager;
    private ListView dailyListView;
    String description;
    String time;
    String status;
    TaskAdapter adapter;
    String selectedHourMinunite;
    String fullFormattedNewTime;
    String newDescription;
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
        CheckBox checkBox_Daily = findViewById(R.id.checkBox_Main);
        dbManager = new DBManager(this);
        dbManager.open();
        getData();

    }

    public void getData() {
        Cursor cursor = dbManager.dailyFetch();
        if (cursor.moveToFirst()) {
            description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
            status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
            String shortTime = time.substring(11);
            taskArrayList.add(new Task(description, shortTime,status));
            while (cursor.moveToNext()) {
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
                shortTime = time.substring(11);
                taskArrayList.add(new Task(description, shortTime, status));
            }
            adapter = new TaskAdapter(taskArrayList, DailyActivity.this);
            dailyListView.setAdapter(adapter);
            dailyListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }
    }

    public void addTaskDaily(View view) {
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);

        int currentMinute = rightNow.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(DailyActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        selectedHourMinunite = hourOfDay + ":" + minute;
                        fullFormattedNewTime = chosenDate + "T" + selectedHourMinunite;
                        getDescription();
                    }
                }, currentHourIn24Format, currentMinute, true);
        timePickerDialog.show();
    }

    public boolean getDescription() {
        AlertDialog.Builder alert = new AlertDialog.Builder(DailyActivity.this);
        final EditText editText = new EditText(DailyActivity.this);
        alert.setTitle("Add new task: ");
        alert.setMessage("Enter your description for the chosen time: ");

        alert.setView(editText);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newDescription = editText.getText().toString();
                Toast.makeText(DailyActivity.this, "Added", Toast.LENGTH_LONG).show();
                notifyListAfterAdding();
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

    public void notifyListAfterAdding() {
        taskArrayList.add(new Task(newDescription, selectedHourMinunite, status));
        dbManager.insert(newDescription, fullFormattedNewTime);
        adapter.notifyDataSetChanged();
    }

    //todo also set alarms for the unchecked items.
    @Override
    protected void onStop() {
        super.onStop();
    }
}
