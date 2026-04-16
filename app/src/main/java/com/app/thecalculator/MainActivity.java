import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvExpression, tvResult;
    private StringBuilder inputTracker = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);

        setupButton(R.id.btn0, "0"); setupButton(R.id.btn1, "1");
        setupButton(R.id.btn2, "2"); setupButton(R.id.btn3, "3");
        setupButton(R.id.btn4, "4"); setupButton(R.id.btn5, "5");
        setupButton(R.id.btn6, "6"); setupButton(R.id.btn7, "7");
        setupButton(R.id.btn8, "8"); setupButton(R.id.btn9, "9");
        setupButton(R.id.btnDot, ".");

        setupButton(R.id.btnAdd, "+"); setupButton(R.id.btnSub, "-");
        setupButton(R.id.btnMul, "×"); setupButton(R.id.btnDiv, "÷");

        findViewById(R.id.btnAc).setOnClickListener(v -> {
            inputTracker.setLength(0);
            tvExpression.setText("");
            tvResult.setText("");
        });

        findViewById(R.id.btnDel).setOnClickListener(v -> {
            if (inputTracker.length() > 0) {
                inputTracker.deleteCharAt(inputTracker.length() - 1);
                tvResult.setText(inputTracker.toString());
            }
        });

        findViewById(R.id.btnPlusMinus).setOnClickListener(v -> {
            if (inputTracker.length() > 0) {
                String current = inputTracker.toString();
                if (current.startsWith("-")) {
                    inputTracker.deleteCharAt(0);
                } else {
                    inputTracker.insert(0, "-");
                }
                tvResult.setText(inputTracker.toString());
            }
        });

        findViewById(R.id.btnPercent).setOnClickListener(v -> {
            if (inputTracker.length() > 0) {
                try {
                    double val = Double.parseDouble(inputTracker.toString()) / 100;
                    inputTracker.setLength(0);
                    inputTracker.append(val);
                    tvResult.setText(inputTracker.toString());
                } catch (Exception ignored) {}
            }
        });

        findViewById(R.id.btnEqual).setOnClickListener(v -> {
            if (inputTracker.length() > 0) {
                String expression = inputTracker.toString();
                tvExpression.setText(expression);
                try {
                    String cleanExpression = expression.replace("×", "*").replace("÷", "/");
                    double result = evaluate(cleanExpression);
                    
                    String resultStr = (result == (long) result) ? String.format("%d", (long) result) : String.format("%s", result);
                    tvResult.setText(resultStr);
                    inputTracker.setLength(0);
                    inputTracker.append(resultStr);
                } catch (Exception e) {
                    tvResult.setText("Error");
                }
            }
        });
    }

    private void setupButton(int id, String value) {
        findViewById(id).setOnClickListener(v -> {
            inputTracker.append(value);
            tvResult.setText(inputTracker.toString());
        });
    }

    // Custom string evaluator for strict completion
    private double evaluate(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() { ch = (++pos < str.length()) ? str.charAt(pos) : -1; }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus
                double x;
                int startPos = this.pos;
                if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }
}
