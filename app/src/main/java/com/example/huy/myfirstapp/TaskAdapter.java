package com.example.huy.myfirstapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.huy.myfirstapp.MainActivity.YEAR_MONTH_DAY;
import static com.example.huy.myfirstapp.MainActivity.dbManager;


public class TaskAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Task> list;
    private Context context;
    private TextView tvTime;
    private TextView tvDescription;
    int selectedHour;
    int selectedMinute;
    String hourMinute;
    int position;
    String description;

    private String fullFormattedNewTime;
    private String newDescription;

    public TaskAdapter(ArrayList<Task> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public int getPosition() {
        return position;
    }


    public String getDescription() {
        return description;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_item, null);
        }

        tvTime = view.findViewById(R.id.textView_timeMain);
        tvDescription = view.findViewById(R.id.textView_DescriptionMain);
        CheckBox checkBox = view.findViewById(R.id.checkBox_Main);
        tvTime.setText(list.get(position).getAppointedTime());
        tvDescription.setText(list.get(position).getDescription());

        ImageButton deleteBtn = view.findViewById(R.id.imageButton_Delete);
        ImageButton editBtn = view.findViewById(R.id.imageButton_Edit);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Confirm Delete");
                alert.setMessage("Are you sure you want to delete this task?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        description = tvDescription.getText().toString();
                        dbManager.delete(description);
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                alert.show();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar rightNow = Calendar.getInstance();
                int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);

                int currentMinute = rightNow.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                selectedHour = hourOfDay;
                                selectedMinute = minute;
                                hourMinute = hourOfDay + ":" + minute;
                                fullFormattedNewTime = YEAR_MONTH_DAY + "T" + hourMinute;
                            }
                        }, currentHourIn24Format, currentMinute, true);


                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                final EditText editText = new EditText(context);
                alert.setTitle("Edit task:");
                alert.setMessage("Enter your description:");

                alert.setView(editText);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newDescription = editText.getText().toString();
                        dbManager.update(description, newDescription, fullFormattedNewTime);
                        list.set(position, new Task(newDescription, hourMinute));
                        Toast.makeText(context, "Edited", Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newDescription = null;
                    }
                });
                alert.show();
                timePickerDialog.show();
            }
        });

        this.position = position;
        description = tvDescription.getText().toString();

        return view;
    }

}
