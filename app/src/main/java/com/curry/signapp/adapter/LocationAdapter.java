package com.curry.signapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.curry.signapp.R;
import com.curry.signapp.bean.SimpleLocation;

import java.util.List;

/**
 * Created by curry on 2017/4/4.
 */

public class LocationAdapter extends BaseAdapter {

    private List<SimpleLocation> locationList;
    private Context context;

    public LocationAdapter(Context context, List<SimpleLocation> locationList) {
        this.context = context;
        this.locationList = locationList;
    }

    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_location, null);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvLongitude = (TextView) convertView.findViewById(R.id.tvLongitude);
            holder.tvLatitude = (TextView) convertView.findViewById(R.id.tvLatitude);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SimpleLocation location = locationList.get(position);
        holder.tvTime.setText(location.getTime());
        holder.tvLongitude.setText(location.getLongitude());
        holder.tvLatitude.setText(location.getLatitude());
        return convertView;
    }

    private static class ViewHolder {
        TextView tvTime;
        TextView tvLongitude;
        TextView tvLatitude;
    }

}
