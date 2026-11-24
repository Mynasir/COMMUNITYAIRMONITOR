package com.communityairmonitor.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.communityairmonitor.R;
import com.communityairmonitor.model.AirAlert;

import java.util.ArrayList;
import java.util.List;

public class AlertListAdapter extends RecyclerView.Adapter<AlertListAdapter.AlertViewHolder> {

    public interface OnBroadcastClickListener {
        void onBroadcastClick(AirAlert alert);
    }

    private final List<AirAlert> alertList = new ArrayList<>();
    private OnBroadcastClickListener listener;

    public void setAlertList(List<AirAlert> list) {
        this.alertList.clear();
        if (list != null) {
            this.alertList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setOnBroadcastClickListener(OnBroadcastClickListener listener) {
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
        AirAlert alert = alertList.get(position);
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

        public void bind(AirAlert alert, OnBroadcastClickListener listener) {
            tvAlertCategory.setText(alert.getCategory().getLabel());
            tvAlertNodeId.setText("ID: " + alert.getNodeId());
            tvAlertTime.setText(alert.getTimeAgo());
            tvAlertLocation.setText(alert.getLocation());
            tvAlertTitle.setText(alert.getTitle());
            tvAlertReadout.setText(String.format("%.1f µg/m³", alert.getPm25Value()));
            tvAlertPrecaution.setText("Precaution: " + alert.getPrecaution());

            if (alert.getCategory() == AirAlert.Category.HAZARDOUS) {
                tvAlertCategory.setBackgroundColor(Color.parseColor("#93000A"));
                tvAlertCategory.setTextColor(Color.parseColor("#FFDAD6"));
            } else if (alert.getCategory() == AirAlert.Category.WARNING) {
                tvAlertCategory.setBackgroundColor(Color.parseColor("#FFB95F"));
                tvAlertCategory.setTextColor(Color.parseColor("#472A00"));
            } else {
                tvAlertCategory.setBackgroundColor(Color.parseColor("#272A32"));
                tvAlertCategory.setTextColor(Color.parseColor("#4CD7F6"));
            }

            if (alert.isBroadcasting()) {
                btnBroadcast.setText("Civic Broadcast Active ✓");
                btnBroadcast.setAlpha(0.6f);
            } else {
                btnBroadcast.setText("Broadcast Civic Alert");
                btnBroadcast.setAlpha(1.0f);
            }

            btnBroadcast.setOnClickListener(v -> {
                alert.setBroadcasting(!alert.isBroadcasting());
                if (listener != null) listener.onBroadcastClick(alert);
            });
        }
    }
}
