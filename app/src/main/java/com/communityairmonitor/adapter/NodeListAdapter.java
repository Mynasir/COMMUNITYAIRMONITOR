package com.communityairmonitor.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.communityairmonitor.R;
import com.communityairmonitor.model.AirQualityNode;

import java.util.ArrayList;
import java.util.List;

public class NodeListAdapter extends RecyclerView.Adapter<NodeListAdapter.NodeViewHolder> {

    public interface OnNodeClickListener {
        void onNodeClick(AirQualityNode node);
    }

    private final List<AirQualityNode> nodeList = new ArrayList<>();
    private OnNodeClickListener listener;

    public void setNodeList(List<AirQualityNode> list) {
        this.nodeList.clear();
        if (list != null) {
            this.nodeList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setOnNodeClickListener(OnNodeClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_node_card, parent, false);
        return new NodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NodeViewHolder holder, int position) {
        AirQualityNode node = nodeList.get(position);
        holder.bind(node, listener);
    }

    @Override
    public int getItemCount() {
        return nodeList.size();
    }

    static class NodeViewHolder extends RecyclerView.ViewHolder {
        TextView tvNodeId, tvStatusLabel, tvUptimeBadge, tvNodeName, tvNodeAqi, tvNodePm25, tvNodeRssi;
        View statusDot;

        public NodeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNodeId = itemView.findViewById(R.id.tvNodeId);
            tvStatusLabel = itemView.findViewById(R.id.tvStatusLabel);
            tvUptimeBadge = itemView.findViewById(R.id.tvUptimeBadge);
            tvNodeName = itemView.findViewById(R.id.tvNodeName);
            tvNodeAqi = itemView.findViewById(R.id.tvNodeAqi);
            tvNodePm25 = itemView.findViewById(R.id.tvNodePm25);
            tvNodeRssi = itemView.findViewById(R.id.tvNodeRssi);
            statusDot = itemView.findViewById(R.id.statusDot);
        }

        public void bind(AirQualityNode node, OnNodeClickListener listener) {
            tvNodeId.setText(node.getId());
            tvNodeName.setText(node.getName());
            tvStatusLabel.setText(node.getStatus().getLabel());
            tvUptimeBadge.setText(node.getUptime() + " [" + (node.getAqi() < 50 ? "A+" : "B") + "]");
            tvNodeAqi.setText(String.valueOf(node.getAqi()));
            tvNodePm25.setText(String.format("%.1f µg/m³", node.getPm2_5()));
            tvNodeRssi.setText(node.getRssi() + " dBm");

            if (node.getAqi() > 100) {
                tvNodeAqi.setTextColor(Color.parseColor("#EF4444"));
                statusDot.setBackgroundResource(R.drawable.shape_dot_red);
            } else {
                tvNodeAqi.setTextColor(Color.parseColor("#00FF66"));
                statusDot.setBackgroundResource(R.drawable.shape_dot_green);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onNodeClick(node);
            });
        }
    }
}
