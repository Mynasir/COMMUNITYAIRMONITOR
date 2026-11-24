package com.communityairmonitor.ui.reports;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.communityairmonitor.R;
import com.communityairmonitor.data.repository.CaqmnRepository;
import com.communityairmonitor.utils.PdfReportGenerator;
import com.google.android.material.appbar.MaterialToolbar;

public class ExportReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_report);

        MaterialToolbar toolbar = findViewById(R.id.toolbarExportReport);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        findViewById(R.id.btnDoExportReport).setOnClickListener(v -> {
            CaqmnRepository repository = CaqmnRepository.getInstance(this);
            repository.getAllReadings().observe(this, readings -> {
                PdfReportGenerator.generateEnvironmentalReport(this, "Obuasi, Ashanti Region", readings);
                finish();
            });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
