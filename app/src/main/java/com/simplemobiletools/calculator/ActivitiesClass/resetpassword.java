package com.simplemobiletools.calculator.ActivitiesClass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.simplemobiletools.calculator.R;


/**
 * Created by Hamza on 10/12/2017.
 */

public class resetpassword extends AppCompatActivity implements View.OnClickListener {
    EditText RecentPassword,NewPassword,NewRePassword;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword);
        setTitle("Safety Management App");
        RecentPassword=(EditText)findViewById(R.id.editText);
        NewPassword=(EditText)findViewById(R.id.editText2);
        NewRePassword=(EditText)findViewById(R.id.editText3);
        save=(Button)findViewById(R.id.saveforpass);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RecentPassword.getText()!=null&&NewPassword.getText()!=null&&NewRePassword.getText()!=null)
                {

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        //Password change

        Intent intent=new Intent(resetpassword.this,loggedin.class);
        startActivity(intent);

    }
}
