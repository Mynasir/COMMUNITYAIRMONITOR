package com.communityairmonitor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class IngestSparklineView extends View {

    private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public IngestSparklineView(Context context) {
        super(context);
        init();
    }

    public IngestSparklineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        strokePaint.setColor(Color.parseColor("#4CD7F6"));
        strokePaint.setStrokeWidth(3.5f);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        float[] values = {0.7f, 0.65f, 0.55f, 0.60f, 0.35f, 0.42f, 0.28f, 0.32f, 0.48f, 0.18f, 0.25f, 0.12f, 0.30f, 0.20f};
        float stepX = (float) w / (values.length - 1);

        Path linePath = new Path();
        Path fillPath = new Path();

        for (int i = 0; i < values.length; i++) {
            float x = i * stepX;
            float y = values[i] * h;
            if (i == 0) {
                linePath.moveTo(x, y);
                fillPath.moveTo(x, h);
                fillPath.lineTo(x, y);
            } else {
                float prevX = (i - 1) * stepX;
                float prevY = values[i - 1] * h;
                float cx = prevX + stepX / 2f;
                linePath.cubicTo(cx, prevY, cx, y, x, y);
                fillPath.cubicTo(cx, prevY, cx, y, x, y);
            }
        }

        fillPath.lineTo(w, h);
        fillPath.close();

        LinearGradient g = new LinearGradient(0, 0, 0, h,
                Color.parseColor("#354CD7F6"), Color.TRANSPARENT, Shader.TileMode.CLAMP);
        fillPaint.setShader(g);

        canvas.drawPath(fillPath, fillPaint);
        canvas.drawPath(linePath, strokePaint);
    }
}
