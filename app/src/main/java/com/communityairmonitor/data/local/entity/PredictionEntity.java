package com.communityairmonitor.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "predictions")
public class PredictionEntity {

    @PrimaryKey
    @NonNull
    private String id;
    private String sensorId;
    private String pollutant;
    private double predictedValue;
    private long predictionTimestamp;
    private String modelVersion;
    private String confidenceInformation;

    public PredictionEntity(@NonNull String id, String sensorId, String pollutant, double predictedValue,
                            long predictionTimestamp, String modelVersion, String confidenceInformation) {
        this.id = id;
        this.sensorId = sensorId;
        this.pollutant = pollutant;
        this.predictedValue = predictedValue;
        this.predictionTimestamp = predictionTimestamp;
        this.modelVersion = modelVersion;
        this.confidenceInformation = confidenceInformation;
    }

    @NonNull
    public String getId() { return id; }
    public String getSensorId() { return sensorId; }
    public String getPollutant() { return pollutant; }
    public double getPredictedValue() { return predictedValue; }
    public long getPredictionTimestamp() { return predictionTimestamp; }
    public String getModelVersion() { return modelVersion; }
    public String getConfidenceInformation() { return confidenceInformation; }

    public void setId(@NonNull String id) { this.id = id; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }
    public void setPollutant(String pollutant) { this.pollutant = pollutant; }
    public void setPredictedValue(double predictedValue) { this.predictedValue = predictedValue; }
    public void setPredictionTimestamp(long predictionTimestamp) { this.predictionTimestamp = predictionTimestamp; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
    public void setConfidenceInformation(String confidenceInformation) { this.confidenceInformation = confidenceInformation; }
}
