package com.communityairmonitor.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.communityairmonitor.data.local.entity.AlertEntity;
import com.communityairmonitor.data.local.entity.SensorEntity;
import com.communityairmonitor.data.local.entity.SensorReadingEntity;
import com.communityairmonitor.data.local.entity.UserEntity;
import com.communityairmonitor.data.repository.CaqmnRepository;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private final CaqmnRepository repository;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        repository = CaqmnRepository.getInstance(application);
    }

    public LiveData<List<SensorEntity>> getSensors() {
        return repository.getAllSensors();
    }

    public LiveData<List<SensorReadingEntity>> getAllReadings() {
        return repository.getAllReadings();
    }

    public LiveData<List<AlertEntity>> getAlerts() {
        return repository.getAllAlerts();
    }

    public LiveData<UserEntity> getUserProfile() {
        return repository.getUserProfile();
    }

    public LiveData<Integer> getActiveSensorCount() {
        return repository.getActiveSensorCount();
    }

    public LiveData<Integer> getOfflineSensorCount() {
        return repository.getOfflineSensorCount();
    }
}
