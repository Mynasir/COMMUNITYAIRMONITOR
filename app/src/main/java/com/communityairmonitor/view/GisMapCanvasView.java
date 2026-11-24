package com.communityairmonitor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.communityairmonitor.model.AirQualityNode;

import java.util.ArrayList;
import java.util.List;

public class GisMapCanvasView extends View {

    public interface OnNodeSelectedListener {
        void onNodeSelected(AirQualityNode node);
    }

    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint contourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint highwayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint highwayDashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint plumePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint windPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint hudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint markerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private boolean showNodes = true;
    private boolean showHeatmap = true;
    private boolean showWind = true;
    private boolean showTopo = true;

    private final List<AirQualityNode> nodes = new ArrayList<>();
    private AirQualityNode selectedNode = null;
    private OnNodeSelectedListener listener;

    private float pulseRadius = 0f;
    private boolean pulseGrowing = true;

    public GisMapCanvasView(Context context) {
        super(context);
        init();
    }

    public GisMapCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gridPaint.setColor(Color.parseColor("#3B4B3A"));
        gridPaint.setStrokeWidth(1.5f);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{4, 6}, 0));

        contourPaint.setColor(Color.parseColor("#232936"));
        contourPaint.setStrokeWidth(2f);
        contourPaint.setStyle(Paint.Style.STROKE);

        highwayPaint.setColor(Color.parseColor("#272A32"));
        highwayPaint.setStrokeWidth(18f);
        highwayPaint.setStyle(Paint.Style.STROKE);
        highwayPaint.setStrokeCap(Paint.Cap.ROUND);

        highwayDashPaint.setColor(Color.parseColor("#4CD7F6"));
        highwayDashPaint.setStrokeWidth(3f);
        highwayDashPaint.setStyle(Paint.Style.STROKE);
        highwayDashPaint.setPathEffect(new DashPathEffect(new float[]{12, 8}, 0));

        hudPaint.setColor(Color.parseColor("#94A3B8"));
        hudPaint.setTextSize(32f);
        hudPaint.setFakeBoldText(true);

        markerTextPaint.setColor(Color.WHITE);
        markerTextPaint.setTextSize(26f);

        postInvalidateOnAnimation();
    }

    public void setNodes(List<AirQualityNode> newNodes) {
        this.nodes.clear();
        if (newNodes != null) {
            this.nodes.addAll(newNodes);
            if (!this.nodes.isEmpty() && selectedNode == null) {
                selectedNode = this.nodes.get(0);
            }
        }
        invalidate();
    }

    public void setOnNodeSelectedListener(OnNodeSelectedListener listener) {
        this.listener = listener;
    }

    public void setLayerToggles(boolean nodes, boolean heatmap, boolean wind, boolean topo) {
        this.showNodes = nodes;
        this.showHeatmap = heatmap;
        this.showWind = wind;
        this.showTopo = topo;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        // Background canvas fill
        canvas.drawColor(Color.parseColor("#0B0E16"));

        // 1. Vector Grid Overlay
        float step = 70f;
        for (float x = 0; x < w; x += step) {
            canvas.drawLine(x, 0, x, h, gridPaint);
        }
        for (float y = 0; y < h; y += step) {
            canvas.drawLine(0, y, w, y, gridPaint);
        }

        // 2. GIS Topography Contour Lines
        if (showTopo) {
            Path contour = new Path();
            contour.moveTo(0, h * 0.25f);
            contour.cubicTo(w * 0.3f, h * 0.15f, w * 0.6f, h * 0.35f, w, h * 0.2f);
            canvas.drawPath(contour, contourPaint);

            Path contour2 = new Path();
            contour2.moveTo(0, h * 0.55f);
            contour2.cubicTo(w * 0.25f, h * 0.45f, w * 0.7f, h * 0.65f, w, h * 0.5f);
            canvas.drawPath(contour2, contourPaint);

            Path highway = new Path();
            highway.moveTo(-20, h * 0.8f);
            highway.cubicTo(w * 0.4f, h * 0.75f, w * 0.6f, h * 0.45f, w + 20, h * 0.25f);
            canvas.drawPath(highway, highwayPaint);
            canvas.drawPath(highway, highwayDashPaint);
        }

        // 3. Heatmap Plumes
        if (showHeatmap) {
            // Hazard Plume at Industrial North
            RadialGradient hazardGrad = new RadialGradient(w * 0.65f, h * 0.45f, 220f,
                    new int[]{Color.parseColor("#80EF4444"), Color.parseColor("#4093000A"), Color.TRANSPARENT},
                    new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP);
            plumePaint.setShader(hazardGrad);
            canvas.drawCircle(w * 0.65f, h * 0.45f, 220f, plumePaint);

            // Warning Plume at Port Transit
            RadialGradient warnGrad = new RadialGradient(w * 0.35f, h * 0.3f, 160f,
                    new int[]{Color.parseColor("#70FFB95F"), Color.parseColor("#30653E00"), Color.TRANSPARENT},
                    new float[]{0f, 0.6f, 1f}, Shader.TileMode.CLAMP);
            plumePaint.setShader(warnGrad);
            canvas.drawCircle(w * 0.35f, h * 0.3f, 160f, plumePaint);

            // Clean Plume at Lincoln School
            RadialGradient cleanGrad = new RadialGradient(w * 0.25f, h * 0.7f, 150f,
                    new int[]{Color.parseColor("#6000FF66"), Color.parseColor("#2000531B"), Color.TRANSPARENT},
                    new float[]{0f, 0.7f, 1f}, Shader.TileMode.CLAMP);
            plumePaint.setShader(cleanGrad);
            canvas.drawCircle(w * 0.25f, h * 0.7f, 150f, plumePaint);
            plumePaint.setShader(null);
        }

        // 4. Wind Stream Vectors (14kt NW)
        if (showWind) {
            windPaint.setStyle(Paint.Style.STROKE);
            windPaint.setStrokeWidth(3.5f);
            windPaint.setStrokeCap(Paint.Cap.ROUND);
            LinearGradient windGrad = new LinearGradient(0, 0, w * 0.4f, h * 0.4f,
                    Color.parseColor("#4CD7F6"), Color.parseColor("#00FF66"), Shader.TileMode.CLAMP);
            windPaint.setShader(windGrad);

            for (int i = 0; i < 5; i++) {
                float startX = 80 + i * 140;
                float startY = 60 + (i % 3) * 90;
                canvas.drawLine(startX, startY, startX + 110, startY + 80, windPaint);
            }
            windPaint.setShader(null);
        }

        // 5. Sensor Nodes Markers
        if (showNodes && !nodes.isEmpty()) {
            // Animate pulse ring
            if (pulseGrowing) {
                pulseRadius += 0.4f;
                if (pulseRadius > 24f) pulseGrowing = false;
            } else {
                pulseRadius -= 0.4f;
                if (pulseRadius < 8f) pulseGrowing = true;
            }

            for (int i = 0; i < Math.min(nodes.size(), 12); i++) {
                AirQualityNode node = nodes.get(i);
                float nx = w * (0.15f + (i % 4) * 0.24f);
                float ny = h * (0.2f + (i / 4) * 0.26f);

                int color = Color.parseColor("#00FF66");
                if (node.getAqi() > 100) color = Color.parseColor("#EF4444");
                else if (node.getAqi() > 50) color = Color.parseColor("#FFB95F");

                // Pulse ring for active/selected node
                markerPaint.setStyle(Paint.Style.STROKE);
                markerPaint.setStrokeWidth(3f);
                markerPaint.setColor(color);
                markerPaint.setAlpha(120);
                canvas.drawCircle(nx, ny, 14f + pulseRadius, markerPaint);

                // Solid inner node dot
                markerPaint.setStyle(Paint.Style.FILL);
                markerPaint.setColor(color);
                markerPaint.setAlpha(255);
                canvas.drawCircle(nx, ny, 10f, markerPaint);

                // Station Tag Label
                markerPaint.setColor(Color.parseColor("#1C1F28"));
                canvas.drawRect(nx - 50, ny + 16, nx + 50, ny + 46, markerPaint);
                markerPaint.setStyle(Paint.Style.STROKE);
                markerPaint.setColor(Color.parseColor("#3B4B3A"));
                markerPaint.setStrokeWidth(1.5f);
                canvas.drawRect(nx - 50, ny + 16, nx + 50, ny + 46, markerPaint);

                canvas.drawText(node.getId(), nx - 38, ny + 38, markerTextPaint);
            }
        }

        // 6. HUD Coordinate Stamp
        markerPaint.setStyle(Paint.Style.FILL);
        markerPaint.setColor(Color.parseColor("#CC181B24"));
        canvas.drawRect(20, 20, 460, 80, markerPaint);
        canvas.drawText("37.7749° N, 122.4194° W", 36, 58, hudPaint);

        postInvalidateOnAnimation();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !nodes.isEmpty()) {
            float x = event.getX();
            float y = event.getY();
            int w = getWidth();
            int h = getHeight();

            for (int i = 0; i < Math.min(nodes.size(), 12); i++) {
                float nx = w * (0.15f + (i % 4) * 0.24f);
                float ny = h * (0.2f + (i / 4) * 0.26f);
                double dist = Math.hypot(x - nx, y - ny);
                if (dist < 60) {
                    selectedNode = nodes.get(i);
                    if (listener != null) {
                        listener.onNodeSelected(selectedNode);
                    }
                    invalidate();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
