package ch.hslu.mobpro.persistenz;

import android.app.Activity;
import android.preference.PreferenceFragment;
import android.os.Bundle;
// import android.R;

public class TeaPreferenceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getFragmentManager().findFragmentById(android.R.id.content) == null){
            getFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new TeaPreferences())
                    .commit();
        }
    }

    public static final class TeaPreferences extends PreferenceFragment{

        public TeaPreferences(){

        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
