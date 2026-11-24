package com.communityairmonitor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.communityairmonitor.R;
import com.communityairmonitor.model.AirQualityNode;
import com.communityairmonitor.view.AqiGaugeView;
import com.communityairmonitor.viewmodel.AirViewModel;

public class CommunityDashboardFragment extends Fragment {

    private AirViewModel viewModel;
    private TextView tvDashAqi, tvDashPm1, tvDashPm25, tvDashPm10;
    private AqiGaugeView dashAqiGauge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_community_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AirViewModel.class);

        tvDashAqi = view.findViewById(R.id.tvDashAqi);
        tvDashPm1 = view.findViewById(R.id.tvDashPm1);
        tvDashPm25 = view.findViewById(R.id.tvDashPm25);
        tvDashPm10 = view.findViewById(R.id.tvDashPm10);
        dashAqiGauge = view.findViewById(R.id.dashAqiGauge);

        view.findViewById(R.id.btnRecalibrate).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Sensor Recalibration Triggered", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btnExportCsv).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Exporting Telemetry Matrix CSV...", Toast.LENGTH_SHORT).show();
        });

        viewModel.getNodesLiveData().observe(getViewLifecycleOwner(), nodes -> {
            if (nodes != null && !nodes.isEmpty()) {
                AirQualityNode n = nodes.get(0);
                tvDashAqi.setText(String.valueOf(n.getAqi()));
                dashAqiGauge.setAqi(n.getAqi());
                tvDashPm1.setText(String.format("%.1f µg/m³", n.getPm1_0()));
                tvDashPm25.setText(String.format("%.1f µg/m³", n.getPm2_5()));
                tvDashPm10.setText(String.format("%.1f µg/m³", n.getPm10()));
            }
        });
    }
}
