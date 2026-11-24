package com.communityairmonitor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.communityairmonitor.model.TelemetryPoint;

import java.util.ArrayList;
import java.util.List;

public class TrendChartCanvasView extends View {

    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint whoLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint whoTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Paint pm25StrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pm25FillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pm10StrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pm10FillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Paint scrubberLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint tooltipBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint tooltipTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final List<TelemetryPoint> points = new ArrayList<>();
    private float scrubberX = -1f;

    public TrendChartCanvasView(Context context) {
        super(context);
        init();
    }

    public TrendChartCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gridPaint.setColor(Color.parseColor("#272A32"));
        gridPaint.setStrokeWidth(1.5f);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));

        whoLinePaint.setColor(Color.parseColor("#EF4444"));
        whoLinePaint.setStrokeWidth(2.5f);
        whoLinePaint.setStyle(Paint.Style.STROKE);
        whoLinePaint.setPathEffect(new DashPathEffect(new float[]{8, 6}, 0));

        whoTextPaint.setColor(Color.parseColor("#FFDAD6"));
        whoTextPaint.setTextSize(24f);
        whoTextPaint.setFakeBoldText(true);

        pm25StrokePaint.setColor(Color.parseColor("#00FF66"));
        pm25StrokePaint.setStrokeWidth(5f);
        pm25StrokePaint.setStyle(Paint.Style.STROKE);
        pm25StrokePaint.setStrokeCap(Paint.Cap.ROUND);
        pm25StrokePaint.setStrokeJoin(Paint.Join.ROUND);

        pm10StrokePaint.setColor(Color.parseColor("#4CD7F6"));
        pm10StrokePaint.setStrokeWidth(4f);
        pm10StrokePaint.setStyle(Paint.Style.STROKE);
        pm10StrokePaint.setStrokeCap(Paint.Cap.ROUND);
        pm10StrokePaint.setStrokeJoin(Paint.Join.ROUND);

        scrubberLinePaint.setColor(Color.parseColor("#4CD7F6"));
        scrubberLinePaint.setStrokeWidth(2f);
        scrubberLinePaint.setStyle(Paint.Style.STROKE);
        scrubberLinePaint.setPathEffect(new DashPathEffect(new float[]{6, 4}, 0));

        tooltipBgPaint.setColor(Color.parseColor("#E032353D"));
        tooltipBgPaint.setStyle(Paint.Style.FILL);

        tooltipTextPaint.setColor(Color.WHITE);
        tooltipTextPaint.setTextSize(26f);
        tooltipTextPaint.setFakeBoldText(true);

        dotPaint.setStyle(Paint.Style.FILL);
    }

    public void setPoints(List<TelemetryPoint> newPoints) {
        this.points.clear();
        if (newPoints != null) {
            this.points.addAll(newPoints);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        // Background
        canvas.drawColor(Color.parseColor("#0B0E16"));

        // Grid lines
        for (int i = 1; i <= 3; i++) {
            float y = h * (i / 4f);
            canvas.drawLine(0, y, w, y, gridPaint);
        }

        // WHO Limit Reference line (15 ug/m3 -> around y = h * 0.52)
        float whoY = h * 0.52f;
        canvas.drawLine(0, whoY, w, whoY, whoLinePaint);
        canvas.drawText("WHO LIMIT • 15 µg/m³", w - 260, whoY - 10, whoTextPaint);

        if (points.isEmpty()) return;

        // Build Paths
        Path pm25Path = new Path();
        Path pm10Path = new Path();
        Path pm25Fill = new Path();
        Path pm10Fill = new Path();

        float maxVal = 80f; // Max scale
        float stepX = (float) w / (points.size() - 1);

        for (int i = 0; i < points.size(); i++) {
            TelemetryPoint pt = points.get(i);
            float x = i * stepX;
            float y25 = h - (float) ((pt.getPm25() / maxVal) * (h * 0.8f)) - 20;
            float y10 = h - (float) ((pt.getPm10() / maxVal) * (h * 0.8f)) - 20;

            if (i == 0) {
                pm25Path.moveTo(x, y25);
                pm10Path.moveTo(x, y10);
                pm25Fill.moveTo(x, h);
                pm25Fill.lineTo(x, y25);
                pm10Fill.moveTo(x, h);
                pm10Fill.lineTo(x, y10);
            } else {
                float prevX = (i - 1) * stepX;
                float prevY25 = h - (float) ((points.get(i - 1).getPm25() / maxVal) * (h * 0.8f)) - 20;
                float prevY10 = h - (float) ((points.get(i - 1).getPm10() / maxVal) * (h * 0.8f)) - 20;

                float cx1 = prevX + stepX / 2f;
                pm25Path.cubicTo(cx1, prevY25, cx1, y25, x, y25);
                pm10Path.cubicTo(cx1, prevY10, cx1, y10, x, y10);

                pm25Fill.cubicTo(cx1, prevY25, cx1, y25, x, y25);
                pm10Fill.cubicTo(cx1, prevY10, cx1, y10, x, y10);
            }
        }

        pm25Fill.lineTo(w, h);
        pm25Fill.close();
        pm10Fill.lineTo(w, h);
        pm10Fill.close();

        // Area gradients
        LinearGradient g25 = new LinearGradient(0, 0, 0, h,
                Color.parseColor("#4000FF66"), Color.TRANSPARENT, Shader.TileMode.CLAMP);
        pm25FillPaint.setShader(g25);
        canvas.drawPath(pm25Fill, pm25FillPaint);

        LinearGradient g10 = new LinearGradient(0, 0, 0, h,
                Color.parseColor("#304CD7F6"), Color.TRANSPARENT, Shader.TileMode.CLAMP);
        pm10FillPaint.setShader(g10);
        canvas.drawPath(pm10Fill, pm10FillPaint);

        // Curves
        canvas.drawPath(pm10Path, pm10StrokePaint);
        canvas.drawPath(pm25Path, pm25StrokePaint);

        // Peak Marker Dot
        float peakX = 4 * stepX;
        float peakY = h - (float) ((points.get(4).getPm25() / maxVal) * (h * 0.8f)) - 20;
        dotPaint.setColor(Color.parseColor("#00FF66"));
        canvas.drawCircle(peakX, peakY, 9f, dotPaint);
        dotPaint.setColor(Color.parseColor("#10131B"));
        canvas.drawCircle(peakX, peakY, 4f, dotPaint);

        // Scrubber line & tooltip
        if (scrubberX >= 0) {
            canvas.drawLine(scrubberX, 0, scrubberX, h, scrubberLinePaint);

            int idx = Math.min(points.size() - 1, Math.max(0, Math.round(scrubberX / stepX)));
            TelemetryPoint curr = points.get(idx);
            String tooltipText = curr.getTimeLabel() + " | PM2.5: " + curr.getPm25() + " µg/m³";

            float rectLeft = Math.min(w - 320, Math.max(10, scrubberX - 160));
            canvas.drawRect(rectLeft, 10, rectLeft + 310, 60, tooltipBgPaint);
            canvas.drawText(tooltipText, rectLeft + 15, 46, tooltipTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scrubberX = event.getX();
        invalidate();
        return true;
    }
}
