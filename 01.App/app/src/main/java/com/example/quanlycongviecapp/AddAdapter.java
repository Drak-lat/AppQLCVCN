package com.example.quanlycongviecapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.ViewHolder> {
    private List<String> adds;
    private int maxAdds = 2; // Giới hạn 2 item

    public AddAdapter(List<String> adds) {
        this.adds = adds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < adds.size()) {
            holder.tvAdd.setText(adds.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(maxAdds, adds.size()); // Giới hạn tối đa 2 item
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAdd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdd = itemView.findViewById(R.id.tvAdd);
        }
    }
}