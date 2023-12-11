package com.example.dto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.com02.R;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    private List<DeviceDTO> deviceList;
    private LayoutInflater inflater;
    private Context context;

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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list_item, parent, false);

                holder = new ViewHolder();
                holder.deviceNameTextView = view.findViewById(R.id.deviceNameTextView);
                holder.categoryTextView = view.findViewById(R.id.categoryTextView);
                holder.batteryLevelTextView = view.findViewById(R.id.batteryLevelTextView);
                holder.isChargeTextView = view.findViewById(R.id.isNeedChargeTextView);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            DeviceDTO device = deviceList.get(position);

            // 뷰에 데이터 설정
            holder.deviceNameTextView.setText(device.getDeviceName());
            holder.categoryTextView.setText(device.getCategory());
            holder.batteryLevelTextView.setText(device.getBatteryLevel().toString());
            holder.isChargeTextView.setText("충전 중");

            return view;
        }

        private static class ViewHolder {
            TextView deviceNameTextView;
            TextView categoryTextView;
            TextView batteryLevelTextView;
            TextView isChargeTextView;
        }
    }
