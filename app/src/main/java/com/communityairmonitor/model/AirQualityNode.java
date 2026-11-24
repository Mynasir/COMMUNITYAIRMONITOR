package com.communityairmonitor.model;

public class AirQualityNode {

    public enum Zone {
        SCHOOL("School Zone"),
        INDUSTRIAL("Industrial Corridor"),
        RESIDENTIAL("Residential");

        private final String displayName;

        Zone(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Status {
        OPERATIONAL("OPERATIONAL"),
        ATTENTION("ATTENTION"),
        OFFLINE("OFFLINE");

        private final String label;

        Status(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private final String id;
    private final String name;
    private final Zone zone;
    private final double latitude;
    private final double longitude;
    private int aqi;
    private double pm1_0;
    private double pm2_5;
    private double pm10;
    private double gasIndex;
    private double temperature;
    private double humidity;
    private Status status;
    private int rssi;
    private int battery;
    private String uptime;

    public AirQualityNode(String id, String name, Zone zone, double latitude, double longitude,
                          int aqi, double pm1_0, double pm2_5, double pm10, double gasIndex,
                          double temperature, double humidity, Status status, int rssi,
                          int battery, String uptime) {
        this.id = id;
        this.name = name;
        this.zone = zone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.aqi = aqi;
        this.pm1_0 = pm1_0;
        this.pm2_5 = pm2_5;
        this.pm10 = pm10;
        this.gasIndex = gasIndex;
        this.temperature = temperature;
        this.humidity = humidity;
        this.status = status;
        this.rssi = rssi;
        this.battery = battery;
        this.uptime = uptime;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Zone getZone() { return zone; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getAqi() { return aqi; }
    public double getPm1_0() { return pm1_0; }
    public double getPm2_5() { return pm2_5; }
    public double getPm10() { return pm10; }
    public double getGasIndex() { return gasIndex; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public Status getStatus() { return status; }
    public int getRssi() { return rssi; }
    public int getBattery() { return battery; }
    public String getUptime() { return uptime; }

    public void setAqi(int aqi) { this.aqi = aqi; }
    public void setPm2_5(double pm2_5) { this.pm2_5 = pm2_5; }
    public void setPm10(double pm10) { this.pm10 = pm10; }
    public void setStatus(Status status) { this.status = status; }
}
