package com.example.quanlycongviecapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;


import com.example.quanlycongviecapp.Activity.TaskFragment;
import com.example.quanlycongviecapp.Model.Plan;  // <-- phải import đúng
import com.example.quanlycongviecapp.R;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<Plan> planList;
    private OnPlanClickListener listener;

    public interface OnPlanClickListener {
        void onPlanClick(Plan plan);
        void onEdit(Plan plan);
        void onDelete(Plan plan);
    }

    public PlanAdapter(List<Plan> planList, OnPlanClickListener listener) {
        this.planList = planList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);
        holder.tvPlanName.setText(plan.getName());
        holder.tvPlanDesc.setText(plan.getDescription());
        holder.tvPlanDates.setText(plan.getStartDate() + " - " + plan.getEndDate());


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onPlanClick(plan);
        });
        holder.imgEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(plan);
        });
        holder.imgDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(plan);
        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlanName, tvPlanDesc, tvPlanDates;
        ImageView imgEdit, imgDelete;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlanName = itemView.findViewById(R.id.tvPlanName);
            tvPlanDesc = itemView.findViewById(R.id.tvPlanDesc);
            tvPlanDates = itemView.findViewById(R.id.tvPlanDates);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
