package com.communityairmonitor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.communityairmonitor.R;
import com.communityairmonitor.view.TrendChartCanvasView;
import com.communityairmonitor.viewmodel.AirViewModel;

public class PollutionTrendsFragment extends Fragment {

    private AirViewModel viewModel;
    private TrendChartCanvasView trendChartCanvas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pollution_trends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AirViewModel.class);

        trendChartCanvas = view.findViewById(R.id.trendChartCanvas);

        view.findViewById(R.id.btnRange1h).setOnClickListener(v -> viewModel.setTimeRange("1H"));
        view.findViewById(R.id.btnRange24h).setOnClickListener(v -> viewModel.setTimeRange("24H"));
        view.findViewById(R.id.btnRange7d).setOnClickListener(v -> viewModel.setTimeRange("7D"));
        view.findViewById(R.id.btnRange30d).setOnClickListener(v -> viewModel.setTimeRange("30D"));

        viewModel.getTelemetryLiveData().observe(getViewLifecycleOwner(), points -> {
            trendChartCanvas.setPoints(points);
        });
    }
}
