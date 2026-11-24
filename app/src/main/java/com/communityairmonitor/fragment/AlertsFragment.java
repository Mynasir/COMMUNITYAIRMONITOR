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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.communityairmonitor.R;
import com.communityairmonitor.adapter.AlertListAdapter;
import com.communityairmonitor.model.AirAlert;
import com.communityairmonitor.viewmodel.AirViewModel;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlertsFragment extends Fragment {

    private AirViewModel viewModel;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerAlerts;
    private View layoutEmpty;
    private TextView tvAlertsHeadline, tvAlertsLatest, tvAlertsUpdated;
    private ChipGroup chipGroupFilter;
    private AlertListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alerts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AirViewModel.class);

        swipeRefresh = view.findViewById(R.id.swipeRefreshAlerts);
        recyclerAlerts = view.findViewById(R.id.recyclerAlerts);
        layoutEmpty = view.findViewById(R.id.layoutAlertsEmpty);
        tvAlertsHeadline = view.findViewById(R.id.tvAlertsHeadline);
        tvAlertsLatest = view.findViewById(R.id.tvAlertsLatest);
        tvAlertsUpdated = view.findViewById(R.id.tvAlertsUpdated);
        chipGroupFilter = view.findViewById(R.id.chipGroupAlertFilter);

        adapter = new AlertListAdapter();
        adapter.setOnBroadcastClickListener(alert -> Snackbar.make(view, "Broadcast updated for " + alert.getNodeName(), Snackbar.LENGTH_SHORT).show());
        recyclerAlerts.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerAlerts.setAdapter(adapter);

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
            }, 700));
        }

        if (chipGroupFilter != null) {
            chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> {
                if (checkedIds == null || checkedIds.isEmpty()) return;
                int id = checkedIds.get(0);
                if (id == R.id.chipAlertHazardous) viewModel.filterAlerts(AirAlert.Category.HAZARDOUS);
                else if (id == R.id.chipAlertWarning) viewModel.filterAlerts(AirAlert.Category.WARNING);
                else if (id == R.id.chipAlertSystem) viewModel.filterAlerts(AirAlert.Category.SYSTEM);
                else viewModel.filterAlerts(null);
            });
        }
    }

    private void observeData() {
        viewModel.getAlertsLiveData().observe(getViewLifecycleOwner(), alerts -> {
            adapter.setAlertList(alerts);
            int count = alerts == null ? 0 : alerts.size();
            boolean empty = count == 0;

            if (recyclerAlerts != null) recyclerAlerts.setVisibility(empty ? View.GONE : View.VISIBLE);
            if (layoutEmpty != null) layoutEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);

            if (tvAlertsHeadline != null) {
                if (empty) {
                    tvAlertsHeadline.setText(R.string.alerts_all_clear_title);
                    tvAlertsHeadline.setTextColor(requireContext().getColor(R.color.status_good));
                } else {
                    tvAlertsHeadline.setText(count + (count == 1 ? " alert in view" : " alerts in view"));
                    tvAlertsHeadline.setTextColor(requireContext().getColor(R.color.status_warning));
                }
            }
            if (tvAlertsLatest != null) {
                if (empty) {
                    tvAlertsLatest.setText(R.string.alerts_latest_placeholder);
                } else {
                    tvAlertsLatest.setText("Tap a broadcast action to notify your community about important air quality events.");
                }
            }
            stampUpdated();
        });
    }

    private void stampUpdated() {
        if (tvAlertsUpdated == null) return;
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        tvAlertsUpdated.setText("Updated at " + time);
    }
}
