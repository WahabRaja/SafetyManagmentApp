package com.simplemobiletools.calculator.ActivitiesClass;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.simplemobiletools.calculator.R;
import com.simplemobiletools.calculator.retrofituploadimage.ApiClient;
import com.simplemobiletools.calculator.retrofituploadimage.ApiInterface;
import com.simplemobiletools.calculator.retrofituploadimage.ImageClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by haseeb on 10/24/2017.
 */

public class TermsandConditionlayout extends AppCompatActivity {
   Button ok;
    TextView mainTitle;
    Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions);
        setTitle("Safety Management App");
        tf=Typeface.createFromAsset(getAssets(),"font/Pacifico.ttf");
        mainTitle=(TextView)findViewById(R.id.textView20);
        mainTitle.setTypeface(tf);
        ok=(Button)findViewById(R.id.Okay);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<ImageClass> call=apiInterface.RegisterUser(Signup.email,Signup.password,2);
                call.enqueue(new Callback<ImageClass>() {
                    @Override
                    public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                        ImageClass imageClass=response.body();
                        Toast.makeText(TermsandConditionlayout.this, imageClass.getResponse(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ImageClass> call, Throwable t) {
                        Toast.makeText(TermsandConditionlayout.this, "" + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent=new Intent(TermsandConditionlayout.this,loggedin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
