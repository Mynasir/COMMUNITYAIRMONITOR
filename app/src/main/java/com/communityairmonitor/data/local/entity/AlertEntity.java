package com.communityairmonitor.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alerts")
public class AlertEntity {

    @PrimaryKey
    @NonNull
    private String id;
    private String sensorId;
    private String alertType;
    private String severity;
    private double measuredValue;
    private double threshold;
    private String message;
    private long timestamp;
    private boolean isRead;
    private boolean isResolved;

    public AlertEntity(@NonNull String id, String sensorId, String alertType, String severity,
                       double measuredValue, double threshold, String message, long timestamp,
                       boolean isRead, boolean isResolved) {
        this.id = id;
        this.sensorId = sensorId;
        this.alertType = alertType;
        this.severity = severity;
        this.measuredValue = measuredValue;
        this.threshold = threshold;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.isResolved = isResolved;
    }

    @NonNull
    public String getId() { return id; }
    public String getSensorId() { return sensorId; }
    public String getAlertType() { return alertType; }
    public String getSeverity() { return severity; }
    public double getMeasuredValue() { return measuredValue; }
    public double getThreshold() { return threshold; }
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }
    public boolean isRead() { return isRead; }
    public boolean isResolved() { return isResolved; }

    public void setId(@NonNull String id) { this.id = id; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setMeasuredValue(double measuredValue) { this.measuredValue = measuredValue; }
    public void setThreshold(double threshold) { this.threshold = threshold; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setRead(boolean read) { isRead = read; }
    public void setResolved(boolean resolved) { isResolved = resolved; }
}
