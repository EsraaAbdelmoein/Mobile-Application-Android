package com.example.m03_bounce;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DBClassTest_01 {

    DBClass DBtest;  // class level declaration

    @Before
    public void setUp() throws Exception {
        Log.w("AndroidTest", "setUp()");
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DBtest = new DBClass(appContext);

        // Default rows reset each test
        int doCount = DBtest.count();
        if (doCount > 0) {
            Log.v("DBClass", "already rows in DB");
        } else {
            Log.v("DBClass", "n...add some rows");
            DataModel a = new DataModel(0, 20.0f, 20.0f, -4.0f, 4.0f);
            DBtest.save(a);
            a = new DataModel(0, 30f, 30f, 3f, -3f);
            DBtest.save(a);
        }

        List<DataModel> ALL = DBtest.findAll();
        for (DataModel one : ALL) {
            Log.w("AndroidTest", "Setup()=> Item => " + one.toString());
        }
    }

    @After
    public void tearDown() throws Exception {
        List<DataModel> ALL = DBtest.findAll();
        for (DataModel one : ALL) {
            Log.w("AndroidTest", "tearDown () Item => " + one.toString());
        }
    }

    @Test
    public void onCreate() {
    }

    @Test
    public void onUpgrade() {
    }

    @Test
    public void count() {
    }

    @Test
    public void saveTest_01() {
        Log.v("AndroidTest", "saveTest_01");

        // note that 'id' is created as the record is saved in the DB
        DataModel newBall = new DataModel(0, 5.0f, 5.0f, 5.0f, 5.0f);
        DBtest.save(newBall);

        // Count should be 3, latest ID should be 3 which is this record
        int doCount = DBtest.count();
        assertEquals(3, doCount);

        List<DataModel> ALL = DBtest.findAll();
        Boolean found = false;
        for (DataModel one : ALL) {
            Log.w("AndroidTest", "saveTest_01=> Item => " + one.toString());
            if ((one.getId() == 3) &&
                    (one.getModelX() == 5.0f) &&
                    (one.getModelY() == 5.0f) &&
                    (one.getModelDX() == 5.0f) &&
                    (one.getModelDY() == 5.0f)) {
                found = true;
            }
        }

        // Expect to find the added record.
        assertEquals(true, found);

    }

    @Test
    public void update() {
    }

    @Test
    public void deleteByIdTest_01() {
        DBtest.deleteById(1L);
        List<DataModel> ALL = DBtest.findAll();
        for (DataModel one : ALL) {
            Log.w("AndroidTest", "Setup()=> Item => " + one.toString());
        }
    }

    @Test
    public void deleteByIdTest_02() {
        DBtest.deleteById(2L);
        List<DataModel> ALL = DBtest.findAll();
        for (DataModel one : ALL) {
            Log.w("AndroidTest", "Setup()=> Item => " + one.toString());
        }
    }

    @Test
    public void deleteByIdTest_03() {
        DBtest.deleteById(3L);
        List<DataModel> ALL = DBtest.findAll();
        for (DataModel one : ALL) {
            Log.w("AndroidTest", "Setup()=> Item => " + one.toString());
        }
    }

    @Test
    public void findAll() {
    }

    @Test
    public void getNameById() {
    }
}