package com.communityairmonitor.ui.sensors;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.communityairmonitor.R;
import com.communityairmonitor.data.repository.CaqmnRepository;
import com.google.android.material.appbar.MaterialToolbar;

public class SensorDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SENSOR_ID = "extra_sensor_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        MaterialToolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String sensorId = getIntent().getStringExtra(EXTRA_SENSOR_ID);
        if (sensorId == null) sensorId = "OBUASI_001";

        TextView tvDetailSensorId = findViewById(R.id.tvDetailSensorId);
        TextView tvDetailSensorName = findViewById(R.id.tvDetailSensorName);
        TextView tvDetailLocation = findViewById(R.id.tvDetailLocation);

        tvDetailSensorId.setText(sensorId);

        CaqmnRepository repository = CaqmnRepository.getInstance(this);
        String finalSensorId = sensorId;
        repository.getSensorById(sensorId).observe(this, sensor -> {
            if (sensor != null) {
                tvDetailSensorName.setText(sensor.getName());
                tvDetailLocation.setText(String.format("Location: %s (%.4f° N, %.4f° W)",
                        sensor.getLocation(), sensor.getLatitude(), sensor.getLongitude()));
            }
        });

        findViewById(R.id.btnDeactivateSensor).setOnClickListener(v -> {
            Toast.makeText(this, "Admin Action: Sensor " + finalSensorId + " state updated.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
