package com.example.m03_bounce;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Box {
    public float xMin, xMax, yMin, yMax;
    private final Paint paint;
    private final RectF rect;

    public Box(int color) {
        paint = new Paint();
        paint.setColor(color);
        rect = new RectF();
    }

    public void set(float xMin, float yMin, float xMax, float yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
        rect.set(xMin, yMin, xMax, yMax);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }
}
