package com.simplemobiletools.calculator.ActivitiesClass;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.simplemobiletools.calculator.R;
import com.simplemobiletools.calculator.emailverification.SendMail;
import com.simplemobiletools.calculator.mMainActivity;
import com.simplemobiletools.calculator.retrofituploadimage.ApiClient;
import com.simplemobiletools.calculator.retrofituploadimage.ApiInterface;
import com.simplemobiletools.calculator.retrofituploadimage.update;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hamza on 10/12/2017.
 */

public class forgetpassword extends AppCompatActivity {

    static int MAX_LENGTH;
    Button proceed,Save;
    String verify;
    TextView mainText;
    Typeface tf;
    EditText Email,Password,ReEntrPassword,Verification;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);
        setTitle("Safety Management App");
        MAX_LENGTH=5;
        tf=Typeface.createFromAsset(getAssets(),"font/Pacifico.ttf");;
        mainText=(TextView)findViewById(R.id.textView3);
        mainText.setTypeface(tf);
        proceed=(Button)findViewById(R.id.sndcode);
        Email=(EditText)findViewById(R.id.foremailid);
        Password=(EditText)findViewById(R.id.newpass);
        ReEntrPassword=(EditText)findViewById(R.id.repass);
        Verification=(EditText)findViewById(R.id.vericode) ;
        Save=(Button)findViewById(R.id.saveforpass);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Verification.getText().toString().equals(verify))
                {
                    if(Password.getText().toString().equals(ReEntrPassword.getText().toString()))
                    {
                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<update> call = apiInterface.Update(Email.getText().toString(), Password.getText().toString(), 4);
                        call.enqueue(new Callback<update>() {
                            @Override
                            public void onResponse(Call<update> call, Response<update> response) {
                                update imageclass=response.body();
                                Toast.makeText(forgetpassword.this,imageclass.getResponse(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<update> call, Throwable t) {
                                Toast.makeText(forgetpassword.this, "Check Network Connection please\n   " + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent intent=new Intent(forgetpassword.this, mMainActivity.class);
                    startActivity(intent);
                        finish();
                    }
                }
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Email.getText()!=null)
                {
                    SendMail sm = new SendMail(Email.getText().toString(), "Verification Code", "Your verification Code is:" +getSaltString());
                    sm.execute();
                    Password.setEnabled(true);
                    ReEntrPassword.setEnabled(true);
                    Verification.setEnabled(true);
                    Save.setEnabled(true);

                }
            }
        });

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
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
