package com.simplemobiletools.calculator.ActivitiesClass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.simplemobiletools.calculator.R;

/**
 * Created by haseeb on 10/24/2017.
 */

public class AboutAntiTheftlayout extends AppCompatActivity {
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);
        setTitle("Safety Management App");
        ok=(Button)findViewById(R.id.aboutOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
