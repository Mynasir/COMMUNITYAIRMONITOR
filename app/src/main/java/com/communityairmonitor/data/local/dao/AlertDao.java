package com.communityairmonitor.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.communityairmonitor.data.local.entity.AlertEntity;

import java.util.List;

@Dao
public interface AlertDao {

    @Query("SELECT * FROM alerts ORDER BY timestamp DESC")
    LiveData<List<AlertEntity>> getAllAlerts();

    @Query("SELECT * FROM alerts WHERE severity = :severity ORDER BY timestamp DESC")
    LiveData<List<AlertEntity>> getAlertsBySeverity(String severity);

    @Query("SELECT * FROM alerts WHERE isResolved = 0 ORDER BY timestamp DESC")
    LiveData<List<AlertEntity>> getUnresolvedAlerts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AlertEntity alert);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AlertEntity> alerts);

    @Query("UPDATE alerts SET isRead = 1 WHERE id = :alertId")
    void markAsRead(String alertId);

    @Query("UPDATE alerts SET isResolved = 1 WHERE id = :alertId")
    void markAsResolved(String alertId);
}
