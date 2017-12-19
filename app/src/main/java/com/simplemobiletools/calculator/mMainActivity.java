package com.simplemobiletools.calculator;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.simplemobiletools.calculator.ActivitiesClass.Signup;
import com.simplemobiletools.calculator.ActivitiesClass.forgetpassword;
import com.simplemobiletools.calculator.ActivitiesClass.loggedin;
import com.simplemobiletools.calculator.ActivitiesClass.resetpassword;
import com.simplemobiletools.calculator.retrofituploadimage.ApiClient;
import com.simplemobiletools.calculator.retrofituploadimage.ApiInterface;
import com.simplemobiletools.calculator.retrofituploadimage.loginClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import android.support.v4.app.ActivityCompat;

public class mMainActivity extends AppCompatActivity implements View.OnClickListener{
    //Marshmallow Permissions
    private static final String[] SMS_PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.CHANGE_WIFI_STATE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    Typeface tf;
    Button SignINBtn,SignUPBtn;
    TextView ForgetPassword,ResetPassword,titleText;
    EditText Usrname,Password;
    public static String userid;
    Context mcontext;
    SharedPreferences ss;
    SharedPreferences.Editor editor;
   public static String username,password;
    protected static final int REQUEST_CHECK_SETTINGS=0x1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mactivity_main);
        tf=Typeface.createFromAsset(getAssets(),"font/Pacifico.ttf");

        requestPermissions();
       // displayLocationSettingsRequest(mMainActivity.this);
        SignINBtn=(Button)findViewById(R.id.btnsignin);
        SignUPBtn=(Button)findViewById(R.id.btnsignup);
        ForgetPassword=(TextView)findViewById(R.id.forgetpass);
        ResetPassword=(TextView)findViewById(R.id.resetpass);
        Usrname=(EditText)findViewById(R.id.logemail);
        Password=(EditText)findViewById(R.id.pass1);
        titleText=(TextView)findViewById(R.id.textView4);
        titleText.setTypeface(tf);
        ss=getSharedPreferences("MyValues",Context.MODE_PRIVATE);
        editor=ss.edit();
        setTitle("Safety Management App");
        SignINBtn.setOnClickListener(this);
        SignUPBtn.setOnClickListener(this);
        ForgetPassword.setOnClickListener(this);
        ResetPassword.setOnClickListener(this);
        //SignINBtn.setOnClickListener(this);
        //Usrname=(EditText)findViewById(R.id.etname);
        //Password=(EditText)findViewById(R.id.etpassword);

        boolean login=ss.getBoolean("Login",false);
        if(login)
        {
            Intent ActualApp= new Intent(mMainActivity.this,loggedin.class );
            startActivity(ActualApp);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        username=Usrname.getText().toString();
        password=Password.getText().toString();


        // Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, password, Toast.LENGTH_SHORT).show();
        switch(v.getId()) {

            //sign In
            case R.id.btnsignin:
                final Boolean login=false;
                if(username!=null&&password!=null) {
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<loginClass> call = apiInterface.LoginUser(Usrname.getText().toString(), Password.getText().toString(), 1);
                    call.enqueue(new Callback<loginClass>() {
                        @Override
                        public void onResponse(Call<loginClass> call, Response<loginClass> response) {
                           // loginClass imageclass=response.body();
                            loginClass res=response.body();
                            editor.putString("username",Usrname.getText().toString());
                            editor.commit();
                            //Log.d("Login Detail","login: "+res.getResponse().get(1));
                           /* userid=res.getResponse().get(1).id;
                            Toast.makeText(mMainActivity.this, "Id: "+res.getResponse().get(1).id, Toast.LENGTH_SHORT).show();*/
                            //get id for Authenfication of user to send user detail in GetUserResponse class;

                            Toast.makeText(mMainActivity.this,"Successfully Log in", Toast.LENGTH_SHORT).show();

                            username=Usrname.getText().toString();
                            AlertDialog alertDialog=new AlertDialog.Builder(mMainActivity.this).create();
                            alertDialog.setTitle("Log in");
                            alertDialog.setMessage("Remember this Account? ");
                            alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.putBoolean("Login",true);
                                            editor.commit();
                                            dialog.dismiss();
                                            Intent SignIN = new Intent(mMainActivity.this, loggedin.class);
                                            startActivity(SignIN);
                                            finish();
                                        }
                                    });
                            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "No",
                                    new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                    dialog.dismiss();
                                    Intent SignIN = new Intent(mMainActivity.this, loggedin.class);
                                    startActivity(SignIN);
                                    finish();
                                }

                            });

                            alertDialog.show();
                        }

                        @Override
                        public void onFailure(Call<loginClass> call, Throwable t) {
                            Toast.makeText(mMainActivity.this, "Check Network Connection please\n   " + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                  /*  if (login){
                        Intent SignIN = new Intent(mMainActivity.this, loggedin.class);
                        startActivity(SignIN);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                    }*/
                }
                else
                {
                    Toast.makeText(mMainActivity.this, "Insert Email and Password", Toast.LENGTH_SHORT).show();
                }
                break;
            //Sign Up Activity
            case R.id.btnsignup:
                Intent SignUP= new Intent(mMainActivity.this, Signup.class);
                startActivity(SignUP);
                break;
            //New Register
            case R.id.btnsignup1:

                break;
            //ForgetPassword
            case R.id.forgetpass:
                Intent forget = new Intent(mMainActivity.this, forgetpassword.class);
                startActivity(forget);
                break;
            //ResetPassword
            case R.id.resetpass:
                Intent Reset = new Intent(mMainActivity.this, resetpassword.class);
                startActivity(Reset);
                break;
        }


    }
    private void SignIn()
    {

    }

    private void requestPermissions() {
        List<String> missingPermissions;
        String methodText;


        missingPermissions = getMissingPermissions(SMS_PERMISSIONS);



        if (missingPermissions.isEmpty()) {



        } else {
            if (needPermissionsRationale(missingPermissions)) {
                Toast.makeText(this, "This application needs your permission", Toast.LENGTH_LONG)
                        .show();
            }
            ActivityCompat.requestPermissions(this,
                    missingPermissions.toArray(new String[missingPermissions.size()]),
                    0);
        }
    }




/////////////////////////////////////////////////////////////////


    private boolean needPermissionsRationale(List<String> permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // Proceed with verification after requesting permissions.
        // If the verification SDK fails to intercept the code automatically due to missing permissions,
        // the VerificationListener.onVerificationFailed(1) method will be executed with an instance of
        // CodeInterceptionException. In this case it is still possible to proceed with verification
        // by asking the user to enter the code manually.
        // createVerification();

    }

    private List<String> getMissingPermissions(String[] requiredPermissions) {
        List<String> missingPermissions = new ArrayList<>();
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
                Log.d("permission", "getMissingPermissions: "+missingPermissions);
            }
        }
        return missingPermissions;
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override

            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(mMainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                         //   Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                       // Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }



}

