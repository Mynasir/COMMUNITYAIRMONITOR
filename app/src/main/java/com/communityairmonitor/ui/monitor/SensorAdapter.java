package com.communityairmonitor.ui.monitor;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.communityairmonitor.R;
import com.communityairmonitor.data.local.entity.SensorEntity;

import java.util.ArrayList;
import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder> {

    public interface OnSensorClickListener {
        void onSensorClick(SensorEntity sensor);
    }

    private final List<SensorEntity> sensorList = new ArrayList<>();
    private OnSensorClickListener listener;

    public void setSensorList(List<SensorEntity> list) {
        this.sensorList.clear();
        if (list != null) {
            this.sensorList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setOnSensorClickListener(OnSensorClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sensor, parent, false);
        return new SensorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {
        SensorEntity sensor = sensorList.get(position);
        holder.bind(sensor, listener);
    }

    @Override
    public int getItemCount() {
        return sensorList.size();
    }

    static class SensorViewHolder extends RecyclerView.ViewHolder {
        TextView tvSensorId, tvSensorLocation, tvSensorStatus, tvSensorName, tvSensorPm25, tvSensorPm10, tvSensorTempHumid;

        public SensorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSensorId = itemView.findViewById(R.id.tvSensorId);
            tvSensorLocation = itemView.findViewById(R.id.tvSensorLocation);
            tvSensorStatus = itemView.findViewById(R.id.tvSensorStatus);
            tvSensorName = itemView.findViewById(R.id.tvSensorName);
            tvSensorPm25 = itemView.findViewById(R.id.tvSensorPm25);
            tvSensorPm10 = itemView.findViewById(R.id.tvSensorPm10);
            tvSensorTempHumid = itemView.findViewById(R.id.tvSensorTempHumid);
        }

        public void bind(SensorEntity sensor, OnSensorClickListener listener) {
            tvSensorId.setText(sensor.getId());
            tvSensorName.setText(sensor.getName());
            tvSensorLocation.setText(sensor.getLocation());
            tvSensorStatus.setText(sensor.getStatus());

            if ("OPERATIONAL".equalsIgnoreCase(sensor.getStatus())) {
                tvSensorStatus.setTextColor(Color.parseColor("#267A55"));
            } else {
                tvSensorStatus.setTextColor(Color.parseColor("#B7791F"));
            }

            tvSensorPm25.setText("18.4 µg/m³");
            tvSensorPm10.setText("32.1 µg/m³");
            tvSensorTempHumid.setText("24.5°C / 58%");

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onSensorClick(sensor);
            });
        }
    }
}
