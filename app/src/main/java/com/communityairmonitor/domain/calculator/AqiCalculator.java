package com.communityairmonitor.domain.calculator;

import android.graphics.Color;

public class AqiCalculator {

    public enum AqiCategory {
        GOOD("Good", "Air quality is satisfactory, and air pollution poses little or no risk.", Color.parseColor("#267A55")),
        MODERATE("Moderate", "Air quality is acceptable. However, there may be a risk for some sensitive individuals.", Color.parseColor("#B7791F")),
        UNHEALTHY_SENSITIVE("Unhealthy for Sensitive Groups", "Members of sensitive groups may experience health effects. General public is less likely to be affected.", Color.parseColor("#D97706")),
        UNHEALTHY("Unhealthy", "Some members of the general public may experience health effects; sensitive groups may experience more serious health effects.", Color.parseColor("#DC2626")),
        VERY_UNHEALTHY("Very Unhealthy", "Health alert: The risk of health effects is increased for everyone.", Color.parseColor("#7C3AED")),
        HAZARDOUS("Hazardous", "Health warning of emergency conditions: Everyone is more likely to be affected.", Color.parseColor("#7F1D1D"));

        private final String title;
        private final String healthMessage;
        private final int color;

        AqiCategory(String title, String healthMessage, int color) {
            this.title = title;
            this.healthMessage = healthMessage;
            this.color = color;
        }

        public String getTitle() { return title; }
        public String getHealthMessage() { return healthMessage; }
        public int getColor() { return color; }
    }

    public static class AqiResult {
        private final int aqi;
        private final AqiCategory category;
        private final String primaryPollutant;

        public AqiResult(int aqi, AqiCategory category, String primaryPollutant) {
            this.aqi = aqi;
            this.category = category;
            this.primaryPollutant = primaryPollutant;
        }

        public int getAqi() { return aqi; }
        public AqiCategory getCategory() { return category; }
        public String getPrimaryPollutant() { return primaryPollutant; }
    }

    public static AqiResult calculateOverallAqi(double pm25, double pm10) {
        int aqiPm25 = calculatePm25Aqi(pm25);
        int aqiPm10 = calculatePm10Aqi(pm10);

        int maxAqi = Math.max(aqiPm25, aqiPm10);
        String primary = (aqiPm25 >= aqiPm10) ? "PM2.5" : "PM10";
        AqiCategory category = getCategoryForAqi(maxAqi);

        return new AqiResult(maxAqi, category, primary);
    }

    public static int calculatePm25Aqi(double pm25) {
        double c = Math.floor(pm25 * 10.0) / 10.0;
        if (c < 0) return 0;
        if (c <= 12.0) return interpolate(c, 0.0, 12.0, 0, 50);
        if (c <= 35.4) return interpolate(c, 12.1, 35.4, 51, 100);
        if (c <= 55.4) return interpolate(c, 35.5, 55.4, 101, 150);
        if (c <= 150.4) return interpolate(c, 55.5, 150.4, 151, 200);
        if (c <= 250.4) return interpolate(c, 150.5, 250.4, 201, 300);
        if (c <= 500.4) return interpolate(c, 250.5, 500.4, 301, 500);
        return 500;
    }

    public static int calculatePm10Aqi(double pm10) {
        double c = Math.floor(pm10);
        if (c < 0) return 0;
        if (c <= 54) return interpolate(c, 0, 54, 0, 50);
        if (c <= 154) return interpolate(c, 55, 154, 51, 100);
        if (c <= 254) return interpolate(c, 155, 254, 101, 150);
        if (c <= 354) return interpolate(c, 255, 354, 151, 200);
        if (c <= 424) return interpolate(c, 355, 424, 201, 300);
        if (c <= 604) return interpolate(c, 425, 604, 301, 500);
        return 500;
    }

    private static int interpolate(double c, double cLow, double cHigh, int iLow, int iHigh) {
        double aqi = ((double) (iHigh - iLow) / (cHigh - cLow)) * (c - cLow) + iLow;
        return (int) Math.round(aqi);
    }

    public static AqiCategory getCategoryForAqi(int aqi) {
        if (aqi <= 50) return AqiCategory.GOOD;
        if (aqi <= 100) return AqiCategory.MODERATE;
        if (aqi <= 150) return AqiCategory.UNHEALTHY_SENSITIVE;
        if (aqi <= 200) return AqiCategory.UNHEALTHY;
        if (aqi <= 300) return AqiCategory.VERY_UNHEALTHY;
        return AqiCategory.HAZARDOUS;
    }
}
