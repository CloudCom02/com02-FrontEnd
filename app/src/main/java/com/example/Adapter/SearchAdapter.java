package com.example.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dto.DeviceData;
import com.example.com02.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private final List<DeviceData> dataList;

    public SearchAdapter(List<DeviceData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_device_block, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceData data = dataList.get(position);
        holder.bind(data);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.search_btn) // 로딩 중에 표시할 이미지
                .error(R.drawable.your_logo); // 에러 발생 시 표시할 이미지

        Glide.with(holder.imageView.getContext())
                .load(data.getImageURI())
                .apply(requestOptions)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameView;
        private final TextView contentsView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.device_blockImageView);
            nameView = itemView.findViewById(R.id.device_blockNameTextView);
            contentsView = itemView.findViewById(R.id.device_blockTypeTextView);
        }

        public void bind(DeviceData data) {
            nameView.setText(data.getName());
            contentsView.setText(data.getContents());
        }
    }
}
