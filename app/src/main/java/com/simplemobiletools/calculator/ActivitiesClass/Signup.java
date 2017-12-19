package com.simplemobiletools.calculator.ActivitiesClass;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.simplemobiletools.calculator.R;
import com.simplemobiletools.calculator.emailverification.SendMail;

import java.util.Random;

/**
 * Created by Hamza on 10/11/2017.
 */

public class Signup extends AppCompatActivity implements View.OnClickListener {
    Button proceed, signup;
    EditText Email, Password, RePassword,code;
    String verify;
    TextView main1,Main2;
    Typeface tf;
public static String email,password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        setTitle("Safety Management App");
        proceed = (Button) findViewById(R.id.proceedBtn);
        signup = (Button) findViewById(R.id.btnsignup1);
        main1=(TextView)findViewById(R.id.textView13);
        tf= Typeface.createFromAsset(getAssets(),"font/Pacifico.ttf");
        Main2=(TextView)findViewById(R.id.signupview);
        main1.setTypeface(tf);
        Main2.setTypeface(tf);
        Email = (EditText) findViewById(R.id.etemail);
        Password = (EditText) findViewById(R.id.pass);
        RePassword = (EditText) findViewById(R.id.confpass);
        code=(EditText)findViewById(R.id.etcode);
        proceed.setOnClickListener(this);
        signup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceedBtn:
                if (Email.getText() != null && Password.getText() != null && RePassword.getText() != null) {
                    if (Password.getText().toString().equals(RePassword.getText().toString())) {
                        email=Email.getText().toString();
                        password=Password.getText().toString();
                        SendMail sm = new SendMail(Email.getText().toString(), "Verification Code", "Your verification Code is:" +getSaltString());
                        sm.execute();
                    }
                    signup.setEnabled(true);
                    code.setEnabled(true);
                }
                break;
            case R.id.btnsignup1:
                if(verify.equals(code.getText().toString()))
                {
                    Toast.makeText(this, "hello in singup", Toast.LENGTH_SHORT).show();
                    Log.d("code: ","Code is "+verify);
                    Intent intent =new Intent(Signup.this,TermsandConditionlayout.class);
                    startActivity(intent);
                    /*ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<ImageClass> call=apiInterface.RegisterUser(Email.getText().toString(),Password.getText().toString(),2);
                    call.enqueue(new Callback<ImageClass>() {
                        @Override
                        public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                            ImageClass imageClass=response.body();
                            Toast.makeText(Signup.this, imageClass.getResponse(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ImageClass> call, Throwable t) {
                            Toast.makeText(Signup.this, "" + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });*/

                }
                else
                {
                    Toast.makeText(this, "Your code in not Correct", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 7) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        verify=saltStr;
        return saltStr;

    }
}
