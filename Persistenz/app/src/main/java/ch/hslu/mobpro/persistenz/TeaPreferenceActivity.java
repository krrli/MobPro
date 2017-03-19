package ch.hslu.mobpro.persistenz;

import android.app.Activity;
import android.preference.PreferenceFragment;
import android.os.Bundle;
// import android.R;

public class TeaPreferenceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_app_preference);

        if(getFragmentManager().findFragmentById(android.R.id.content) == null){
            getFragmentManager()
                    .beginTransaction()
                    //.add(android.R.id.content, new TeaPreferences())
                    .replace(android.R.id.content, new TeaPreferences())
                    .commit();
        }
    }

    public static final class TeaPreferences extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
