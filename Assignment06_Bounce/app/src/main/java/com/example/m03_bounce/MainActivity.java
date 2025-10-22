package com.example.m03_bounce;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private BouncingBallView bbView;
    private SeekBar seekX, seekY, seekDX, seekDY;
    private Spinner spnColor;
    private EditText edtName;
    private Button btnAdd, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bbView  = findViewById(R.id.bbView);
        seekX   = findViewById(R.id.seekX);
        seekY   = findViewById(R.id.seekY);
        seekDX  = findViewById(R.id.seekDX);
        seekDY  = findViewById(R.id.seekDY);
        spnColor = findViewById(R.id.spnColor);
        edtName = findViewById(R.id.edtName);
        btnAdd  = findViewById(R.id.btnAdd);
        btnClear = findViewById(R.id.btnClear);

        String[] colors = new String[]{"Red", "Green", "Blue", "White", "Yellow", "Cyan", "Magenta"};
        spnColor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors));

        btnAdd.setOnClickListener(v -> {
            float x  = seekX.getProgress();
            float y  = seekY.getProgress();
            float dx = seekDX.getProgress() - 10f;
            float dy = seekDY.getProgress() - 10f;
            String name = edtName.getText().toString();
            int color = toColor(spnColor.getSelectedItem().toString());
            bbView.RussButtonPressed(color, x, y, dx, dy, name);
        });

        btnClear.setOnClickListener(v -> bbView.clearBalls());
    }

    private int toColor(String s) {
        String v = s.toLowerCase();
        if (v.contains("red")) return Color.RED;
        if (v.contains("green")) return Color.GREEN;
        if (v.contains("blue")) return Color.BLUE;
        if (v.contains("white")) return Color.WHITE;
        if (v.contains("yellow")) return Color.YELLOW;
        if (v.contains("cyan")) return Color.CYAN;
        if (v.contains("magenta")) return Color.MAGENTA;
        return Color.WHITE;
    }
}
