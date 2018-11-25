package com.example.huy.myfirstapp;

import android.app.AlertDialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import static com.example.huy.myfirstapp.MainActivity.dbManager;


public class TaskAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Task> list;
    private Context context;
    TextView tvTime;
    TextView tvDescription;

    public TaskAdapter(ArrayList<Task> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public int getPosition() {
        return position;
    }

    int position;

    String description;

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
            if (view==null) {
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

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
//                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                description = tvDescription.getText().toString();
                dbManager.delete(description);
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

            this.position = position;
            description = tvDescription.getText().toString();

            return view;
}

}
