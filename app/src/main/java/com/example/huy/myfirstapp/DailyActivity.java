package com.example.huy.myfirstapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;
import java.util.concurrent.Executors;


import static com.example.huy.myfirstapp.CalendarActivity.CHOSEN_DATE;

public class DailyActivity extends AppCompatActivity {
    private TextView tvDaily;
    private ListView dailyListView;
    String description;
    String time;
    String status;
    TaskAdapter adapter;
    String selectedHourMinute;
    String fullFormattedNewTime;
    String newDescription;
    private TaskDatabase taskDatabase;
    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_daily);

        dailyListView = findViewById(R.id.listView_Daily);

        taskDatabase = TaskDatabase.getDatabaseInstance(this);
        Intent getDateIntent = getIntent();
        String date = getDateIntent.getStringExtra("date");
        tvDaily = findViewById(R.id.text_thedate);
        tvDaily.setText(date);
        getData();
        super.onCreate(savedInstanceState);
    }

    public void getData() {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String timeForQuery = "%" + CHOSEN_DATE + "%";
                taskList = taskDatabase.taskDao().loadAllByTime(timeForQuery);
                Log.e("time string: ", timeForQuery);
                Log.e("taskList size", "" + (taskList.size()));
                for (Task task : taskList) {
                    description = task.getDescription();
                    time = task.getAppointedTime();
                    status = task.getStatus();
                    adapter = new TaskAdapter(taskList, DailyActivity.this);
                    dailyListView.setAdapter(adapter);
                    dailyListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                }
            }
        });
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
                        selectedHourMinute = hourOfDay + ":" + minute;
                        fullFormattedNewTime = CHOSEN_DATE + "T" + selectedHourMinute;
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
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        taskDatabase.taskDao().insert(new Task(newDescription, fullFormattedNewTime, "0"));
                        taskList.add(new Task(newDescription, fullFormattedNewTime, "0"));
                    }
                });
                Toast.makeText(DailyActivity.this, "Added", Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newDescription = description;
            }
        });
        alert.show();
        if (newDescription == null) {
            return false;
        }
        return true;
    }


    @Override
    protected void onStop() {
        int requestCode = 0;
        for (Task task : taskList) {
            description = task.getDescription();
            time = task.getAppointedTime();
            status = task.getStatus();

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
        }
        super.onStop();
    }
}

