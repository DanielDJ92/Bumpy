package com.example.michael.bumpy;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.michael.bumpy.Model.Witness;

import java.util.ArrayList;

/**
 * Created by Dudu on 07/05/2016.
 */
public class CustomListAdapter extends BaseAdapter {

    private ArrayList<Witness> listData;

    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context context, ArrayList<Witness> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_dialog, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.phoneView = (TextView) convertView.findViewById(R.id.phone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(listData.get(position).getName());
        holder.phoneView.setText(listData.get(position).getPhone());

        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView phoneView;
    }

}
