package dot.empire.angleconverter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;

/**
 * Starting point of the application. This is going to be the only activity.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Conversion table.
     */
    private HashMap<String, Double> degreesConvversions;

    /**
     * Output result to user.
     */
    private TextView txfOutput;
    /**
     * User's input.
     */
    private EditText txfInput;

    /**
     * To be converted from.
     */
    private Spinner spnFrom;
    /**
     * To be converted to.
     */
    private Spinner spnTo;

    private AdView adView;

    /**
     * Default constructor.
     */
    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.degreesConvversions = new HashMap<>();
        this.degreesConvversions.put("Radians", 180 / Math.PI);
        this.degreesConvversions.put("Degrees", 1d);
        this.degreesConvversions.put("Minutes", 1 / 60d);
        this.degreesConvversions.put("Seconds", 1 / 3600d);
        this.degreesConvversions.put("Octant", 360 / 8d);
        this.degreesConvversions.put("Sextant", 360 / 6d);
        this.degreesConvversions.put("Quadrant", 360 / 4d);
        this.degreesConvversions.put("Revolution", 360 / 1d);
        this.degreesConvversions.put("Gon", 360 / 400d);
        this.degreesConvversions.put("Mil", 360 / 6400d);

        ((Button) findViewById(R.id.btnClear)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnConvert)).setOnClickListener(this);

        this.txfOutput = (TextView) findViewById(R.id.txtOutput);
        this.txfInput = (EditText) findViewById(R.id.txfInput);
        this.spnFrom = (Spinner) findViewById(R.id.spnFrom);
        this.spnTo = (Spinner) findViewById(R.id.spnTo);

        try {
            FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
            analytics.setAnalyticsCollectionEnabled(true);

            MobileAds.initialize(this,"ca-app-pub-7186830154970754~4506385135");

            this.adView = (AdView) findViewById(R.id.adView);

            AdRequest.Builder builder = new AdRequest.Builder();
            this.adView.loadAd(builder.build());
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }


        Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConvert: {
                try {
                    double deg = Double.parseDouble(txfInput.getText().toString()) // convert to degrees
                            * degreesConvversions.get(spnFrom.getSelectedItem().toString());
                    double value = deg / degreesConvversions.get(spnTo.getSelectedItem().toString());

                    this.txfOutput.setText(String.format(Math.round(value) == value ? "%.0f" : "%.3f", (float) value)
                            .replaceAll(",", "."));
                } catch (Exception ex) {
                    Log.e("Error", "Invalid input", ex);

                    Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(this, ex.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

                    // String msg = ex.getMessage().toLowerCase().trim();
                    // Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    // this.txfOutput.setText(msg);

                    this.txfOutput.setText("Invalid Input");
                }
            }
            return;
            case R.id.btnClear: {
                this.txfInput.setText("");
                this.txfOutput.setText("");
            }
            return;
        }
    }
}
