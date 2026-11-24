package com.communityairmonitor.ui.dashboard;

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
import com.communityairmonitor.data.local.entity.SensorReadingEntity;
import com.communityairmonitor.domain.calculator.AqiCalculator;

public class DashboardFragment extends Fragment {

    private DashboardViewModel viewModel;
    private TextView tvCurrentPm25, tvAqiStatusPill, tvPm10Value, tvPm1Value, tvTempValue, tvHumidValue, tvLocationName, tvDeviceStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        tvLocationName = view.findViewById(R.id.tvLocationName);
        tvDeviceStatus = view.findViewById(R.id.tvDeviceStatus);
        tvCurrentPm25 = view.findViewById(R.id.tvCurrentPm25);
        tvAqiStatusPill = view.findViewById(R.id.tvAqiStatusPill);
        tvPm10Value = view.findViewById(R.id.tvPm10Value);
        tvPm1Value = view.findViewById(R.id.tvPm1Value);
        tvTempValue = view.findViewById(R.id.tvTempValue);
        tvHumidValue = view.findViewById(R.id.tvHumidValue);

        viewModel.getUserProfile().observe(getViewLifecycleOwner(), user -> {
            if (user != null && tvLocationName != null) {
                tvLocationName.setText(user.getCommunity());
            }
        });

        viewModel.getAllReadings().observe(getViewLifecycleOwner(), readings -> {
            if (readings != null && !readings.isEmpty()) {
                SensorReadingEntity r = readings.get(0);
                if (tvCurrentPm25 != null) tvCurrentPm25.setText(String.format("%.1f", r.getPm25()));
                if (tvPm10Value != null) tvPm10Value.setText(String.format("%.1f", r.getPm10()));
                if (tvPm1Value != null) tvPm1Value.setText(String.format("%.1f", r.getPm1()));
                if (tvTempValue != null) tvTempValue.setText(String.format("%.1f", r.getTemperature()));
                if (tvHumidValue != null) tvHumidValue.setText(String.format("%.0f", r.getHumidity()));

                // Calculate AQI using EPA Calculator Engine
                AqiCalculator.AqiResult aqiResult = AqiCalculator.calculateOverallAqi(r.getPm25(), r.getPm10());
                if (tvAqiStatusPill != null) {
                    tvAqiStatusPill.setText(String.format("AQI %d · %s", aqiResult.getAqi(), aqiResult.getCategory().getTitle()));
                    tvAqiStatusPill.setTextColor(aqiResult.getCategory().getColor());
                }
            }
        });
    }
}
