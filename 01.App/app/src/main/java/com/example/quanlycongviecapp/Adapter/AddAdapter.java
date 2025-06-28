package com.example.quanlycongviecapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlycongviecapp.R;

import java.util.List;

public class AddAdapter extends BaseAdapter {
    private Context context;
    private List<String> adds;
    private int maxAdds = 2; // Giới hạn tối đa 2 item

    public AddAdapter(Context context, List<String> adds) {
        this.context = context;
        this.adds = adds;
    }

    @Override
    public int getCount() {
        return Math.min(maxAdds, adds.size());
    }

    @Override
    public Object getItem(int position) {
        return adds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_add, null);
        }

        TextView tvAdd = view.findViewById(R.id.tvAdd);

        String add = adds.get(position);
        tvAdd.setText(add);

        return view;
    }

    public void updateAdds(List<String> newAdds) {
        this.adds = newAdds;
        notifyDataSetChanged();
    }
}