package com.communityairmonitor.ui.alerts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.communityairmonitor.data.local.entity.AlertEntity;
import com.communityairmonitor.data.repository.CaqmnRepository;

import java.util.List;

public class AlertsViewModel extends AndroidViewModel {

    private final CaqmnRepository repository;

    public AlertsViewModel(@NonNull Application application) {
        super(application);
        repository = CaqmnRepository.getInstance(application);
    }

    public LiveData<List<AlertEntity>> getAlerts() {
        return repository.getAllAlerts();
    }

    public void markAsRead(String alertId) {
        repository.markAlertAsRead(alertId);
    }

    public void markAsResolved(String alertId) {
        repository.markAlertAsResolved(alertId);
    }
}
