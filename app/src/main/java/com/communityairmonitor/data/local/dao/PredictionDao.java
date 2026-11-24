package com.communityairmonitor.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.communityairmonitor.data.local.entity.PredictionEntity;

import java.util.List;

@Dao
public interface PredictionDao {

    @Query("SELECT * FROM predictions ORDER BY predictionTimestamp ASC")
    LiveData<List<PredictionEntity>> getAllPredictions();

    @Query("SELECT * FROM predictions WHERE sensorId = :sensorId ORDER BY predictionTimestamp ASC")
    LiveData<List<PredictionEntity>> getPredictionsForSensor(String sensorId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PredictionEntity> predictions);
}
