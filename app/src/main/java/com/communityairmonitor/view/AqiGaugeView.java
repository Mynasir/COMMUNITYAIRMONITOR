package com.communityairmonitor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class AqiGaugeView extends View {

    private final Paint segPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint cursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();

    private int aqi = 38;

    public AqiGaugeView(Context context) {
        super(context);
    }

    public AqiGaugeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        float gap = 6f;
        float segW = (w - (gap * 3)) / 4f;
        float barH = h * 0.4f;
        float top = (h - barH) / 2f;

        // 4 Segments
        int[] colors = {
                Color.parseColor("#00FF66"), // 0-50 Good
                Color.parseColor("#FFB95F"), // 51-100 Moderate
                Color.parseColor("#EF4444"), // 101-150 Unhealthy
                Color.parseColor("#93000A")  // 150+ Hazard
        };

        for (int i = 0; i < 4; i++) {
            float left = i * (segW + gap);
            rect.set(left, top, left + segW, top + barH);
            segPaint.setColor(colors[i]);
            // If AQI is not in this range, dim segment
            if ((i == 0 && aqi > 50) || (i == 1 && (aqi <= 50 || aqi > 100)) ||
                (i == 2 && (aqi <= 100 || aqi > 150)) || (i == 3 && aqi <= 150)) {
                segPaint.setAlpha(80);
            } else {
                segPaint.setAlpha(255);
            }
            canvas.drawRoundRect(rect, 6f, 6f, segPaint);
        }

        // Active AQI cursor marker
        float clampedAqi = Math.min(200, Math.max(0, aqi));
        float cursorRatio = clampedAqi / 200f;
        float cursorX = cursorRatio * w;

        cursorPaint.setColor(Color.WHITE);
        cursorPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cursorX, top + barH / 2f, barH * 0.65f, cursorPaint);

        cursorPaint.setColor(Color.parseColor("#10131B"));
        canvas.drawCircle(cursorX, top + barH / 2f, barH * 0.35f, cursorPaint);
    }
}
