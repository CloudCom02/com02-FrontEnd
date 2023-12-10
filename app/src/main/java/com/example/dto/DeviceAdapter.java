package com.example.dto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.com02.R;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    private List<DeviceDTO> deviceList;
    private LayoutInflater inflater;

    public DeviceAdapter(Context context, List<DeviceDTO> deviceList) {
        this.deviceList = deviceList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.device_list_item_adpater, parent);
            holder = new ViewHolder();

            holder.deviceNameTextView = view.findViewById(R.id.deviceNameTextView);
            holder.categoryTextView = view.findViewById(R.id.categoryTextView);
            holder.batteryLevelTextView = view.findViewById(R.id.batteryLevelTextView);
            holder.isNeedChargeTextView = view.findViewById(R.id.isNeedChargeTextView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        DeviceDTO device = deviceList.get(position);

        holder.deviceNameTextView.setText(device.getDeviceName());
        holder.categoryTextView.setText(device.getCategory());
        holder.batteryLevelTextView.setText(device.getBatteryLevel().toString());
        holder.isNeedChargeTextView.setText("Needs Charge");

        return view;
    }

    private static class ViewHolder {
        TextView deviceNameTextView;
        TextView categoryTextView;
        TextView batteryLevelTextView;
        TextView isNeedChargeTextView;
    }
}
