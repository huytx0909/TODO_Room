package com.example.huy.myfirstapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.List;

import static com.example.huy.myfirstapp.MainActivity.CURRENT_YEAR_MONTH_DAY;
import static com.example.huy.myfirstapp.MainActivity.taskDatabase;


public class TaskAdapter extends BaseAdapter implements ListAdapter {
    private List<Task> list;
    private Context context;
    int selectedHour;
    int selectedMinute;
    String hourMinute;
    int position;
    String description;
    String status;
    int tag;
    private String fullFormattedNewTime;
    private String newDescription;

    public TaskAdapter(List<Task> list, Context context) {
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
        final ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_item, null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = view.findViewById(R.id.checkBox_Main);
            viewHolder.description = view.findViewById(R.id.textView_DescriptionMain);
            viewHolder.time = view.findViewById(R.id.textView_timeMain);
            view.setTag(viewHolder);

            viewHolder.description.setText(list.get(position).getDescription());
            viewHolder.time.setText(list.get(position).getAppointedTime().substring(11));
            status = list.get(position).getStatus();

        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.checkBox.setOnCheckedChangeListener(null);


        viewHolder.checkBox.setOnCheckedChangeListener(null);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                description = list.get(getPosition).getDescription();
                final String checkBoxStatus;
                if(isChecked) {
                    checkBoxStatus = "1";
                } else {
                    checkBoxStatus = "0";
                }
                list.get(getPosition).setStatus(checkBoxStatus);
                new Thread() {
                    @Override
                    public void run() {
                        taskDatabase.taskDao().setStatus(checkBoxStatus, description);
                    }
                }.start();
            }
        });
        viewHolder.checkBox.setTag(position);
        String isChecked = list.get(position).getStatus();
        if (isChecked.equals("1")) {
            viewHolder.checkBox.setChecked(true);
        } else {
            viewHolder.checkBox.setChecked(false);
        }


















































        ImageButton deleteBtn = view.findViewById(R.id.imageButton_Delete);
        ImageButton editBtn = view.findViewById(R.id.imageButton_Edit);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Confirm Delete");
                alert.setMessage("Are you sure you want to delete this task?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        description = list.get(position).getDescription();
                        new Thread() {
                            @Override
                            public void run() {
                                taskDatabase.taskDao().delete(taskDatabase.taskDao().get1Task(description));
                                list.remove(position);
                            }
                        }.start();
//                        Toast.makeText(context, "removed: " + description, Toast.LENGTH_SHORT).show();
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
                                new Thread() {
                                    @Override
                                    public void run() {
                                        fullFormattedNewTime =  CURRENT_YEAR_MONTH_DAY
                                                + "T" + hourMinute;
                                    }
                                }.start();
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
                        new Thread() {
                            @Override
                            public void run() {
                                description = list.get(position).getDescription();
                                status = list.get(position).getStatus();
                                newDescription = editText.getText().toString();
                                taskDatabase.taskDao().update(description, newDescription, fullFormattedNewTime);
                                list.set(position, new Task(newDescription, fullFormattedNewTime, "0"));
                            }
                        }.start();
                        Toast.makeText(context, "Edited", Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                        Log.e("EDIT", "code ran here");
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newDescription = description;
                    }
                });
                alert.show();
                timePickerDialog.show();
            }
        });
        tag++;
        return view;
    }

    private class ViewHolder {
        private TextView description;
        private TextView time;
        private CheckBox checkBox;
    }
}
