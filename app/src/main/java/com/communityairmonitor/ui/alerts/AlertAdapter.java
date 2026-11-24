package com.communityairmonitor.ui.alerts;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.communityairmonitor.R;
import com.communityairmonitor.data.local.entity.AlertEntity;

import java.util.ArrayList;
import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {

    public interface OnAlertActionListener {
        void onMarkRead(AlertEntity alert);
        void onResolve(AlertEntity alert);
    }

    private final List<AlertEntity> alertList = new ArrayList<>();
    private OnAlertActionListener listener;

    public void setAlertList(List<AlertEntity> list) {
        this.alertList.clear();
        if (list != null) {
            this.alertList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setOnAlertActionListener(OnAlertActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert_card, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        AlertEntity alert = alertList.get(position);
        holder.bind(alert, listener);
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    static class AlertViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlertCategory, tvAlertNodeId, tvAlertTime, tvAlertLocation, tvAlertTitle, tvAlertReadout, tvAlertPrecaution;
        Button btnBroadcast;

        public AlertViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlertCategory = itemView.findViewById(R.id.tvAlertCategory);
            tvAlertNodeId = itemView.findViewById(R.id.tvAlertNodeId);
            tvAlertTime = itemView.findViewById(R.id.tvAlertTime);
            tvAlertLocation = itemView.findViewById(R.id.tvAlertLocation);
            tvAlertTitle = itemView.findViewById(R.id.tvAlertTitle);
            tvAlertReadout = itemView.findViewById(R.id.tvAlertReadout);
            tvAlertPrecaution = itemView.findViewById(R.id.tvAlertPrecaution);
            btnBroadcast = itemView.findViewById(R.id.btnBroadcast);
        }

        public void bind(AlertEntity alert, OnAlertActionListener listener) {
            tvAlertCategory.setText(alert.getSeverity());
            tvAlertNodeId.setText("ID: " + alert.getSensorId());
            tvAlertTime.setText("Recent");
            tvAlertLocation.setText(alert.getSensorId());
            tvAlertTitle.setText(alert.getAlertType());
            tvAlertReadout.setText(String.format("%.1f µg/m³", alert.getMeasuredValue()));
            tvAlertPrecaution.setText(alert.getMessage());

            if ("High".equalsIgnoreCase(alert.getSeverity()) || "Critical".equalsIgnoreCase(alert.getSeverity())) {
                tvAlertCategory.setBackgroundColor(Color.parseColor("#7F1D1D"));
                tvAlertCategory.setTextColor(Color.WHITE);
            } else {
                tvAlertCategory.setBackgroundColor(Color.parseColor("#B7791F"));
                tvAlertCategory.setTextColor(Color.WHITE);
            }

            btnBroadcast.setText(alert.isResolved() ? "Resolved ✓" : "Mark Resolved");
            btnBroadcast.setOnClickListener(v -> {
                if (listener != null) listener.onResolve(alert);
            });
        }
    }
}
