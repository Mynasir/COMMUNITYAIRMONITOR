package com.communityairmonitor.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.communityairmonitor.data.AirQualityRepository;
import com.communityairmonitor.model.AirAlert;
import com.communityairmonitor.model.AirQualityNode;
import com.communityairmonitor.model.TelemetryPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AirViewModel extends AndroidViewModel {

    private final AirQualityRepository repository = AirQualityRepository.getInstance();

    private final MutableLiveData<List<AirQualityNode>> nodesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<AirAlert>> alertsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<TelemetryPoint>> telemetryLiveData = new MutableLiveData<>();
    private final MutableLiveData<AirQualityNode> selectedNodeLiveData = new MutableLiveData<>();
    private final MutableLiveData<AirQualityNode.Zone> selectedZoneLiveData = new MutableLiveData<>(null);
    private final MutableLiveData<AirAlert.Category> selectedCategoryLiveData = new MutableLiveData<>(null);
    private final MutableLiveData<String> selectedTimeRangeLiveData = new MutableLiveData<>("24H");

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();

    public AirViewModel(@NonNull Application application) {
        super(application);
        loadInitialData();
        startLiveSimulation();
    }

    private void loadInitialData() {
        nodesLiveData.setValue(new ArrayList<>(repository.getNodes()));
        alertsLiveData.setValue(new ArrayList<>(repository.getAlerts()));
        telemetryLiveData.setValue(new ArrayList<>(repository.getTelemetryPoints()));
        if (!repository.getNodes().isEmpty()) {
            selectedNodeLiveData.setValue(repository.getNodes().get(0));
        }
    }

    private void startLiveSimulation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<AirQualityNode> currentNodes = nodesLiveData.getValue();
                if (currentNodes != null && !currentNodes.isEmpty()) {
                    // Slightly fluctuate random node AQI to simulate live sensor telemetry
                    int idx = random.nextInt(Math.min(5, currentNodes.size()));
                    AirQualityNode n = currentNodes.get(idx);
                    int delta = random.nextInt(5) - 2;
                    int newAqi = Math.max(15, Math.min(180, n.getAqi() + delta));
                    n.setAqi(newAqi);
                    n.setPm2_5(newAqi * 0.28);
                    n.setPm10(newAqi * 0.55);

                    nodesLiveData.setValue(currentNodes);
                }
                handler.postDelayed(this, 2500);
            }
        }, 2500);
    }

    public LiveData<List<AirQualityNode>> getNodesLiveData() { return nodesLiveData; }
    public LiveData<List<AirAlert>> getAlertsLiveData() { return alertsLiveData; }
    public LiveData<List<TelemetryPoint>> getTelemetryLiveData() { return telemetryLiveData; }
    public LiveData<AirQualityNode> getSelectedNodeLiveData() { return selectedNodeLiveData; }
    public LiveData<AirQualityNode.Zone> getSelectedZoneLiveData() { return selectedZoneLiveData; }
    public LiveData<AirAlert.Category> getSelectedCategoryLiveData() { return selectedCategoryLiveData; }
    public LiveData<String> getSelectedTimeRangeLiveData() { return selectedTimeRangeLiveData; }

    public void setSelectedNode(AirQualityNode node) {
        selectedNodeLiveData.setValue(node);
    }

    public void filterByZone(AirQualityNode.Zone zone) {
        selectedZoneLiveData.setValue(zone);
        if (zone == null) {
            nodesLiveData.setValue(new ArrayList<>(repository.getNodes()));
        } else {
            List<AirQualityNode> filtered = new ArrayList<>();
            for (AirQualityNode n : repository.getNodes()) {
                if (n.getZone() == zone) filtered.add(n);
            }
            nodesLiveData.setValue(filtered);
        }
    }

    public void filterAlerts(AirAlert.Category category) {
        selectedCategoryLiveData.setValue(category);
        if (category == null) {
            alertsLiveData.setValue(new ArrayList<>(repository.getAlerts()));
        } else {
            List<AirAlert> filtered = new ArrayList<>();
            for (AirAlert a : repository.getAlerts()) {
                if (a.getCategory() == category) filtered.add(a);
            }
            alertsLiveData.setValue(filtered);
        }
    }

    public void setTimeRange(String range) {
        selectedTimeRangeLiveData.setValue(range);
    }

    public void toggleBroadcast(AirAlert alert) {
        List<AirAlert> currentAlerts = alertsLiveData.getValue();
        if (currentAlerts != null) {
            for (AirAlert a : currentAlerts) {
                if (a.getId().equals(alert.getId())) {
                    a.setBroadcasting(!a.isBroadcasting());
                    break;
                }
            }
            alertsLiveData.setValue(currentAlerts);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacksAndMessages(null);
    }
}
