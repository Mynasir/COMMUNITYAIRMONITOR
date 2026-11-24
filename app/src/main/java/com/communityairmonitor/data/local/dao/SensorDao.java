package com.communityairmonitor.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.communityairmonitor.data.local.entity.SensorEntity;

import java.util.List;

@Dao
public interface SensorDao {

    @Query("SELECT * FROM sensors ORDER BY name ASC")
    LiveData<List<SensorEntity>> getAllSensors();

    @Query("SELECT * FROM sensors WHERE status = :status ORDER BY name ASC")
    LiveData<List<SensorEntity>> getSensorsByStatus(String status);

    @Query("SELECT * FROM sensors WHERE id = :id LIMIT 1")
    LiveData<SensorEntity> getSensorById(String id);

    @Query("SELECT * FROM sensors WHERE id = :id LIMIT 1")
    SensorEntity getSensorByIdSync(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(SensorEntity sensor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SensorEntity> sensors);

    @Update
    void update(SensorEntity sensor);

    @Query("DELETE FROM sensors WHERE id = :id")
    void deleteById(String id);

    @Query("SELECT COUNT(*) FROM sensors WHERE status = 'OPERATIONAL'")
    LiveData<Integer> getActiveSensorCount();

    @Query("SELECT COUNT(*) FROM sensors WHERE status != 'OPERATIONAL'")
    LiveData<Integer> getOfflineSensorCount();
}
