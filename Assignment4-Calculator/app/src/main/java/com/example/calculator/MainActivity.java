package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etNum1;
    private EditText etNum2;
    private TextView tvAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNum1 = findViewById(R.id.editText_Num1);
        etNum2 = findViewById(R.id.editText_Num2);
        tvAnswer = findViewById(R.id.textView_Answer);
    }

    // Helpers
    private Double readNumber(EditText e) {
        String s = e.getText().toString().trim();
        if (s.isEmpty()) return null;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void showResult(String label, Double value) {
        if (value == null) {
            tvAnswer.setText(label + " Invalid input");
        } else {
            tvAnswer.setText(label + " " + value);
        }
    }

    // Button handlers
    public void onAdd(View v) {
        Double a = readNumber(etNum1);
        Double b = readNumber(etNum2);
        if (a == null || b == null) { showResult("Answer:", null); return; }
        showResult("Answer:", a + b);
    }

    public void onSub(View v) {
        Double a = readNumber(etNum1);
        Double b = readNumber(etNum2);
        if (a == null || b == null) { showResult("Answer:", null); return; }
        showResult("Answer:", a - b);
    }

    public void onMul(View v) {
        Double a = readNumber(etNum1);
        Double b = readNumber(etNum2);
        if (a == null || b == null) { showResult("Answer:", null); return; }
        showResult("Answer:", a * b);
    }

    public void onDiv(View v) {
        Double a = readNumber(etNum1);
        Double b = readNumber(etNum2);
        if (a == null || b == null) { showResult("Answer:", null); return; }
        if (b == 0) {
            tvAnswer.setText("Answer: Cannot divide by zero");
            return;
        }
        showResult("Answer:", a / b);
    }
}
