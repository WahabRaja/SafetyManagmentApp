package com.simplemobiletools.calculator.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.simplemobiletools.calculator.Config;
import com.simplemobiletools.calculator.R;

public class SettingsActivity extends SimpleActivity {
    //@BindView(R.id.settings_dark_theme) SwitchCompat mDarkThemeSwitch;

    private static Config mConfig;
    Button sbmitpin;
    String oldPinString,newPinString;
    SharedPreferences ss;
    SharedPreferences.Editor editor;
    EditText oldtext,newtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sbmitpin=(Button)findViewById(R.id.PinSubmitBtn);
        oldtext=(EditText)findViewById(R.id.OldPin);
        newtext=(EditText)findViewById(R.id.NewPin);
        ss=getSharedPreferences("MyValues",Context.MODE_PRIVATE);
        editor=ss.edit();
        mConfig = Config.newInstance(getApplicationContext());
//        ButterKnife.bind(this);
       // setupDarkTheme();
        sbmitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                oldPinString=ss.getString("PinSet","1256");
                if(oldtext.getText().toString().equals(oldPinString))
                {
                    editor.putString("PinSet",newtext.getText().toString());
                    editor.commit();
                    Toast.makeText(SettingsActivity.this, "new Text is "+newtext.getText().toString(), Toast.LENGTH_SHORT).show();
                    oldtext.setText(null);
                    newtext.setText(null);
                }
            }
        });
    }

  /*  private void setupDarkTheme() {
        mDarkThemeSwitch.setChecked(mConfig.getIsDarkTheme());
    }

    @OnClick(R.id.settings_dark_theme_holder)
    public void handleDarkTheme() {
        mDarkThemeSwitch.setChecked(!mDarkThemeSwitch.isChecked());
        mConfig.setIsDarkTheme(mDarkThemeSwitch.isChecked());
        restartActivity();
    }*/

    private void restartActivity() {
        TaskStackBuilder.create(getApplicationContext()).addNextIntentWithParentStack(getIntent()).startActivities();
    }
}
