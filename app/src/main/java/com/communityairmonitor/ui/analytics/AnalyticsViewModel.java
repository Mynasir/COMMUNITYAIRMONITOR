package com.communityairmonitor.ui.analytics;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.communityairmonitor.data.local.entity.PredictionEntity;
import com.communityairmonitor.data.local.entity.SensorReadingEntity;
import com.communityairmonitor.data.repository.CaqmnRepository;

import java.util.List;

public class AnalyticsViewModel extends AndroidViewModel {

    private final CaqmnRepository repository;

    public AnalyticsViewModel(@NonNull Application application) {
        super(application);
        repository = CaqmnRepository.getInstance(application);
    }

    public LiveData<List<SensorReadingEntity>> getAllReadings() {
        return repository.getAllReadings();
    }

    public LiveData<List<SensorReadingEntity>> getReadingsSince(long startTime) {
        return repository.getReadingsSince(startTime);
    }

    public LiveData<List<PredictionEntity>> getPredictions() {
        return repository.getAllPredictions();
    }
}
