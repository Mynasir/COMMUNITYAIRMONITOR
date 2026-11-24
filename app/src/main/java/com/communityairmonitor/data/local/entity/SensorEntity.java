package com.communityairmonitor.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sensors")
public class SensorEntity {

    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String location;
    private double latitude;
    private double longitude;
    private String status;
    private long lastSeen;
    private String installationDate;

    public SensorEntity(@NonNull String id, String name, String location, double latitude,
                        double longitude, String status, long lastSeen, String installationDate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.lastSeen = lastSeen;
        this.installationDate = installationDate;
    }

    @NonNull
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getStatus() { return status; }
    public long getLastSeen() { return lastSeen; }
    public String getInstallationDate() { return installationDate; }

    public void setId(@NonNull String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setStatus(String status) { this.status = status; }
    public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
    public void setInstallationDate(String installationDate) { this.installationDate = installationDate; }
}
