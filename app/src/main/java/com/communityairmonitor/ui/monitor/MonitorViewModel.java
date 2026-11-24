package com.communityairmonitor.ui.monitor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.communityairmonitor.data.local.entity.SensorEntity;
import com.communityairmonitor.data.repository.CaqmnRepository;

import java.util.List;

public class MonitorViewModel extends AndroidViewModel {

    private final CaqmnRepository repository;

    public MonitorViewModel(@NonNull Application application) {
        super(application);
        repository = CaqmnRepository.getInstance(application);
    }

    public LiveData<List<SensorEntity>> getSensors() {
        return repository.getAllSensors();
    }

    public LiveData<Integer> getActiveSensorCount() {
        return repository.getActiveSensorCount();
    }

    public LiveData<Integer> getOfflineSensorCount() {
        return repository.getOfflineSensorCount();
    }

    public void registerNewSensor(SensorEntity sensor) {
        repository.createSensor(sensor);
    }
}
