package com.communityairmonitor;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

/**
 * Applies Material You dynamic color when the user opts in from Profile.
 * The branded forest/mint palette remains the default.
 */
public class CaqmnApplication extends Application {

    public static final String PREFS_NAME = "caqmn_prefs";
    public static final String KEY_DYNAMIC_COLOR = "use_dynamic_color";

    @Override
    public void onCreate() {
        super.onCreate();
        boolean dynamicColor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getBoolean(KEY_DYNAMIC_COLOR, false);
        if (dynamicColor) {
            DynamicColors.applyToActivitiesIfAvailable(this);
        }
    }
}
