package net.leevanec.finalstuffv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
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

    //Start defining stuff

    private TextView displayTextView;
    private boolean isResultDisplayed = false;
    private List<Double> numberHistory;
    private ArrayAdapter<Double> numberHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // This thing for whatever reason refuses to work
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Connect the required stuff

        displayTextView = findViewById(R.id.displayTextView);
        numberHistory = new ArrayList<>();

        // Load number history from SharedPreferences

        loadNumberHistory();

        // The history needs to go to the ListView by an Array Adapter

        ListView historyListView = findViewById(R.id.historyListView);
        numberHistoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numberHistory);
        historyListView.setAdapter(numberHistoryAdapter);
    }

    // Button handler

    public void onButtonClick(View view) {

        // Which button exactly was pressed?
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        String currentText = displayTextView.getText().toString();

        // If it was =, then the displayed value will be the result
        // but otherwise it has to be the current input - this
        // makes sure of it

        if (isResultDisplayed) {
            if (!buttonText.equals("=")) {
                displayTextView.setText("");
                isResultDisplayed = false;
            }
        }

        // What to do if the button is =:

        if (buttonText.equals("=")) {

            // Fetch input and perform the calculation

            try {
                double result = ExpressionEvaluator.evaluateExpression(currentText);
                displayTextView.setText(String.valueOf(result));
                saveDisplayedNumber(result); // Save the result
                isResultDisplayed = true;
            } catch (ArithmeticException | IllegalArgumentException e) {

                // If its fucked...
                displayTextView.setText("Error");
            }
        }

        // The clear input button (C)
        else if (buttonText.equals("C")) {
            displayTextView.setText("");
            isResultDisplayed = false;
        }

        // Load the last result button (M)
        else if (buttonText.equals("M")) {
            moveNumberToExpression();
        }

        // And for anything else, just add it to the input accumulator
        else {
            displayTextView.append(buttonText);
        }
    }

    // Saves the result to the ArrayList thingy
    private void saveDisplayedNumber(double displayedNumber) {
        numberHistory.add(0, displayedNumber);    // Add to the beginning of the list
        numberHistoryAdapter.notifyDataSetChanged();    // Update the adapter to reflect the change
        saveNumberHistory();                            // Save number history to SharedPreferences
    }

    // The actual return-last-thing-to-expression-button code
    private void moveNumberToExpression() {
        if (!numberHistory.isEmpty()) {
            double lastSavedNumber = numberHistory.get(0); // Get the newest number
            displayTextView.append(String.valueOf(lastSavedNumber));
        } else {
            Toast.makeText(this, "Number history is empty", Toast.LENGTH_SHORT).show();
        }
    }

    // Load saved values from previous app runs
    private void loadNumberHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("NumberHistory", Context.MODE_PRIVATE);
        Set<String> stringNumberHistory = sharedPreferences.getStringSet("history", new HashSet<>());
        for (String numberString : stringNumberHistory) {
            numberHistory.add(Double.parseDouble(numberString));
        }
    }

    // Save more values to the memory as you compute!
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

    // Override onBackPressed to prevent the activity from committing die when the "back button" is pressed
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}