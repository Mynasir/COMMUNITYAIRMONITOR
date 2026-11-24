package com.communityairmonitor.model;

public class TelemetryPoint {

    private final String timeLabel;
    private final double pm25;
    private final double pm10;
    private final double gasIndex;

    public TelemetryPoint(String timeLabel, double pm25, double pm10, double gasIndex) {
        this.timeLabel = timeLabel;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.gasIndex = gasIndex;
    }

    public String getTimeLabel() { return timeLabel; }
    public double getPm25() { return pm25; }
    public double getPm10() { return pm10; }
    public double getGasIndex() { return gasIndex; }
}
