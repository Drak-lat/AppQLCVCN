package com.example.quanlycongviecapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycongviecapp.Model.TaskModel;
import com.example.quanlycongviecapp.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    public interface OnTaskActionListener {
        void onEdit(TaskModel task);
        void onDelete(TaskModel task);
    }
    private final List<TaskModel> tasks;
    private final String[] priorityLabels;
    private final OnTaskActionListener listener;


    // Constructor
    public TaskAdapter(Context ctx, List<TaskModel> tasks, OnTaskActionListener listener) {
        this.tasks = tasks;
        this.priorityLabels = ctx.getResources()
                .getStringArray(R.array.priorities);
        this.listener = listener;

    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskModel task = tasks.get(position);

        holder.tvTitle.setText(task.getTitle());
        holder.tvDescription.setText(task.getDescription());
        holder.tvDueDate.setText(task.getDueDate().split("T")[0]);  // Hiển thị yyyy-MM-dd
        int p = task.getPriority(); // 1,2,3
        String label = (p >= 1 && p <= priorityLabels.length)
                ? priorityLabels[p - 1]
                : "Không xác định";
        holder.tvPriority.setText("Ưu tiên: " + label);
        holder.tvStatus.setText(task.isCompleted() ? "Đã hoàn thành" : "Chưa hoàn thành");
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(task));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(task));


    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // ViewHolder inner class
    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDueDate;
        TextView tvPriority;
        TextView tvStatus;
        ImageButton btnEdit, btnDelete;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle       = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDueDate     = itemView.findViewById(R.id.tvDueDate);
            tvPriority    = itemView.findViewById(R.id.tvPriority);
            tvStatus      = itemView.findViewById(R.id.tvStatus);
            btnEdit       = itemView.findViewById(R.id.btnEdit);
            btnDelete     = itemView.findViewById(R.id.btnDelete);
        }
    }
}
