package com.example.quanlycongviecapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlycongviecapp.R;

import java.util.List;

public class ProfileAdapter extends BaseAdapter {
    private Context context;
    private List<String> profiles;
    private int maxProfiles = 2; // Giới hạn tối đa 2 item

    public ProfileAdapter(Context context, List<String> profiles) {
        this.context = context;
        this.profiles = profiles;
    }

    @Override
    public int getCount() {
        return Math.min(maxProfiles, profiles.size());
    }

    @Override
    public Object getItem(int position) {
        return profiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_profile, null);
        }

        TextView tvProfile = view.findViewById(R.id.tvProfile);

        String profile = profiles.get(position);
        tvProfile.setText(profile);

        return view;
    }

    public void updateProfiles(List<String> newProfiles) {
        this.profiles = newProfiles;
        notifyDataSetChanged();
    }
}