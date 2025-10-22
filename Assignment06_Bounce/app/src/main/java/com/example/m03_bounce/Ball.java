package com.example.m03_bounce;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class Ball {

    float radius = 50;
    float x;
    float y;
    float speedX;
    float speedY;
    private RectF bounds;
    private Paint paint;

    private double ax, ay, az = 0;

    public void setAcc(double ax, double ay, double az){
        this.ax = ax;
        this.ay = ay;
        this.az = az;
    }

    Random r = new Random();

    public Ball(int color) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);
        x = radius + r.nextInt(800);
        y = radius + r.nextInt(800);
        speedX = r.nextInt(10) - 5;
        speedY = r.nextInt(10) - 5;
    }

    public Ball(int color, float x, float y, float speedX, float speedY) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public void moveWithCollisionDetection(Box box) {
        x += speedX;
        y += speedY;
        speedX += ax;
        speedY += ay;
        if (x + radius > box.xMax) {
            speedX = -speedX;
            x = box.xMax - radius;
        } else if (x - radius < box.xMin) {
            speedX = -speedX;
            x = box.xMin + radius;
        }
        if (y + radius > box.yMax) {
            speedY = -speedY;
            y = box.yMax - radius;
        } else if (y - radius < box.yMin) {
            speedY = -speedY;
            y = box.yMin + radius;
        }
    }

    public void draw(Canvas canvas) {
        if (bounds == null) bounds = new RectF();
        if (paint == null) paint = new Paint();
        bounds.set(x - radius, y - radius, x + radius, y + radius);
        canvas.drawOval(bounds, paint);
    }
}
