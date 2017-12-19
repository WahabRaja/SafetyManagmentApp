package com.simplemobiletools.calculator.ActivitiesClass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.simplemobiletools.calculator.R;
import com.simplemobiletools.calculator.activities.MainActivity;

/**
 * Created by haseeb on 10/24/2017.
 */

public class MaskLayout extends AppCompatActivity {
   Button Cal,App;
    TextView maintext;
    Typeface tf;
    Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mask_layout);
        setTitle("Safety Management App");
        maintext=(TextView)findViewById(R.id.textView18);
        Cal=(Button)findViewById(R.id.btncalc);
        App=(Button)findViewById(R.id.btnanti);
        tf=Typeface.createFromAsset(getAssets(),"font/Pacifico.ttf");
        maintext.setTypeface(tf);

        Cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=getSharedPreferences("MaskView", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("MaskValue",1);
                editor.commit();

                Intent intent=new Intent(MaskLayout.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        App.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            SharedPreferences sharedPreferences=getSharedPreferences("MaskView",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("MaskValue",2);
                editor.commit();
                Intent intent =new Intent(MaskLayout.this,loggedin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
