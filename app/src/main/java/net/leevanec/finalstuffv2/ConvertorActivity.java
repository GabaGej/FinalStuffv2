package net.leevanec.finalstuffv2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ConvertorActivity extends AppCompatActivity {

    private EditText inputEditText;
    private Spinner spinnerboa;
    private Spinner spinnerosso;

    private TextView vystup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "bbbbbb", 0).show();
        super.onCreate(savedInstanceState);


        // Connect stuff
        setContentView(R.layout.activity_convertor);
        inputEditText = findViewById(R.id.vstupjedna);
        spinnerboa = findViewById(R.id.spinboa);
        spinnerosso = findViewById(R.id.spinnerosso);
        vystup = findViewById(R.id.vystup);
    }

    public void onButtonClick(View view) {
        // Fetch Input
        double input = Double.parseDouble(inputEditText.getText().toString());
        String inone = spinnerboa.getSelectedItem().toString();
        String intwo = spinnerosso.getSelectedItem().toString();

        Toast.makeText(ConvertorActivity.this, inone,Toast.LENGTH_SHORT).show();
        Toast.makeText(ConvertorActivity.this, intwo,Toast.LENGTH_SHORT).show();
        // Find out the relationships

        double output = input * relations(inone, intwo);
        String outtext;
        if(output == 0){
            outtext = "There is no defined relation for this conversion, or the output is equal to input.";
        }else{
            outtext = output+" "+intwo;
        }

        vystup.setText(outtext);

    }

    private double relations(String spinone, String spintwo){
        String opcode = spinone + spintwo;
        double result = 0;

        switch (opcode){
            case "MetersKilometers":
                result = 0.001;
                break;

            case "MetersMiles":
                result = 0.000621371192;
                break;

            case "KilometersMeters":
                result = 1000;
                break;

            case "KilometersMiles":
                result = 0.621371192;
                break;

            case "MilesMeters":
                result = 1609.344;
                break;

            case "MilesKilometers":
                result = 1.609344;
                break;

            case "kmphmilph":
                result = 0.621371192;
                break;

            case "kmphmps":
                result = 0.2778;
                break;

            case "milphkmph":
                result = 1.609344;
                break;

            case "milphmps":
                result = 0.44704;
                break;

            case "GramsKilograms":
                result = 0.001;
                break;

            case "GramsPounds":
                result = 0.00220462262;
                break;

            case "GramsOunces":
                result = 0.0352739619;
                break;
            case "KilogramsGrams":
                result = 1000;
                break;

            case "KilogramsPounds":
                result = 2.20462262;
                break;

            case "KilogramsOunces":
                result = 35.2739619;
                break;

            case "PoundsGrams":
                result = 453.59237;
                break;

            case "PoundsKilograms":
                result = 0.45359237;
                break;

            case "PoundsOunces":
                result = 16;
                break;

            case "OuncesGrams":
                result = 28.3495231;
                break;

            case "OuncesKilograms":
                result = 0.0283495231;
                break;

            case "OuncesPounds":
                result = 0.0625;
                break;

            default:
                result = 0;
                break;
        }

        Toast.makeText(ConvertorActivity.this, opcode,Toast.LENGTH_SHORT).show();

        return (result);
    }

}
