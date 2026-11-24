package com.communityairmonitor.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import com.communityairmonitor.data.local.entity.SensorReadingEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PdfReportGenerator {

    public static void generateEnvironmentalReport(Context context, String location, List<SensorReadingEntity> readings) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        // Title
        titlePaint.setColor(Color.parseColor("#145A52"));
        titlePaint.setTextSize(20);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("CAQMN Environmental Pollution Report", 40, 60, titlePaint);

        paint.setColor(Color.parseColor("#18332F"));
        paint.setTextSize(12);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        canvas.drawText("Report Generated: " + sdf.format(new Date()), 40, 90, paint);
        canvas.drawText("Monitoring Location: " + location, 40, 110, paint);
        canvas.drawText("Environmental Standard: US EPA AQI (2024)", 40, 130, paint);

        canvas.drawLine(40, 150, 555, 150, paint);

        int y = 180;
        paint.setFakeBoldText(true);
        canvas.drawText("Timestamp", 40, y, paint);
        canvas.drawText("PM2.5 (µg/m³)", 180, y, paint);
        canvas.drawText("PM10 (µg/m³)", 310, y, paint);
        canvas.drawText("Temp (°C)", 440, y, paint);
        paint.setFakeBoldText(false);

        y += 20;

        if (readings != null) {
            int count = Math.min(readings.size(), 20);
            for (int i = 0; i < count; i++) {
                SensorReadingEntity r = readings.get(i);
                canvas.drawText(sdf.format(new Date(r.getTimestamp())), 40, y, paint);
                canvas.drawText(String.format(Locale.getDefault(), "%.1f", r.getPm25()), 180, y, paint);
                canvas.drawText(String.format(Locale.getDefault(), "%.1f", r.getPm10()), 310, y, paint);
                canvas.drawText(String.format(Locale.getDefault(), "%.1f", r.getTemperature()), 440, y, paint);
                y += 22;
            }
        }

        pdfDocument.finishPage(page);

        File pdfFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "CAQMN_Report_" + System.currentTimeMillis() + ".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            Toast.makeText(context, "Report saved to: " + pdfFile.getName(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "Failed to generate report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }
    }
}
