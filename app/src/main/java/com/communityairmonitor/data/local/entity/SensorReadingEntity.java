package com.communityairmonitor.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sensor_readings")
public class SensorReadingEntity {

    @PrimaryKey
    @NonNull
    private String id;
    private String sensorId;
    private double pm1;
    private double pm25;
    private double pm10;
    private double temperature;
    private double humidity;
    private double gasValue;
    private long timestamp;

    public SensorReadingEntity(@NonNull String id, String sensorId, double pm1, double pm25,
                               double pm10, double temperature, double humidity, double gasValue, long timestamp) {
        this.id = id;
        this.sensorId = sensorId;
        this.pm1 = pm1;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.temperature = temperature;
        this.humidity = humidity;
        this.gasValue = gasValue;
        this.timestamp = timestamp;
    }

    @NonNull
    public String getId() { return id; }
    public String getSensorId() { return sensorId; }
    public double getPm1() { return pm1; }
    public double getPm25() { return pm25; }
    public double getPm10() { return pm10; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getGasValue() { return gasValue; }
    public long getTimestamp() { return timestamp; }

    public void setId(@NonNull String id) { this.id = id; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }
    public void setPm1(double pm1) { this.pm1 = pm1; }
    public void setPm25(double pm25) { this.pm25 = pm25; }
    public void setPm10(double pm10) { this.pm10 = pm10; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public void setHumidity(double humidity) { this.humidity = humidity; }
    public void setGasValue(double gasValue) { this.gasValue = gasValue; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
