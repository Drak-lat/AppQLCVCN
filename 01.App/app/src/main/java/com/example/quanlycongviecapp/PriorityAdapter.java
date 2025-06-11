package com.example.quanlycongviecapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PriorityAdapter extends RecyclerView.Adapter<PriorityAdapter.ViewHolder> {
    private List<TaskModel> priorities; // Sửa thành List<TaskModel>
    private int maxPriorities = 3; // Chỉ hiển thị tối đa 3 item

    public PriorityAdapter(List<TaskModel> priorities) {
        this.priorities = priorities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_priority, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < priorities.size()) {
            TaskModel task = priorities.get(position);
            //holder.tvPriority.setText(task.getPriority()); // Giả sử TaskModel có getPriority()
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(maxPriorities, priorities.size());
    }

    public void updatePriorities(List<TaskModel> newPriorities) { // Thêm phương thức update
        this.priorities = newPriorities;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPriority;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPriority = itemView.findViewById(R.id.tvPriority);
        }
    }
}