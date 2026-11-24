package com.communityairmonitor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.communityairmonitor.R;
import com.communityairmonitor.model.TelemetryPoint;
import com.communityairmonitor.view.TrendChartCanvasView;
import com.communityairmonitor.viewmodel.AirViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnalyticsFragment extends Fragment {

    private AirViewModel viewModel;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvAvgPm25, tvPeakPm25, tvLatestPm25, tvMinPm25, tvAnalyticsPeriod, tvAnalyticsUpdated, tvAnalyticsInsight;
    private MaterialButtonToggleGroup toggleTimeRange;
    private TrendChartCanvasView trendChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analytics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AirViewModel.class);

        swipeRefresh = view.findViewById(R.id.swipeRefreshAnalytics);
        tvAvgPm25 = view.findViewById(R.id.tvAvgPm25);
        tvPeakPm25 = view.findViewById(R.id.tvPeakPm25);
        tvLatestPm25 = view.findViewById(R.id.tvLatestPm25);
        tvMinPm25 = view.findViewById(R.id.tvMinPm25);
        tvAnalyticsPeriod = view.findViewById(R.id.tvAnalyticsPeriod);
        tvAnalyticsUpdated = view.findViewById(R.id.tvAnalyticsUpdated);
        tvAnalyticsInsight = view.findViewById(R.id.tvAnalyticsInsight);
        toggleTimeRange = view.findViewById(R.id.toggleTimeRange);
        trendChart = view.findViewById(R.id.trendChart);

        setupInteractions(view);
        observeData();
    }

    private void setupInteractions(View root) {
        if (swipeRefresh != null) {
            swipeRefresh.setColorSchemeResources(R.color.brand_primary, R.color.brand_secondary);
            swipeRefresh.setOnRefreshListener(() -> root.postDelayed(() -> {
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                stampUpdated();
                Snackbar.make(root, R.string.last_updated_now, Snackbar.LENGTH_SHORT).show();
            }, 800));
        }

        if (toggleTimeRange != null) {
            toggleTimeRange.check(R.id.btnRange7d);
            toggleTimeRange.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                if (!isChecked) return;
                String range = "7D";
                String label = "Last 7 days";
                String insight = "Use trends to identify peak pollution hours and plan outdoor activities more safely.";
                if (checkedId == R.id.btnRange24h) {
                    range = "24H";
                    label = "Last 24 hours";
                    insight = "Short-term trends help you spot sudden spikes and daily air quality patterns.";
                } else if (checkedId == R.id.btnRange30d) {
                    range = "30D";
                    label = "Last 30 days";
                    insight = "Longer trends reveal recurring pollution patterns and improvements over time.";
                }
                viewModel.setTimeRange(range);
                if (tvAnalyticsPeriod != null) tvAnalyticsPeriod.setText(label);
                if (tvAnalyticsInsight != null) tvAnalyticsInsight.setText(insight);
                pulse(trendChart);
                Snackbar.make(root, "Showing " + label.toLowerCase(Locale.getDefault()), Snackbar.LENGTH_SHORT).show();
            });
        }
    }

    private void observeData() {
        viewModel.getTelemetryLiveData().observe(getViewLifecycleOwner(), points -> {
            if (points != null && !points.isEmpty()) {
                bindStats(points);
                if (trendChart != null) trendChart.setPoints(points);
                stampUpdated();
            }
        });

        viewModel.getSelectedTimeRangeLiveData().observe(getViewLifecycleOwner(), range -> {
            if (range == null || toggleTimeRange == null) return;
            if ("24H".equals(range)) toggleTimeRange.check(R.id.btnRange24h);
            else if ("30D".equals(range)) toggleTimeRange.check(R.id.btnRange30d);
            else toggleTimeRange.check(R.id.btnRange7d);
        });
    }

    private void bindStats(List<TelemetryPoint> points) {
        double sum = 0;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (TelemetryPoint pt : points) {
            sum += pt.getPm25();
            if (pt.getPm25() > max) max = pt.getPm25();
            if (pt.getPm25() < min) min = pt.getPm25();
        }
        double avg = sum / points.size();
        double latest = points.get(points.size() - 1).getPm25();

        setValue(tvAvgPm25, String.format(Locale.getDefault(), "%.1f µg/m³", avg));
        setValue(tvPeakPm25, String.format(Locale.getDefault(), "%.1f µg/m³", max));
        setValue(tvMinPm25, String.format(Locale.getDefault(), "%.1f µg/m³", min));
        setValue(tvLatestPm25, String.format(Locale.getDefault(), "%.1f µg/m³", latest));
    }

    private void setValue(TextView view, String value) {
        if (view == null) return;
        if (!value.contentEquals(view.getText())) {
            view.setText(value);
            pulse(view);
        }
    }

    private void pulse(View v) {
        if (v == null) return;
        v.animate().cancel();
        v.setAlpha(0.65f);
        v.animate().alpha(1f).setDuration(250).start();
    }

    private void stampUpdated() {
        if (tvAnalyticsUpdated == null) return;
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        tvAnalyticsUpdated.setText("Updated at " + time);
    }
}
