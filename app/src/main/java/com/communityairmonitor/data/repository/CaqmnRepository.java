package com.communityairmonitor.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.communityairmonitor.data.local.database.AppDatabase;
import com.communityairmonitor.data.local.entity.AlertEntity;
import com.communityairmonitor.data.local.entity.PredictionEntity;
import com.communityairmonitor.data.local.entity.SensorEntity;
import com.communityairmonitor.data.local.entity.SensorReadingEntity;
import com.communityairmonitor.data.local.entity.UserEntity;
import com.communityairmonitor.data.remote.api.CaqmnApiService;
import com.communityairmonitor.data.remote.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CaqmnRepository {

    private static volatile CaqmnRepository instance;
    private final AppDatabase database;
    private final CaqmnApiService apiService;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private CaqmnRepository(Context context) {
        database = AppDatabase.getInstance(context);
        apiService = RetrofitClient.getApiService();
        initDefaultDataIfEmpty();
    }

    public static synchronized CaqmnRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CaqmnRepository(context.getApplicationContext());
        }
        return instance;
    }

    private void initDefaultDataIfEmpty() {
        executor.execute(() -> {
            UserEntity user = database.userDao().getUserProfileSync();
            if (user == null) {
                database.userDao().insertOrUpdate(new UserEntity(
                        "USR-101",
                        "Dr. Kwame Mensah",
                        "kwame.mensah@caqmn.org",
                        "+233 24 123 4567",
                        "Environmental Officer",
                        "Obuasi, Ashanti Region",
                        "avatar_default"
                ));
            }

            SensorEntity s1 = database.sensorDao().getSensorByIdSync("OBUASI_001");
            if (s1 == null) {
                List<SensorEntity> initialSensors = new ArrayList<>();
                initialSensors.add(new SensorEntity("OBUASI_001", "Obuasi Central Hub", "Obuasi Central", 6.2022, -1.6706, "OPERATIONAL", System.currentTimeMillis(), "2024-01-15"));
                initialSensors.add(new SensorEntity("OBUASI_002", "Tutuka School Station", "Tutuka School", 6.2100, -1.6650, "OPERATIONAL", System.currentTimeMillis(), "2024-02-01"));
                initialSensors.add(new SensorEntity("OBUASI_003", "Wawase Industrial Perimeter", "Wawase Corridor", 6.1950, -1.6800, "ATTENTION", System.currentTimeMillis(), "2024-02-20"));
                initialSensors.add(new SensorEntity("OBUASI_004", "Sanso Residential Campus", "Sanso District", 6.1880, -1.6720, "OPERATIONAL", System.currentTimeMillis(), "2024-03-10"));

                database.sensorDao().insertAll(initialSensors);

                List<SensorReadingEntity> initialReadings = new ArrayList<>();
                long now = System.currentTimeMillis();
                for (int i = 0; i < 24; i++) {
                    long timestamp = now - ((24 - i) * 3600000L);
                    double pm25 = 12.0 + Math.sin(i / 3.0) * 15.0 + (i % 5);
                    double pm10 = pm25 * 1.8;
                    double pm1 = pm25 * 0.4;
                    initialReadings.add(new SensorReadingEntity(
                            "RDG-" + i,
                            "OBUASI_001",
                            pm1, pm25, pm10,
                            24.0 + (i % 4),
                            55.0 + (i % 10),
                            120.0 + (i * 5),
                            timestamp
                    ));
                }
                database.sensorReadingDao().insertAll(initialReadings);

                List<AlertEntity> initialAlerts = new ArrayList<>();
                initialAlerts.add(new AlertEntity(
                        "ALT-201",
                        "OBUASI_003",
                        "High PM2.5 Concentration",
                        "High",
                        48.5,
                        35.5,
                        "Elevated particulate concentration recorded near industrial perimeter.",
                        now - 1800000L,
                        false,
                        false
                ));
                database.alertDao().insertAll(initialAlerts);

                List<PredictionEntity> initialPredictions = new ArrayList<>();
                for (int i = 1; i <= 6; i++) {
                    initialPredictions.add(new PredictionEntity(
                            "PRED-" + i,
                            "OBUASI_001",
                            "PM2.5",
                            22.0 + i * 2.5,
                            now + (i * 3600000L),
                            "CAQMN-LSTM-v1.4",
                            "88% Confidence Interval"
                    ));
                }
                database.predictionDao().insertAll(initialPredictions);
            }
        });
    }

    public LiveData<List<SensorEntity>> getAllSensors() {
        return database.sensorDao().getAllSensors();
    }

    public LiveData<SensorEntity> getSensorById(String id) {
        return database.sensorDao().getSensorById(id);
    }

    public LiveData<List<SensorReadingEntity>> getAllReadings() {
        return database.sensorReadingDao().getAllReadings();
    }

    public LiveData<List<SensorReadingEntity>> getReadingsForSensor(String sensorId) {
        return database.sensorReadingDao().getReadingsForSensor(sensorId);
    }

    public LiveData<List<SensorReadingEntity>> getReadingsSince(long startTime) {
        return database.sensorReadingDao().getReadingsSince(startTime);
    }

    public LiveData<List<AlertEntity>> getAllAlerts() {
        return database.alertDao().getAllAlerts();
    }

    public LiveData<UserEntity> getUserProfile() {
        return database.userDao().getUserProfile();
    }

    public LiveData<List<PredictionEntity>> getAllPredictions() {
        return database.predictionDao().getAllPredictions();
    }

    public LiveData<Integer> getActiveSensorCount() {
        return database.sensorDao().getActiveSensorCount();
    }

    public LiveData<Integer> getOfflineSensorCount() {
        return database.sensorDao().getOfflineSensorCount();
    }

    public void insertReading(SensorReadingEntity reading) {
        executor.execute(() -> database.sensorReadingDao().insert(reading));
    }

    public void createSensor(SensorEntity sensor) {
        executor.execute(() -> database.sensorDao().insertOrUpdate(sensor));
    }

    public void markAlertAsRead(String alertId) {
        executor.execute(() -> database.alertDao().markAsRead(alertId));
    }

    public void markAlertAsResolved(String alertId) {
        executor.execute(() -> database.alertDao().markAsResolved(alertId));
    }

    public void updateUserProfile(UserEntity user) {
        executor.execute(() -> database.userDao().insertOrUpdate(user));
    }
}
