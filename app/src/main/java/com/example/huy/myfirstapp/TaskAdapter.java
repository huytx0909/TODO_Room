package com.example.huy.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class TaskAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Task> list;
    private Context context;

    public TaskAdapter(ArrayList<Task> list, Context context) {
        this.list = list;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_item, null);

            TextView tvTime = view.findViewById(R.id.textView_timeMain);
            TextView tvDescription = view.findViewById(R.id.textView_DescriptionMain);
            CheckBox checkBox = view.findViewById(R.id.checkBox_Main);

            tvTime.setText(list.get(position).getAppointedTime());
            tvDescription.setText(list.get(position).getDescription());


            return view;
}

}
