package com.example.m03_bounce;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BouncingBallView extends View {

    private final ArrayList<Ball> balls = new ArrayList<>();
    private Box box;
    private DBClass DBtest;

    public BouncingBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        box = new Box(Color.BLACK);
        DBtest = new DBClass(context);
        List<DataModel> all = DBtest.findAll();
        Log.d("VIEW", "Loaded balls from DB: " + (all == null ? 0 : all.size()));
        if (all != null) {
            for (DataModel one : all) {
                balls.add(new Ball(
                        one.getColor(),
                        (float) one.getModelX(),
                        (float) one.getModelY(),
                        (float) one.getModelDX(),
                        (float) one.getModelDY()
                ));
            }
        }
        setFocusable(true);
        requestFocus();
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        box.draw(canvas);
        for (Ball b : balls) {
            b.draw(canvas);
            b.moveWithCollisionDetection(box);
        }
        invalidate();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        box.set(0, 0, w, h);
        Log.d("VIEW", "onSizeChanged w=" + w + " h=" + h);
    }

    public void RussButtonPressed(int color, float x, float y, float dx, float dy, String name) {
        Log.d("VIEW", "Add ball pressed: x=" + x + ", y=" + y + ", dx=" + dx + ", dy=" + dy + ", color=" + color + ", name=" + name);
        balls.add(new Ball(color, x, y, dx, dy));
        DataModel newBall = new DataModel(x, y, dx, dy, color, name);
        DBtest.save(newBall);
        invalidate();
    }

    public void clearBalls() {
        balls.clear();
        SQLiteDatabase db = DBtest.getWritableDatabase();
        db.delete("sample_table", null, null);
        db.close();
        Log.d("VIEW", "Cleared balls and DB");
        invalidate();
    }
}
