package com.example.quanlycongviecapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycongviecapp.Model.TaskModel;
import com.example.quanlycongviecapp.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<TaskModel> items; // Sửa thành List<TaskModel>
    private int maxItems = 1; // Chỉ hiển thị 1 item

    public TaskAdapter(List<TaskModel> taskList) {
        this.items = taskList; // Gán taskList vào items
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < items.size()) {
            TaskModel task = items.get(position);
            //holder.tvItem.setText(task.getTaskName()); // Giả sử TaskModel có phương thức getTaskName()
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(maxItems, items.size());
    }

    public void updateItems(List<TaskModel> newItems) {
        this.items = newItems; // Sửa thành List<TaskModel>
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem);
        }
    }
}