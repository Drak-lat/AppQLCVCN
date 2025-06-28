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

public class TaskAdapter extends BaseAdapter {
    private Context context;
    private List<TaskModel> items;

    public TaskAdapter(Context context, List<TaskModel> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_task, null);
        }

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvItem = view.findViewById(R.id.tvItem);

        TaskModel task = items.get(position);
        tvTitle.setText(task.title); // Tiêu đề
        tvItem.setText(task.description + " (Hạn: " + task.dueDate + ")"); // Nội dung

        return view;
    }

    public void updateItems(List<TaskModel> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }
}