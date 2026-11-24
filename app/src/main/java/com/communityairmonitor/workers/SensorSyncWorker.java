package com.communityairmonitor.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.communityairmonitor.R;
import com.communityairmonitor.data.local.entity.SensorReadingEntity;
import com.communityairmonitor.data.repository.CaqmnRepository;
import com.communityairmonitor.domain.calculator.AqiCalculator;

import java.util.Random;

public class SensorSyncWorker extends Worker {

    private static final String CHANNEL_ID = "caqmn_alerts_channel";
    private final CaqmnRepository repository;
    private final Random random = new Random();

    public SensorSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        repository = CaqmnRepository.getInstance(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            // Generate periodic telemetric sensor reading
            long now = System.currentTimeMillis();
            double pm25 = 10.0 + random.nextDouble() * 35.0;
            double pm10 = pm25 * 1.7;
            double pm1 = pm25 * 0.45;
            double temp = 22.0 + random.nextDouble() * 8.0;
            double humid = 45.0 + random.nextDouble() * 25.0;
            double gas = 100.0 + random.nextDouble() * 80.0;

            SensorReadingEntity newReading = new SensorReadingEntity(
                    "RDG-BG-" + now,
                    "OBUASI_001",
                    pm1, pm25, pm10, temp, humid, gas, now
            );

            repository.insertReading(newReading);

            // If PM2.5 exceeds EPA WHO target (35.5 ug/m3), post notification
            if (pm25 > 35.5) {
                postNotification("High PM2.5 Alert", String.format("Elevated concentration recorded: %.1f µg/m³", pm25));
            }

            return Result.success();
        } catch (Exception e) {
            return Result.retry();
        }
    }

    private void postNotification(String title, String message) {
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Air Quality Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for high environmental pollution events.");
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
