package ch.hslu.mobpro.persistenz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private int resumeCount;
    private SharedPreferences sharedPreferences;
    private TextView txtCounterText;
    private TextView txtTeaPrefsSummary;
    private Button btnTeaPrefEdit;
    private Button btnSetDefaultPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCounterText = (TextView) findViewById(R.id.txtResumeText);
        txtTeaPrefsSummary = (TextView) findViewById(R.id.txtTeaPrefSummary);
        btnTeaPrefEdit = (Button) findViewById(R.id.btnEditPreferences);
        btnSetDefaultPreferences = (Button) findViewById(R.id.btnSetPreferences);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        resumeCount = sharedPreferences.getInt("CounterAppPref",0);

        btnTeaPrefEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prefIntent = new Intent(MainActivity.this, TeaPreferenceActivity.class);
                startActivity(prefIntent);
            }
        });

        btnSetDefaultPreferences.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                // fixe Werte in Preferences speichern
                // Gesüsster Tee, Süssstoff honig und Marke/Sorte ginger

                final SharedPreferences.Editor teaPrefEditor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();

                String teaPref = "Ginger from se root";
                String sweetenerPref = "Honig";

                teaPrefEditor.putString("teaPreferred", teaPref);
                teaPrefEditor.putString("teaSweetener",sweetenerPref);
                teaPrefEditor.putBoolean("teaWithSugar", true);

                String textSummaryPt = String.format(getResources().getString(R.string.teaPrefsSummary), teaPref);
                textSummaryPt =  textSummaryPt + String.format(getResources().getString(R.string.teaPrefsWithSweetenerSummary), sweetenerPref);
                txtTeaPrefsSummary.setText(textSummaryPt);
            }
        });

    }

    private void readTeaPreferences(){

        txtTeaPrefsSummary.setText("hoi");
    }

    @Override
    protected void onResume() {
        super.onResume();

        addCounterText();
        addTeaPrefsText();
    }

    private void addCounterText(){

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("CounterAppPref", resumeCount + 1);
        editor.apply();

        String text = String.format(getResources().getString(R.string.appPref_counterText), "" +resumeCount);
        txtCounterText.setText(text);

    }

    private void addTeaPrefsText(){

        final SharedPreferences tiiPref = PreferenceManager.getDefaultSharedPreferences(this);

        boolean teaWithSugar = tiiPref.getBoolean("teaWithSugar", true);
        String suesstoff = tiiPref.getString("teaSweetener","Honig");
        String lieblingstee = tiiPref.getString("teaPreferred","Ginger from se root");

        String textSummaryPt = String.format(getResources().getString(R.string.teaPrefsSummary), lieblingstee);
        if(teaWithSugar){
            textSummaryPt =  textSummaryPt + String.format(getResources().getString(R.string.teaPrefsWithSweetenerSummary), suesstoff);
        }

        txtTeaPrefsSummary.setText(textSummaryPt);
    }
}
