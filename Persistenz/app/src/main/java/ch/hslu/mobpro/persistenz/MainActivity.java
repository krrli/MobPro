package ch.hslu.mobpro.persistenz;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int resumeCount;
    private SharedPreferences preferences;
    private TextView txtCounterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCounterText = (TextView) findViewById(R.id.txtResumeText);

        preferences = getPreferences(MODE_PRIVATE);
        resumeCount = preferences.getInt("CounterAppPref",0);

    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("CounterAppPref", resumeCount + 1);
        editor.apply();

        //Resources res = getResources();
        String text = String.format(getResources().getString(R.string.appPref_counterText), "" +resumeCount);
        txtCounterText.setText(text);

    }
}
