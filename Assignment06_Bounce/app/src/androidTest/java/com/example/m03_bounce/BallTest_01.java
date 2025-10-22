package com.example.m03_bounce;

import static org.junit.Assert.assertEquals;

import android.graphics.Color;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BallTest_01 {

    Box box;

    @Before
    public void setUp() throws Exception {
        // create the box
        box = new Box(Color.BLACK);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setAcc() {

    }

    @Test
    public void moveWithCollisionDetectionTest_01() {
        Log.w("AndroidTest", "moveWithCollisionDetectionTest_01" );
        Ball ball = new Ball(Color.YELLOW, 5.0f,5.0f,1.0f,1.0f);
        ball.moveWithCollisionDetection(box);

        Boolean moveWorked = ( (ball.x == 6.0f) && (ball.y == 6.0f) );
        assertEquals(true, moveWorked);
    }
}