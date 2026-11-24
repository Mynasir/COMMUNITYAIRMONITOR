package com.communityairmonitor.data;

import com.communityairmonitor.model.AirAlert;
import com.communityairmonitor.model.AirQualityNode;
import com.communityairmonitor.model.TelemetryPoint;

import java.util.ArrayList;
import java.util.List;

public class AirQualityRepository {

    private static AirQualityRepository instance;
    private final List<AirQualityNode> nodes = new ArrayList<>();
    private final List<AirAlert> alerts = new ArrayList<>();
    private final List<TelemetryPoint> telemetryPoints = new ArrayList<>();

    private AirQualityRepository() {
        initMockNodes();
        initMockAlerts();
        initMockTelemetry();
    }

    public static synchronized AirQualityRepository getInstance() {
        if (instance == null) {
            instance = new AirQualityRepository();
        }
        return instance;
    }

    private void initMockNodes() {
        nodes.add(new AirQualityNode("NODE-01", "Oakwood Station", AirQualityNode.Zone.RESIDENTIAL,
                37.7749, -122.4194, 38, 4.2, 8.4, 16.2, 112, 22.4, 48.0,
                AirQualityNode.Status.OPERATIONAL, -62, 98, "14d 08h"));

        nodes.add(new AirQualityNode("NODE-04", "Industrial Zone North", AirQualityNode.Zone.INDUSTRIAL,
                37.7833, -122.4167, 142, 18.5, 68.4, 110.2, 480, 26.1, 41.5,
                AirQualityNode.Status.ATTENTION, -74, 85, "9d 14h"));

        nodes.add(new AirQualityNode("NODE-07", "Port Transit Way", AirQualityNode.Zone.INDUSTRIAL,
                37.7650, -122.4050, 156, 21.0, 78.2, 134.0, 520, 27.8, 39.0,
                AirQualityNode.Status.ATTENTION, -81, 79, "22d 02h"));

        nodes.add(new AirQualityNode("NODE-12", "Lincoln Elementary School", AirQualityNode.Zone.SCHOOL,
                37.7550, -122.4250, 28, 3.1, 6.2, 12.8, 85, 21.0, 52.0,
                AirQualityNode.Status.OPERATIONAL, -58, 100, "31d 19h"));

        nodes.add(new AirQualityNode("NODE-18", "Sunset High Campus", AirQualityNode.Zone.SCHOOL,
                37.7480, -122.4400, 32, 3.8, 7.1, 14.5, 92, 21.5, 50.0,
                AirQualityNode.Status.OPERATIONAL, -65, 94, "18d 05h"));

        nodes.add(new AirQualityNode("NODE-23", "Valley Park Hub", AirQualityNode.Zone.RESIDENTIAL,
                37.7600, -122.4300, 42, 5.0, 9.8, 19.4, 105, 23.0, 46.0,
                AirQualityNode.Status.OPERATIONAL, -60, 92, "45d 11h"));

        // Generate remaining nodes up to 42
        for (int i = 24; i <= 42; i++) {
            AirQualityNode.Zone zone = (i % 3 == 0) ? AirQualityNode.Zone.SCHOOL :
                    ((i % 2 == 0) ? AirQualityNode.Zone.INDUSTRIAL : AirQualityNode.Zone.RESIDENTIAL);
            int aqi = 25 + (i * 3) % 90;
            AirQualityNode.Status status = (aqi > 100) ? AirQualityNode.Status.ATTENTION : AirQualityNode.Status.OPERATIONAL;
            nodes.add(new AirQualityNode(
                    "NODE-" + String.format("%02d", i),
                    "Station #" + String.format("%02d", i) + " (" + zone.getDisplayName() + ")",
                    zone,
                    37.75 + (i * 0.003),
                    -122.42 + (i * 0.002),
                    aqi,
                    aqi * 0.15,
                    aqi * 0.28,
                    aqi * 0.55,
                    100 + aqi * 2,
                    22.0 + (i % 5),
                    45.0 + (i % 10),
                    status,
                    -60 - (i % 20),
                    80 + (i % 20),
                    (i * 2) + "d 12h"
            ));
        }
    }

    private void initMockAlerts() {
        alerts.add(new AirAlert(
                "ALT-101",
                AirAlert.Category.HAZARDOUS,
                "NODE-04",
                "Node-04 Industrial North",
                "Industrial Zone North (Sec-D)",
                "High Particulate Concentration Exceeded",
                "PM2.5 levels spike at 68.4 µg/m³, surpassing the safe limit of 35.0 µg/m³.",
                68.4,
                "8m ago",
                "Wear N95 mask outdoors, close exterior air intake vents immediately.",
                false
        ));

        alerts.add(new AirAlert(
                "ALT-102",
                AirAlert.Category.WARNING,
                "NODE-07",
                "Node-07 Port Transit",
                "Port Transit Way",
                "VOC & MQ-135 Gas Fluctuation",
                "Volatile Organic Compounds elevated by +34% during freight transport operations.",
                42.1,
                "24m ago",
                "Limit outdoor strenuous activities in Port Transit vicinity.",
                false
        ));

        alerts.add(new AirAlert(
                "ALT-103",
                AirAlert.Category.SYSTEM,
                "NODE-18",
                "Sunset High Campus",
                "Sunset High School",
                "Sensirion SPS30 Sensor Recalibrated",
                "Routine optical dust sensor calibration successfully executed across LoRa mesh.",
                14.5,
                "1h ago",
                "Telemetry verified. Safe threshold confirmed.",
                false
        ));

        alerts.add(new AirAlert(
                "ALT-104",
                AirAlert.Category.WARNING,
                "NODE-02",
                "Metro Rail Yard",
                "Metro Freight Corridor",
                "Diesel Particulate Matter Spike",
                "PM10 concentration reached 94.2 µg/m³ during peak switching hours.",
                52.8,
                "2h ago",
                "Keep windows closed near transit rail yards.",
                false
        ));
    }

    private void initMockTelemetry() {
        String[] hours = {"00:00", "03:00", "06:00", "08:30", "12:00", "15:00", "18:00", "21:00", "Now"};
        double[] pm25 = {12.4, 10.8, 18.2, 28.6, 34.1, 22.4, 19.8, 14.2, 11.2};
        double[] pm10 = {24.8, 20.2, 35.0, 52.4, 68.2, 44.0, 38.5, 28.0, 22.8};
        double[] gas = {95, 88, 140, 210, 280, 180, 160, 120, 105};

        for (int i = 0; i < hours.length; i++) {
            telemetryPoints.add(new TelemetryPoint(hours[i], pm25[i], pm10[i], gas[i]));
        }
    }

    public List<AirQualityNode> getNodes() {
        return nodes;
    }

    public List<AirAlert> getAlerts() {
        return alerts;
    }

    public List<TelemetryPoint> getTelemetryPoints() {
        return telemetryPoints;
    }

    public AirQualityNode getNodeById(String id) {
        for (AirQualityNode node : nodes) {
            if (node.getId().equalsIgnoreCase(id)) {
                return node;
            }
        }
        return nodes.get(0);
    }
}
