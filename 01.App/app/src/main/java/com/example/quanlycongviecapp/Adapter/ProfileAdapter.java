package com.example.quanlycongviecapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycongviecapp.R;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private List<String> profiles;
    private int maxProfiles = 2;

    public ProfileAdapter(List<String> profiles) {
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < profiles.size()) {
            holder.tvProfile.setText(profiles.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(maxProfiles, profiles.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProfile = itemView.findViewById(R.id.tvProfile);
        }
    }
}