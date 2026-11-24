package com.communityairmonitor.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.communityairmonitor.R;
import com.communityairmonitor.model.AirQualityNode;
import com.communityairmonitor.view.GisMapCanvasView;
import com.communityairmonitor.viewmodel.AirViewModel;

public class LiveMapFragment extends Fragment {

    private AirViewModel viewModel;
    private GisMapCanvasView gisMapCanvas;
    private TextView tvMapNodeId, tvMapNodeName, tvMapNodeAqi, tvMapNodeParams;

    private boolean showNodes = true;
    private boolean showHeatmap = true;
    private boolean showWind = true;
    private boolean showTopo = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_live_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AirViewModel.class);

        gisMapCanvas = view.findViewById(R.id.gisMapCanvas);
        tvMapNodeId = view.findViewById(R.id.tvMapNodeId);
        tvMapNodeName = view.findViewById(R.id.tvMapNodeName);
        tvMapNodeAqi = view.findViewById(R.id.tvMapNodeAqi);
        tvMapNodeParams = view.findViewById(R.id.tvMapNodeParams);

        view.findViewById(R.id.btnToggleNodes).setOnClickListener(v -> {
            showNodes = !showNodes;
            updateMapLayers();
        });
        view.findViewById(R.id.btnToggleHeatmap).setOnClickListener(v -> {
            showHeatmap = !showHeatmap;
            updateMapLayers();
        });
        view.findViewById(R.id.btnToggleWind).setOnClickListener(v -> {
            showWind = !showWind;
            updateMapLayers();
        });
        view.findViewById(R.id.btnToggleTopo).setOnClickListener(v -> {
            showTopo = !showTopo;
            updateMapLayers();
        });

        gisMapCanvas.setOnNodeSelectedListener(node -> {
            viewModel.setSelectedNode(node);
        });

        viewModel.getNodesLiveData().observe(getViewLifecycleOwner(), nodes -> {
            gisMapCanvas.setNodes(nodes);
        });

        viewModel.getSelectedNodeLiveData().observe(getViewLifecycleOwner(), this::updateNodeDetails);
    }

    private void updateMapLayers() {
        gisMapCanvas.setLayerToggles(showNodes, showHeatmap, showWind, showTopo);
    }

    private void updateNodeDetails(AirQualityNode node) {
        if (node == null) return;
        tvMapNodeId.setText(node.getId());
        tvMapNodeName.setText(node.getName());
        tvMapNodeAqi.setText(node.getAqi() + " AQI");
        tvMapNodeParams.setText(String.format("PM2.5: %.1f µg/m³ • PM10: %.1f µg/m³ • Temp: %.1f°C • Hum: %.1f%%",
                node.getPm2_5(), node.getPm10(), node.getTemperature(), node.getHumidity()));

        if (node.getAqi() > 100) {
            tvMapNodeAqi.setTextColor(Color.parseColor("#EF4444"));
        } else {
            tvMapNodeAqi.setTextColor(Color.parseColor("#00FF66"));
        }
    }
}
