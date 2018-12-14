package com.example.huy.myfirstapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_1 = 123;
    private ListView mainListView;
    private TextView mainDate;
    private String hourMinute;
    private String fullFormattedNewTime;
    private String newDescription;
    private String description;
    private String time;
    private String status;
    public static TaskDatabase taskDatabase;
    private List<Task> taskList;
    TaskAdapter adapter;
    public static String CURRENT_YEAR_MONTH_DAY;
    public static String CURRENT_FULL_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat year_month_day = new SimpleDateFormat("yyyy-MM-dd");
        CURRENT_YEAR_MONTH_DAY = year_month_day.format(date);

        SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        CURRENT_FULL_TIME = fullFormat.format(date);

        mainListView = findViewById(R.id.list_view_Main);
        mainDate = findViewById(R.id.Main_Date);
        mainDate.setText(CURRENT_YEAR_MONTH_DAY);
        taskDatabase = TaskDatabase.getDatabaseInstance(this);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            //
        }
        getData();
    }

    public void getData() {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String timeForQuery = "'%" + CURRENT_YEAR_MONTH_DAY + "%'";

                taskList = taskDatabase.taskDao().getAll();
                Log.e("time string: ", timeForQuery);
                Log.e("taskList size", "" + (taskList.size()));
                for (Task task : taskList) {
                    description = task.getDescription();
                    time = task.getAppointedTime();
                    status = task.getIsChecked();
                    adapter = new TaskAdapter(taskList, MainActivity.this);
                    mainListView.setAdapter(adapter);
                    mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                }
            }
        });
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
                        fullFormattedNewTime = CURRENT_YEAR_MONTH_DAY + "T" + hourMinute;
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
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        taskDatabase.taskDao().insert(new Task(newDescription, fullFormattedNewTime, "0"));
                        taskList.add(new Task(newDescription, fullFormattedNewTime, "0"));
                    }
                });
                Toast.makeText(MainActivity.this, "taskList after add: " + taskList.size(), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
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
        } else {
            return true;
        }
    }


    @Override
    protected void onStop() {
        Log.e("task list size in onStop", "" + taskList.size());
        int requestCode = 0;
        for (Task task : taskList) {
            description = task.getDescription();
            time = task.getAppointedTime();
            status = task.getIsChecked();

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
//        Cursor cursor = dbManager.fetch();
//        if (cursor.moveToFirst()) {
//            int requestCode = 0;
//            do {
//                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
//                time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
//                status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
//                if (status.equals("0")) {
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//                    Date date = null;
//                    try {
//                        date = simpleDateFormat.parse(time);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    long milis = date.getTime();
//                    AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
//                    Intent sendAlarmService = new Intent(this, AlarmReceiver.class);
//                    sendAlarmService.putExtra("time", time);
//                    sendAlarmService.putExtra("description", description);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, sendAlarmService, PendingIntent.FLAG_UPDATE_CURRENT);
//                    requestCode++;
//                    if (milis > System.currentTimeMillis()) {
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, milis, pendingIntent);
//                    }
//                }
//            } while (cursor.moveToNext());
//        }
            super.onStop();
        }
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


