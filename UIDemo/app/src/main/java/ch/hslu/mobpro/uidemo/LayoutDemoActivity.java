package ch.hslu.mobpro.uidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LayoutDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String layout = getIntent().getStringExtra("LayoutType");
        if (layout.equals("Linear")) {
            setContentView(R.layout.layoutdemo_linearlayout);
        } else if (layout.equals("Relative")) {
            setContentView(R.layout.layoutdemo_relativelayout);
        }


    }
}
