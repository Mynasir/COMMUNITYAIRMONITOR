package com.communityairmonitor.model;

public class AirAlert {

    public enum Category {
        HAZARDOUS("HAZARDOUS"),
        WARNING("WARNING"),
        SYSTEM("SYSTEM");

        private final String label;

        Category(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private final String id;
    private final Category category;
    private final String nodeId;
    private final String nodeName;
    private final String location;
    private final String title;
    private final String message;
    private final double pm25Value;
    private final String timeAgo;
    private final String precaution;
    private boolean isBroadcasting;

    public AirAlert(String id, Category category, String nodeId, String nodeName,
                    String location, String title, String message, double pm25Value,
                    String timeAgo, String precaution, boolean isBroadcasting) {
        this.id = id;
        this.category = category;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.location = location;
        this.title = title;
        this.message = message;
        this.pm25Value = pm25Value;
        this.timeAgo = timeAgo;
        this.precaution = precaution;
        this.isBroadcasting = isBroadcasting;
    }

    public String getId() { return id; }
    public Category getCategory() { return category; }
    public String getNodeId() { return nodeId; }
    public String getNodeName() { return nodeName; }
    public String getLocation() { return location; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public double getPm25Value() { return pm25Value; }
    public String getTimeAgo() { return timeAgo; }
    public String getPrecaution() { return precaution; }
    public boolean isBroadcasting() { return isBroadcasting; }

    public void setBroadcasting(boolean broadcasting) { isBroadcasting = broadcasting; }
}
