package net.leevanec.finalstuffv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalculatorActivity extends AppCompatActivity {

    private TextView displayTextView;
    private boolean isResultDisplayed = false;
    private List<Double> numberHistory;
    private ArrayAdapter<Double> numberHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        displayTextView = findViewById(R.id.displayTextView);
        numberHistory = new ArrayList<>();

        // Load number history from SharedPreferences
        loadNumberHistory();

        ListView historyListView = findViewById(R.id.historyListView);
        numberHistoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numberHistory);
        historyListView.setAdapter(numberHistoryAdapter);
    }

    public void onButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        String currentText = displayTextView.getText().toString();

        if (isResultDisplayed) {
            if (!buttonText.equals("=")) {
                displayTextView.setText("");
                isResultDisplayed = false;
            }
        }

        if (buttonText.equals("=")) {
            // Perform calculation
            try {
                double result = ExpressionEvaluator.evaluateExpression(currentText);
                displayTextView.setText(String.valueOf(result));
                saveDisplayedNumber(result); // Save the result
                isResultDisplayed = true;
            } catch (ArithmeticException | IllegalArgumentException e) {
                displayTextView.setText("Error");
            }
        } else if (buttonText.equals("C")) {
            // Clear display
            displayTextView.setText("");
            isResultDisplayed = false;
        } else if (buttonText.equals("M")) {
            // Move the last saved number back to the expression accumulator
            moveNumberToExpression();
        } else {
            // Append button text to display
            displayTextView.append(buttonText);
        }
    }

    // Function to save the displayed number into the list and SharedPreferences
    private void saveDisplayedNumber(double displayedNumber) {
        numberHistory.add(0, displayedNumber); // Add to the beginning of the list
        numberHistoryAdapter.notifyDataSetChanged(); // Update the adapter to reflect the change
        saveNumberHistory(); // Save number history to SharedPreferences
    }

    // Function to move the last saved number back to the expression accumulator
    private void moveNumberToExpression() {
        if (!numberHistory.isEmpty()) {
            double lastSavedNumber = numberHistory.get(0); // Get the newest number
            displayTextView.append(String.valueOf(lastSavedNumber));
        } else {
            Toast.makeText(this, "Number history is empty", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to load number history from SharedPreferences
    private void loadNumberHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("NumberHistory", Context.MODE_PRIVATE);
        Set<String> stringNumberHistory = sharedPreferences.getStringSet("history", new HashSet<>());
        for (String numberString : stringNumberHistory) {
            numberHistory.add(Double.parseDouble(numberString));
        }
    }

    // Function to save number history to SharedPreferences
    private void saveNumberHistory() {
        Set<String> stringNumberHistory = new HashSet<>();
        for (Double number : numberHistory) {
            stringNumberHistory.add(String.valueOf(number));
        }
        SharedPreferences sharedPreferences = getSharedPreferences("NumberHistory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("history", stringNumberHistory);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        // Override onBackPressed to prevent the activity from being destroyed when the back button is pressed
        moveTaskToBack(true);
    }
}