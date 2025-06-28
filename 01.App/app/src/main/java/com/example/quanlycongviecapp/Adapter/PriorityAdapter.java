package com.example.quanlycongviecapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlycongviecapp.Model.TaskModel;
import com.example.quanlycongviecapp.R;

import java.util.List;

public class PriorityAdapter extends BaseAdapter {
    private Context context;
    private List<TaskModel> priorities;
    private int maxPriorities = 3; // Giới hạn tối đa 3 item

    public PriorityAdapter(Context context, List<TaskModel> priorities) {
        this.context = context;
        this.priorities = priorities;
    }

    @Override
    public int getCount() {
        return Math.min(maxPriorities, priorities.size());
    }

    @Override
    public Object getItem(int position) {
        return priorities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_priority, null);
        }

        TextView tvPriority = view.findViewById(R.id.tvPriority);

        TaskModel task = priorities.get(position);
        tvPriority.setText(String.valueOf(task.priority));

        return view;
    }

    public void updatePriorities(List<TaskModel> newPriorities) {
        this.priorities = newPriorities;
        notifyDataSetChanged();
    }
}