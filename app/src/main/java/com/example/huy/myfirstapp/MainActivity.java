package com.example.huy.myfirstapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_DESCRIPTION;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_ID;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_STATUS;
import static com.example.huy.myfirstapp.DatabaseHelper.COLUMN_TIME;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_1 = 123;
    private DatabaseHelper databaseHelper;
    public DBManager dbManager;
    private ListView mainListView;
    private String hourMinute;
    private String fullFormattedNewTime;
    private String newDescription;
    private String description;
    private String time;
    private String status;

    ArrayList<Task> taskArrayList = new ArrayList<>();
    TaskAdapter adapter;
    public static String YEAR_MONTH_DAY;


    String[] columnNames = new String[]{COLUMN_ID, COLUMN_DESCRIPTION, COLUMN_TIME};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        YEAR_MONTH_DAY = df.format(date);

        mainListView = findViewById(R.id.list_view_Main);
        TextView theDate = findViewById(R.id.Main_Date);
        theDate.setText(YEAR_MONTH_DAY);
        dbManager = new DBManager(this);
        dbManager.open();
        dbManager.insertMain();
        getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void getData() {
        Cursor cursor = dbManager.fetch();
        if (cursor.moveToFirst()) {
            do {
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));

                String shortTime = time.substring(11);
                taskArrayList.add(new Task(description, shortTime, status));
            }
            while (cursor.moveToNext());
            adapter = new TaskAdapter(taskArrayList, MainActivity.this);
            mainListView.setAdapter(adapter);
            mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }
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
                        hourMinute = hourOfDay + ":" + minute;
                        fullFormattedNewTime = YEAR_MONTH_DAY + "T" + hourMinute;
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
                notifyListAfterAdding();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newDescription = description;
                notifyListAfterAdding();
            }
        });
        alert.show();
        if (newDescription == null) {
            return false;
        }
        return true;
    }


    public void notifyListAfterAdding() {
        taskArrayList.add(new Task(newDescription, hourMinute, "0"));
        dbManager.insert(newDescription, fullFormattedNewTime);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        Cursor cursor = dbManager.fetch();
        if (cursor.moveToFirst()) {
            int requestCode = 0;
            do {
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
                if (status.equals("0")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long milis = date.getTime();
                    AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
                    Intent sendAlarmService = new Intent(this, AlarmReceiver.class);
                    sendAlarmService.putExtra("time", time);
                    sendAlarmService.putExtra("description", description);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, sendAlarmService, PendingIntent.FLAG_UPDATE_CURRENT);
                    requestCode++;
                    if (milis > System.currentTimeMillis()) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, milis, pendingIntent);
                    }
                }
            } while (cursor.moveToNext());
        }
        super.onStop();
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


