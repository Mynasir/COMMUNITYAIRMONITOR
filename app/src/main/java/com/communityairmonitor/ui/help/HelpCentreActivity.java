package com.communityairmonitor.ui.help;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.communityairmonitor.R;
import com.google.android.material.appbar.MaterialToolbar;

public class HelpCentreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_centre);

        MaterialToolbar toolbar = findViewById(R.id.toolbarHelpCentre);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
