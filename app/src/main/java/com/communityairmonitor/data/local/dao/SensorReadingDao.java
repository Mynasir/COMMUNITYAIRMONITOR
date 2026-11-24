package com.communityairmonitor.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.communityairmonitor.data.local.entity.SensorReadingEntity;

import java.util.List;

@Dao
public interface SensorReadingDao {

    @Query("SELECT * FROM sensor_readings ORDER BY timestamp DESC")
    LiveData<List<SensorReadingEntity>> getAllReadings();

    @Query("SELECT * FROM sensor_readings WHERE sensorId = :sensorId ORDER BY timestamp DESC")
    LiveData<List<SensorReadingEntity>> getReadingsForSensor(String sensorId);

    @Query("SELECT * FROM sensor_readings WHERE timestamp >= :startTime ORDER BY timestamp ASC")
    LiveData<List<SensorReadingEntity>> getReadingsSince(long startTime);

    @Query("SELECT * FROM sensor_readings ORDER BY timestamp DESC LIMIT 1")
    LiveData<SensorReadingEntity> getLatestReading();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SensorReadingEntity reading);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SensorReadingEntity> readings);
}
