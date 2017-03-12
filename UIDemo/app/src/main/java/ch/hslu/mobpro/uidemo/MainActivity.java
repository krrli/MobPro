package ch.hslu.mobpro.uidemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioButton rbLinear = (RadioButton) this.findViewById(R.id.rbLinearLayout);
        RadioButton rbRelative = (RadioButton) this.findViewById(R.id.rbRelativeLayout);

        rbLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                openLayoutDemo(true);
            }
        }) ;

        rbRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                openLayoutDemo(false);
            }
        }) ;

    }

    /**
     * Open Layout Demo Activity
     *
     * @param whichOne true: open Linear View, false: open Relative View
     */
    public void openLayoutDemo(boolean whichOne) {

        Intent intentLayoutDemo = new Intent(this, LayoutDemoActivity.class);

        if (whichOne) {
            // open Linear View
            intentLayoutDemo.putExtra("LayoutType", "Linear");
        } else {
            // open Relative View
            intentLayoutDemo.putExtra("LayoutType", "Relative");
        }

        startActivity(intentLayoutDemo);
    }
}
