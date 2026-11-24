package com.communityairmonitor;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.communityairmonitor.fragment.AlertsFragment;
import com.communityairmonitor.fragment.AnalyticsFragment;
import com.communityairmonitor.fragment.DashboardFragment;
import com.communityairmonitor.fragment.MonitoringFragment;
import com.communityairmonitor.fragment.ProfileFragment;
import com.communityairmonitor.ui.about.AboutActivity;
import com.communityairmonitor.ui.auth.LoginActivity;
import com.communityairmonitor.ui.community.CommunityFragment;
import com.communityairmonitor.ui.help.HelpCentreActivity;
import com.communityairmonitor.ui.map.CommunityMapFragment;
import com.communityairmonitor.ui.profile.EditProfileActivity;
import com.communityairmonitor.ui.reports.ExportReportActivity;
import com.communityairmonitor.ui.settings.SettingsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.topToolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // ActionBarDrawerToggle for Hamburger Icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.nav_home, R.string.nav_home
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle Side Navigation Drawer Click Events
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment fragment = null;

            if (itemId == R.id.nav_home) {
                fragment = new DashboardFragment();
                bottomNavigation.setSelectedItemId(R.id.nav_home);
            } else if (itemId == R.id.nav_monitoring) {
                fragment = new MonitoringFragment();
                bottomNavigation.setSelectedItemId(R.id.nav_monitoring);
            } else if (itemId == R.id.nav_analytics) {
                fragment = new AnalyticsFragment();
                bottomNavigation.setSelectedItemId(R.id.nav_analytics);
            } else if (itemId == R.id.nav_map) {
                fragment = new CommunityMapFragment();
            } else if (itemId == R.id.nav_alerts) {
                fragment = new AlertsFragment();
                bottomNavigation.setSelectedItemId(R.id.nav_alerts);
            } else if (itemId == R.id.nav_community || itemId == R.id.nav_contributors) {
                fragment = new CommunityFragment();
            } else if (itemId == R.id.nav_reports) {
                startActivity(new Intent(this, ExportReportActivity.class));
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment();
                bottomNavigation.setSelectedItemId(R.id.nav_profile);
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            } else if (itemId == R.id.nav_help) {
                startActivity(new Intent(this, HelpCentreActivity.class));
            } else if (itemId == R.id.nav_about) {
                startActivity(new Intent(this, AboutActivity.class));
            } else if (itemId == R.id.nav_logout) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Handle Bottom Navigation Bar Click Events
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_monitoring) {
                selectedFragment = new MonitoringFragment();
            } else if (itemId == R.id.nav_analytics) {
                selectedFragment = new AnalyticsFragment();
            } else if (itemId == R.id.nav_alerts) {
                selectedFragment = new AlertsFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            } else {
                selectedFragment = new DashboardFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        });

        if (savedInstanceState == null) {
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        }
    }

    public void navigateTo(int navId) {
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(navId);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
