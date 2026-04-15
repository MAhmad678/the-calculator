// MainActivity.java
package com.app.thecalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

        TextView display;
        String current = "";
        double firstNumber = 0;
        String operator = "";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                display = findViewById(R.id.display);
        }

        public void onButtonClick(View view) {
                Button btn = (Button) view;
                String text = btn.getText().toString();

                if (text.equals("AC")) {
                        current = "";
                        firstNumber = 0;
                        operator = "";
                        display.setText("0");
                        return;
                }

                if (text.equals("DEL")) {
                        if (current.length() > 0) {
                                current = current.substring(0, current.length() - 1);
                                display.setText(current.isEmpty() ? "0" : current);
                        }
                        return;
                }

                if (text.equals("=")) {
                        if (current.isEmpty()) return;

                        double secondNumber = Double.parseDouble(current);
                        double result = 0;

                        switch (operator) {
                                case "+": result = firstNumber + secondNumber; break;
                                case "-": result = firstNumber - secondNumber; break;
                                case "*": result = firstNumber * secondNumber; break;
                                case "/": result = secondNumber != 0 ? firstNumber / secondNumber : 0; break;
                                case "%": result = firstNumber % secondNumber; break;
                                default: return;
                        }

                        // Clean up decimal point if result is a whole number
                        if (result == (long) result) {
                                display.setText(String.format("%d", (long) result));
                                current = String.format("%d", (long) result);
                        } else {
                                display.setText(String.valueOf(result));
                                current = String.valueOf(result);
                        }

                        operator = "";
                        return;
                }

                if (text.equals("+") || text.equals("-") || text.equals("*") || text.equals("/") || text.equals("%")) {
                        if (!current.isEmpty()) {
                                firstNumber = Double.parseDouble(current);
                        }
                        operator = text;
                        current = "";
                        display.setText(operator);
                        return;
                }

                // Numbers and dot
                current += text;
                display.setText(current);
        }
}
