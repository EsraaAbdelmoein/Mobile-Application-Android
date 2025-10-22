package com.example.m07_interactivegalaxy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GalaxyView extends View implements SensorEventListener {

    private static final String TAG = "M07-Galaxy";

    private final List<Star> stars = new ArrayList<>();
    private final Random random = new Random();

    private final Paint starCorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint starGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint starStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paddleHeadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paddleEdgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paddleHandlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private SensorManager sensorManager;
    private Sensor gravitySensor;

    private long lastTimeNs = 0L;

    private float accelScale = 9f;
    private float friction = 0.972f;
    private float bounceLoss = 0.32f;
    private float maxSpeed = 650f;

    private int maxStars = 800;

    private long lastLogNs = 0L;
    private long logEveryNs = 1_200_000_000L;

    private float paddleCenterX = 0f;
    private float paddleCenterY = 0f;
    private float paddleHeadRadius = 78f;
    private float paddleHandleLen = 95f;
    private float paddleHandleWidth = 28f;
    private float paddleMarginBottom = 84f;
    private final RectF paddleRect = new RectF();
    private float paddleAngle = 0f;
    private float lastPaddleX = 0f;

    private float controlBandTop = 0f;

    private int score = 0;
    private int targetScore = 4;
    private boolean win = false;

    private long lastScoreNs = 0L;
    private long scoreEveryNs = 250_000_000L;

    public GalaxyView(Context context) { super(context); init(context); }
    public GalaxyView(Context context, AttributeSet attrs) { super(context, attrs); init(context); }
    public GalaxyView(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); init(context); }

    private void init(Context ctx) {
        starCorePaint.setStyle(Paint.Style.FILL);
        starGlowPaint.setStyle(Paint.Style.FILL);

        starStroke.setStyle(Paint.Style.STROKE);
        starStroke.setStrokeWidth(3.5f);
        starStroke.setColor(0xCCFFFFFF);

        textPaint.setColor(0xFFEEEEEE);
        textPaint.setTextSize(34f);

        paddleHeadPaint.setColor(0xFFE94F37);
        paddleEdgePaint.setStyle(Paint.Style.STROKE);
        paddleEdgePaint.setStrokeWidth(6f);
        paddleEdgePaint.setColor(0xFF222222);
        paddleHandlePaint.setColor(0xFF7F5A27);

        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        setFocusable(true);
        setClickable(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (sensorManager != null && gravitySensor != null) {
            sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
        }
        lastTimeNs = System.nanoTime();
        Log.d(TAG, "attached");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (sensorManager != null) sensorManager.unregisterListener(this);
        Log.d(TAG, "detached");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paddleCenterX = w * 0.5f;
        paddleCenterY = h - paddleMarginBottom - paddleHeadRadius;
        controlBandTop = h * 0.65f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getPointerCount() >= 2 && event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            score = 0;
            win = false;
            invalidate();
            return true;
        }

        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            if (y >= controlBandTop) {
                float maxY = getHeight() - paddleMarginBottom - paddleHeadRadius;
                float minY = controlBandTop - 10f;
                paddleCenterX = x;
                paddleCenterY = clamp(y, minY, maxY);
            } else {
                if (action == MotionEvent.ACTION_DOWN) {
                    addStar(x, y);
                }
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void addStar(float x, float y) {
        if (stars.size() >= maxStars) stars.remove(0);
        float vx = (random.nextFloat() - 0.5f) * 6f;
        float vy = (random.nextFloat() - 0.5f) * 6f;
        float radius = 28f + random.nextFloat() * 18f;
        int base = randomPastel();
        stars.add(new Star(x, y, vx, vy, radius, 255, base));
    }

    private int randomPastel() {
        int r = 150 + random.nextInt(100);
        int g = 150 + random.nextInt(100);
        int b = 150 + random.nextInt(100);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_GRAVITY) return;

        long now = System.nanoTime();
        float dt = (lastTimeNs == 0L) ? 0f : (now - lastTimeNs) / 1_000_000_000f;
        lastTimeNs = now;

        float gx = event.values[0];
        float gy = event.values[1];

        int w = getWidth();
        int h = getHeight();

        float left = paddleCenterX - paddleHeadRadius;
        float right = paddleCenterX + paddleHeadRadius;
        float top = paddleCenterY - paddleHeadRadius;
        float bottom = paddleCenterY + paddleHeadRadius + paddleHandleLen;
        paddleRect.set(left, top, right, bottom);

        float dx = paddleCenterX - lastPaddleX;
        lastPaddleX = paddleCenterX;
        paddleAngle += (dx * 0.12f - paddleAngle) * Math.min(1f, dt * 10f);
        if (paddleAngle > 14f) paddleAngle = 14f;
        if (paddleAngle < -14f) paddleAngle = -14f;

        boolean scoredThisFrame = false;

        Iterator<Star> it = stars.iterator();
        while (it.hasNext()) {
            Star s = it.next();

            s.vx += -gx * accelScale * dt;
            s.vy += gy * accelScale * dt;

            s.vx *= friction;
            s.vy *= friction;

            float sp = (float) Math.hypot(s.vx, s.vy);
            if (sp > maxSpeed) {
                float k = maxSpeed / sp;
                s.vx *= k;
                s.vy *= k;
            }

            s.x += s.vx * dt;
            s.y += s.vy * dt;

            if (s.x < s.radius) { s.x = s.radius; s.vx = -s.vx * bounceLoss; }
            if (s.x > w - s.radius) { s.x = w - s.radius; s.vx = -s.vx * bounceLoss; }
            if (s.y < s.radius) { s.y = s.radius; s.vy = -s.vy * bounceLoss; }
            if (s.y > h - s.radius) { s.y = h - s.radius; s.vy = -s.vy * bounceLoss; }

            if (s.cooldown > 0f) s.cooldown -= dt;
            if (circleIntersectsRect(s.x, s.y, s.radius, paddleRect)) {
                if (s.cooldown <= 0f && !win) {
                    if (!scoredThisFrame && now - lastScoreNs > scoreEveryNs) {
                        score += 1;
                        lastScoreNs = now;
                        scoredThisFrame = true;
                        if (score >= targetScore) win = true;
                    }
                    s.vy = -Math.abs(s.vy) * (0.68f + 0.34f * random.nextFloat());
                    s.y = top - s.radius - 1f;
                    s.vx *= 0.98f;
                    s.cooldown = 0.18f;
                }
            }
        }

        if (now - lastLogNs > logEveryNs) {
            Log.d(TAG, String.format("g(%.2f,%.2f) stars=%d score=%d", gx, gy, stars.size(), score));
            lastLogNs = now;
        }
        invalidate();
    }

    private boolean circleIntersectsRect(float cx, float cy, float r, RectF rect) {
        float nx = Math.max(rect.left, Math.min(cx, rect.right));
        float ny = Math.max(rect.top, Math.min(cy, rect.bottom));
        float dx = cx - nx;
        float dy = cy - ny;
        return dx * dx + dy * dy <= r * r;
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(0xFF0B0F1A);

        for (Star s : stars) {
            int base = s.color & 0x00FFFFFF;
            int colorWithAlpha = 0xFF000000 | base;

            starCorePaint.setColor(colorWithAlpha);
            canvas.drawCircle(s.x, s.y, s.radius * 0.72f, starCorePaint);

            int edgeAlpha = 0x55000000;
            RadialGradient g = new RadialGradient(
                    s.x, s.y, s.radius * 1.25f,
                    colorWithAlpha,
                    (edgeAlpha | base),
                    Shader.TileMode.CLAMP);
            starGlowPaint.setShader(g);
            canvas.drawCircle(s.x, s.y, s.radius, starGlowPaint);
            starGlowPaint.setShader(null);

            canvas.drawCircle(s.x, s.y, s.radius, starStroke);
        }

        drawPaddle(canvas);

        textPaint.setTextSize(34f);
        canvas.drawText("Top: tap to spawn one ball  •  Bottom: drag paddle", 24f, 48f, textPaint);
        canvas.drawText("Score: " + score + " / " + targetScore, 24f, 90f, textPaint);

        if (win) {
            Paint winPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            winPaint.setColor(0xFFFFFFFF);
            winPaint.setTextSize(64f);
            float tw = winPaint.measureText("You Win! 🎉");
            float x = (getWidth() - tw) * 0.5f;
            float y = getHeight() * 0.45f;
            canvas.drawText("You Win! 🎉", x, y, winPaint);
            textPaint.setTextSize(28f);
            String hint = "Two-finger tap to reset";
            float hw = textPaint.measureText(hint);
            canvas.drawText(hint, (getWidth() - hw) * 0.5f, y + 48f, textPaint);
        }
    }

    private void drawPaddle(Canvas canvas) {
        float cx = paddleCenterX;
        float cy = paddleCenterY;

        canvas.save();
        canvas.rotate(paddleAngle, cx, cy + paddleHeadRadius * 0.35f);

        float gradR = paddleHeadRadius * 1.15f;
        RadialGradient headGrad = new RadialGradient(
                cx - paddleHeadRadius * 0.3f, cy - paddleHeadRadius * 0.3f, gradR,
                0xFFFF7660, 0xFFCC2E1D, Shader.TileMode.CLAMP);
        paddleHeadPaint.setShader(headGrad);
        canvas.drawCircle(cx, cy, paddleHeadRadius, paddleHeadPaint);
        paddleHeadPaint.setShader(null);
        canvas.drawCircle(cx, cy, paddleHeadRadius, paddleEdgePaint);

        float hw = paddleHandleWidth;
        RectF handle = new RectF(cx - hw * 0.5f, cy + paddleHeadRadius - 6f, cx + hw * 0.5f, cy + paddleHeadRadius + paddleHandleLen);
        canvas.drawRoundRect(handle, hw * 0.5f, hw * 0.5f, paddleHandlePaint);
        canvas.restore();
    }

    private static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    private static class Star {
        float x, y;
        float vx, vy;
        float radius;
        int alpha;
        int color;
        float cooldown = 0f;

        Star(float x, float y, float vx, float vy, float radius, int alpha, int color) {
            this.x = x; this.y = y;
            this.vx = vx; this.vy = vy;
            this.radius = radius;
            this.alpha = alpha;
            this.color = color;
        }
    }
}
