package com.communityairmonitor.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.communityairmonitor.R;
import com.communityairmonitor.model.AirQualityNode;
import com.communityairmonitor.view.AqiGaugeView;
import com.communityairmonitor.viewmodel.AirViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MonitoringFragment extends Fragment {

    private AirViewModel viewModel;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvMonitorNodeName, tvMonitorNodeMeta, tvMonitorStatus, tvMonitorPm25, tvMonitorPm10, tvMonitorPm1;
    private TextView tvMonitorGas, tvMonitorTemp, tvMonitorHumidity, tvMonitorBattery, tvMonitorSignal, tvMonitorUptime;
    private TextView tvMonitorUpdated, tvMonitorLiveBadge;
    private LinearProgressIndicator progressMonitorPm25;
    private AqiGaugeView monitorAqiGauge;
    private MaterialCardView cardMonitorPrimary;
    private float lastPm25 = Float.NaN;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monitoring, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AirViewModel.class);

        swipeRefresh = view.findViewById(R.id.swipeRefreshMonitoring);
        cardMonitorPrimary = view.findViewById(R.id.cardMonitorPrimary);
        tvMonitorNodeName = view.findViewById(R.id.tvMonitorNodeName);
        tvMonitorNodeMeta = view.findViewById(R.id.tvMonitorNodeMeta);
        tvMonitorStatus = view.findViewById(R.id.tvMonitorStatus);
        tvMonitorPm25 = view.findViewById(R.id.tvMonitorPm25);
        tvMonitorPm10 = view.findViewById(R.id.tvMonitorPm10);
        tvMonitorPm1 = view.findViewById(R.id.tvMonitorPm1);
        tvMonitorGas = view.findViewById(R.id.tvMonitorGas);
        tvMonitorTemp = view.findViewById(R.id.tvMonitorTemp);
        tvMonitorHumidity = view.findViewById(R.id.tvMonitorHumidity);
        tvMonitorBattery = view.findViewById(R.id.tvMonitorBattery);
        tvMonitorSignal = view.findViewById(R.id.tvMonitorSignal);
        tvMonitorUptime = view.findViewById(R.id.tvMonitorUptime);
        tvMonitorUpdated = view.findViewById(R.id.tvMonitorUpdated);
        tvMonitorLiveBadge = view.findViewById(R.id.tvMonitorLiveBadge);
        progressMonitorPm25 = view.findViewById(R.id.progressMonitorPm25);
        monitorAqiGauge = view.findViewById(R.id.monitorAqiGauge);

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

        MaterialButton refreshBtn = root.findViewById(R.id.btnMonitorRefresh);
        if (refreshBtn != null) {
            refreshBtn.setOnClickListener(v -> {
                if (swipeRefresh != null) swipeRefresh.setRefreshing(true);
                root.postDelayed(() -> {
                    if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                    stampUpdated();
                    Snackbar.make(root, R.string.last_updated_now, Snackbar.LENGTH_SHORT).show();
                }, 800);
            });
        }

        if (cardMonitorPrimary != null) {
            cardMonitorPrimary.setOnClickListener(v -> Snackbar.make(root, "Live node telemetry • tap refresh for latest update", Snackbar.LENGTH_SHORT).show());
        }
    }

    private void observeData() {
        viewModel.getNodesLiveData().observe(getViewLifecycleOwner(), nodes -> {
            if (nodes != null && !nodes.isEmpty()) {
                AirQualityNode node = nodes.get(0);
                bindNode(node);
            }
        });
    }

    private void bindNode(AirQualityNode node) {
        setText(tvMonitorNodeName, node.getName());
        String zone = node.getZone() != null ? node.getZone().getDisplayName() : "Community";
        setText(tvMonitorNodeMeta, zone + " • " + node.getId());
        setText(tvMonitorStatus, node.getStatus() != null ? node.getStatus().getLabel() : "ONLINE");

        float pm25 = (float) node.getPm2_5();
        if (tvMonitorPm25 != null) {
            if (Float.isNaN(lastPm25)) {
                tvMonitorPm25.setText(String.format(Locale.getDefault(), "%.1f µg/m³", pm25));
            } else if (Math.abs(lastPm25 - pm25) >= 0.05f) {
                animateValue(tvMonitorPm25, lastPm25, pm25, " µg/m³");
                pulse(tvMonitorPm25);
            }
            lastPm25 = pm25;
        }

        setAnimated(tvMonitorPm1, String.format(Locale.getDefault(), "%.1f", node.getPm1_0()));
        setAnimated(tvMonitorPm10, String.format(Locale.getDefault(), "%.1f", node.getPm10()));
        setAnimated(tvMonitorGas, String.format(Locale.getDefault(), "%.0f", node.getGasIndex()));
        setAnimated(tvMonitorTemp, String.format(Locale.getDefault(), "%.1f", node.getTemperature()));
        setAnimated(tvMonitorHumidity, String.format(Locale.getDefault(), "%.0f", node.getHumidity()));
        setText(tvMonitorBattery, node.getBattery() + "%");
        setText(tvMonitorSignal, node.getRssi() + " dBm");
        setText(tvMonitorUptime, node.getUptime() != null ? node.getUptime() : "--");

        if (progressMonitorPm25 != null) {
            int p = Math.max(0, Math.min(100, Math.round(pm25)));
            progressMonitorPm25.setProgressCompat(p, true);
        }
        if (monitorAqiGauge != null) monitorAqiGauge.setAqi(node.getAqi());
        updateHeroColor(node.getAqi());

        pulse(tvMonitorLiveBadge);
        stampUpdated();
    }

    private void updateHeroColor(int aqi) {
        if (cardMonitorPrimary == null) return;
        int color;
        if (aqi <= 50) color = ContextCompat.getColor(requireContext(), R.color.status_good);
        else if (aqi <= 100) color = ContextCompat.getColor(requireContext(), R.color.brand_primary);
        else color = ContextCompat.getColor(requireContext(), R.color.status_danger);
        cardMonitorPrimary.setCardBackgroundColor(color);
    }

    private void animateValue(TextView view, float from, float to, String suffix) {
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(450);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(a -> view.setText(String.format(Locale.getDefault(), "%.1f%s", (float) a.getAnimatedValue(), suffix)));
        animator.start();
    }

    private void setAnimated(TextView view, String value) {
        if (view == null) return;
        if (!value.contentEquals(view.getText())) {
            view.setText(value);
            pulse(view);
        }
    }

    private void setText(TextView view, String value) {
        if (view != null) view.setText(value);
    }

    private void pulse(View v) {
        if (v == null) return;
        v.animate().cancel();
        v.setAlpha(0.65f);
        v.animate().alpha(1f).setDuration(240).start();
    }

    private void stampUpdated() {
        if (tvMonitorUpdated == null) return;
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        tvMonitorUpdated.setText("Updated at " + time);
    }
}
