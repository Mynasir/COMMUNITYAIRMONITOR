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

import com.communityairmonitor.MainActivity;
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

public class DashboardFragment extends Fragment {

    private AirViewModel viewModel;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvCurrentPm25, tvAqiStatusPill, tvPm10Value, tvPm1Value, tvTempValue, tvHumidValue;
    private TextView tvLocationName, tvLastUpdated, tvRecentSensorDescription, tvLiveHint, tvHealthTip, tvAlertSummaryValue;
    private LinearProgressIndicator progressAqi;
    private AqiGaugeView aqiGauge;
    private MaterialCardView cardHeroAqi;

    private float lastPm25 = Float.NaN;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AirViewModel.class);

        swipeRefresh = view.findViewById(R.id.swipeRefreshDashboard);
        tvCurrentPm25 = view.findViewById(R.id.tvCurrentPm25);
        tvAqiStatusPill = view.findViewById(R.id.tvAqiStatusPill);
        tvPm10Value = view.findViewById(R.id.tvPm10Value);
        tvPm1Value = view.findViewById(R.id.tvPm1Value);
        tvTempValue = view.findViewById(R.id.tvTempValue);
        tvHumidValue = view.findViewById(R.id.tvHumidValue);
        tvLocationName = view.findViewById(R.id.tvLocationName);
        tvLastUpdated = view.findViewById(R.id.tvLastUpdated);
        tvRecentSensorDescription = view.findViewById(R.id.tvRecentSensorDescription);
        tvLiveHint = view.findViewById(R.id.tvLiveHint);
        tvHealthTip = view.findViewById(R.id.tvHealthTip);
        tvAlertSummaryValue = view.findViewById(R.id.tvAlertSummaryValue);
        progressAqi = view.findViewById(R.id.progressAqi);
        aqiGauge = view.findViewById(R.id.aqiGauge);
        cardHeroAqi = view.findViewById(R.id.cardHeroAqi);

        setupInteractions(view);
        observeData();
    }

    private void setupInteractions(View root) {
        if (swipeRefresh != null) {
            swipeRefresh.setColorSchemeResources(R.color.brand_primary, R.color.brand_secondary);
            swipeRefresh.setOnRefreshListener(() -> root.postDelayed(() -> {
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                updateLastUpdated();
                Snackbar.make(root, R.string.last_updated_now, Snackbar.LENGTH_SHORT).show();
            }, 900));
        }

        View refreshCard = root.findViewById(R.id.cardRefresh);
        if (refreshCard != null) refreshCard.setOnClickListener(v -> {
            if (swipeRefresh != null) swipeRefresh.setRefreshing(true);
            root.postDelayed(() -> {
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                updateLastUpdated();
                Snackbar.make(root, R.string.last_updated_now, Snackbar.LENGTH_SHORT).show();
            }, 900);
        });

        MaterialButton btnMonitor = root.findViewById(R.id.btnOpenMonitor);
        MaterialButton btnAlerts = root.findViewById(R.id.btnOpenAlerts);
        MaterialButton btnAnalytics = root.findViewById(R.id.btnOpenAnalytics);
        if (btnMonitor != null) btnMonitor.setOnClickListener(v -> navigateTo(R.id.nav_monitoring));
        if (btnAlerts != null) btnAlerts.setOnClickListener(v -> navigateTo(R.id.nav_alerts));
        if (btnAnalytics != null) btnAnalytics.setOnClickListener(v -> navigateTo(R.id.nav_analytics));

        if (cardHeroAqi != null) {
            cardHeroAqi.setOnClickListener(v -> {
                String status = tvAqiStatusPill != null ? tvAqiStatusPill.getText().toString() : "";
                String pm = tvCurrentPm25 != null ? tvCurrentPm25.getText().toString() : "--";
                Snackbar.make(root, "PM2.5 " + pm + " µg/m³ · " + status, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_view_monitor, a -> navigateTo(R.id.nav_monitoring))
                        .show();
            });
        }

        View recent = root.findViewById(R.id.cardRecentActivity);
        if (recent != null) recent.setOnClickListener(v -> navigateTo(R.id.nav_monitoring));
        View alertSummary = root.findViewById(R.id.cardAlertSummary);
        if (alertSummary != null) alertSummary.setOnClickListener(v -> navigateTo(R.id.nav_alerts));
        View location = root.findViewById(R.id.cardLocation);
        if (location != null) location.setOnClickListener(v -> Snackbar.make(root, getString(R.string.community_name), Snackbar.LENGTH_SHORT).show());

        bindMetricClicks(root, R.id.cardMetricPm10, "PM10", tvPm10Value, " µg/m³");
        bindMetricClicks(root, R.id.cardMetricPm1, "PM1.0", tvPm1Value, " µg/m³");
        bindMetricClicks(root, R.id.cardMetricTemp, "Temperature", tvTempValue, " °C");
        bindMetricClicks(root, R.id.cardMetricHumidity, "Humidity", tvHumidValue, " %");
    }

    private void bindMetricClicks(View root, int cardId, String label, TextView valueView, String unit) {
        View card = root.findViewById(cardId);
        if (card == null) return;
        card.setOnClickListener(v -> {
            String value = valueView != null ? valueView.getText().toString() : "--";
            Snackbar.make(root, label + ": " + value + unit, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.action_view_analytics, a -> navigateTo(R.id.nav_analytics))
                    .show();
        });
    }

    private void observeData() {
        viewModel.getNodesLiveData().observe(getViewLifecycleOwner(), nodes -> {
            if (nodes != null && !nodes.isEmpty()) {
                AirQualityNode node = nodes.get(0);
                if (tvLocationName != null) tvLocationName.setText(node.getName() != null ? node.getName() : getString(R.string.community_name));

                setValue(tvPm10Value, String.format(Locale.getDefault(), "%.1f", node.getPm10()));
                setValue(tvPm1Value, String.format(Locale.getDefault(), "%.1f", node.getPm1_0()));
                setValue(tvTempValue, String.format(Locale.getDefault(), "%.1f", node.getTemperature()));
                setValue(tvHumidValue, String.format(Locale.getDefault(), "%.0f", node.getHumidity()));

                float pm25 = (float) node.getPm2_5();
                updatePm25(pm25);
                updateAqiState(node.getAqi());
                if (tvRecentSensorDescription != null) {
                    tvRecentSensorDescription.setText(node.getName() + " • Live reading received • Battery " + node.getBattery() + "% • Signal " + node.getRssi() + " dBm");
                }
                updateLastUpdated();
            }
        });

        viewModel.getAlertsLiveData().observe(getViewLifecycleOwner(), alerts -> {
            int count = alerts == null ? 0 : alerts.size();
            if (tvAlertSummaryValue != null) {
                tvAlertSummaryValue.setText(count + (count == 1 ? " active alert" : " active alerts"));
            }
        });
    }

    private void updatePm25(float value) {
        if (tvCurrentPm25 == null) return;
        if (Float.isNaN(lastPm25)) {
            tvCurrentPm25.setText(String.format(Locale.getDefault(), "%.1f", value));
        } else if (Math.abs(lastPm25 - value) >= 0.05f) {
            ValueAnimator animator = ValueAnimator.ofFloat(lastPm25, value);
            animator.setDuration(450);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(a -> tvCurrentPm25.setText(String.format(Locale.getDefault(), "%.1f", (float) a.getAnimatedValue())));
            animator.start();
            pulse(tvCurrentPm25);
        }
        lastPm25 = value;
    }

    private void setValue(TextView view, String value) {
        if (view == null) return;
        if (!value.contentEquals(view.getText())) {
            view.setText(value);
            pulse(view);
        }
    }

    private void pulse(View view) {
        if (view == null) return;
        view.animate().cancel();
        view.setAlpha(0.65f);
        view.animate().alpha(1f).setDuration(220).start();
        if (tvLiveHint != null) {
            tvLiveHint.animate().cancel();
            tvLiveHint.setAlpha(0.5f);
            tvLiveHint.animate().alpha(1f).setDuration(300).start();
        }
    }

    private void updateAqiState(int aqi) {
        if (progressAqi != null) {
            int clamped = Math.max(0, Math.min(aqi, 200));
            int progress = Math.min(100, (int) Math.round((clamped / 200.0) * 100.0));
            progressAqi.setProgressCompat(progress, true);
        }
        if (aqiGauge != null) aqiGauge.setAqi(aqi);

        int heroColor;
        int statusText;
        int tipText;
        if (aqi <= 50) {
            heroColor = ContextCompat.getColor(requireContext(), R.color.status_good);
            statusText = R.string.status_good;
            tipText = R.string.home_tip_good;
        } else if (aqi <= 100) {
            heroColor = ContextCompat.getColor(requireContext(), R.color.brand_primary);
            statusText = R.string.status_moderate;
            tipText = R.string.home_tip_moderate;
        } else {
            heroColor = ContextCompat.getColor(requireContext(), R.color.status_danger);
            statusText = R.string.status_unhealthy;
            tipText = R.string.home_tip_unhealthy;
        }
        if (cardHeroAqi != null) cardHeroAqi.setCardBackgroundColor(heroColor);
        if (tvAqiStatusPill != null) tvAqiStatusPill.setText(getString(statusText));
        if (tvHealthTip != null) tvHealthTip.setText(getString(tipText));
    }

    private void updateLastUpdated() {
        if (tvLastUpdated == null) return;
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        tvLastUpdated.setText("Updated at " + time);
    }

    private void navigateTo(int navId) {
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).navigateTo(navId);
        }
    }
}
