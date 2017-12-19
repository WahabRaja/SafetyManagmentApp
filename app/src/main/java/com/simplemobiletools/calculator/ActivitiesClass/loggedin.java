package com.simplemobiletools.calculator.ActivitiesClass;


import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.simplemobiletools.calculator.AdminReceiver;
import com.simplemobiletools.calculator.R;
import com.simplemobiletools.calculator.mMainActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hamza on 10/11/2017.
 */

public class loggedin extends AppCompatActivity implements View.OnClickListener {

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

    Button StartApp,MaskLayer,About,logout,temp;
    TextView mainText;
    Context mContext;
    SharedPreferences ss;
    SharedPreferences.Editor editor;
    Typeface tf;
    protected static final int REQUEST_CHECK_SETTINGS=0x1;
    //builtin
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loggedin);
        ss=getSharedPreferences("MyValues", Context.MODE_PRIVATE);
        editor=ss.edit();
        mContext=getApplicationContext();
        mainText=(TextView)findViewById(R.id.textView5);

        //requestPermissions();
        displayLocationSettingsRequest(loggedin.this);
        tf=Typeface.createFromAsset(getAssets(),"font/Pacifico.ttf");
        mainText.setTypeface(tf);
        setTitle("Safety Management App");
        boolean login=ss.getBoolean("Login",false);
        //Toast.makeText(this, "Logged in as "+mMainActivity.username, Toast.LENGTH_SHORT).show();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mRequestReceiver, new IntentFilter("IntentsValue"));
        if(login) {
            AlertDialog alertDialog = new AlertDialog.Builder(loggedin.this).create();
            alertDialog.setTitle("Log in ");
            alertDialog.setMessage("You are Logged in as " + ss.getString("username", ""));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }


        StartApp=(Button)findViewById(R.id.ActivateBtn);
        MaskLayer=(Button)findViewById(R.id.Mask);
        About=(Button)findViewById(R.id.TermsConditions);
        logout=(Button)findViewById(R.id.logout);
        temp=(Button)findViewById(R.id.RateUs);
        temp.setOnClickListener(this);
        logout.setOnClickListener(this);
        StartApp.setOnClickListener(this);
        About.setOnClickListener(this);
        MaskLayer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ActivateBtn:
            if(CheckConnectionStatus(getApplicationContext())) {
                DeviceManager();
                finish();
                }
                else
                {
                dialogShow();
                }
                break;
            case R.id.Mask:
                Intent intent=new Intent(loggedin.this,MaskLayout.class);
                startActivity(intent);

                break;
            case R.id.TermsConditions:
                Intent intnt=new Intent(loggedin.this,AboutAntiTheftlayout.class);
                startActivity(intnt);

                break;
            case R.id.logout:
                editor.putBoolean("Login",false);
                editor.commit();
                Intent main=new Intent(loggedin.this, mMainActivity.class);
                startActivity(main);
                finish();
                break;

        }

    }

    private BroadcastReceiver mRequestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getExtras().get("Value").toString();
            if(message.equalsIgnoreCase("success")){
                Notification();
                dialogShow();
            }/*else if(message.equalsIgnoreCase("wait")){
                Toast.makeText(context, "Searching for customer requests in the background.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }*/


        }
    };
    private void dialogShow() {
        AlertDialog alertDialog=new AlertDialog.Builder(loggedin.this).create();
        alertDialog.setTitle("Network Error");
        alertDialog.setMessage("Network error! Kindly check the Internet Connection.");
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        alertDialog.show();
    }


    private Boolean CheckConnectionStatus(Context context) {
        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        final boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        final boolean iswifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        if(iswifi)
        {
            return iswifi;
        }
        else {
    /*    WifiManager wifiManager;
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();*/

            return is3g;
        }
    }


    private void Notification()
    {

        Intent intent= new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(loggedin.this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.antitheficon)
                .setContentTitle("Safety Management App")
                .setContentText("You have new request")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

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
//////////////////////////////////////

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
                            status.startResolutionForResult(loggedin.this, REQUEST_CHECK_SETTINGS);
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


    void DeviceManager()
    {

            ComponentName cn = new ComponentName(this, AdminReceiver.class);
            DevicePolicyManager mgr =
                    (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

            if (mgr.isAdminActive(cn)) {
                int msgId;

                if (mgr.isActivePasswordSufficient()) {
                    msgId = R.string.compliant;
                } else {
                    msgId = R.string.not_compliant;
                }

                Toast.makeText(this, msgId, Toast.LENGTH_LONG).show();
            } else {
                Intent intent =
                        new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        getString(R.string.device_admin_explanation));
                startActivity(intent);
            }
           // SendMail sm = new SendMail("wahabraja@outlook.com", "ALERT from LockScreen Application(Don'tReply@this)", "Your Security service is active." );
            //sm.execute();
            finish();

    }

}


