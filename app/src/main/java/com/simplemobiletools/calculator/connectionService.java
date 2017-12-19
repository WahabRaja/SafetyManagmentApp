package com.simplemobiletools.calculator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by haseeb on 12/11/2017.
 */

public class connectionService extends Service {
    private WifiManager wifiManager;
    Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // CheckConnectionStatus();
                handler.postDelayed(runnable, 0);

            }
        },20000);

        //return START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
    public Runnable runnable = new Runnable() {

        public void run() {


            CheckConnectionStatus();
            handler.postDelayed(this, 60000);
        }

    };
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

        private void CheckConnectionStatus() {
            final ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            final boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .isConnectedOrConnecting();
            final boolean iswifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .isConnectedOrConnecting();
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if(wifi.isConnected()||is3g)
            {
                Intent intent = new Intent("intentKey");
                // You can also include some extra data.
                intent.putExtra("key","3gActive");
                LocalBroadcastManager.getInstance(connectionService.this).sendBroadcast(intent);
               // CheckConnectionStatus();
                //wifiManager.setWifiEnabled(false);

            }
            else if(!is3g||!iswifi)
            {
                Intent intent = new Intent("intentKey");
                // You can also include some extra data.
                intent.putExtra("key","3g");
                LocalBroadcastManager.getInstance(connectionService.this).sendBroadcast(intent);
                //Toast.makeText(connectionService.this, "3g is disabled", Toast.LENGTH_SHORT).show();
                //setMobileDataEnabled(getApplicationContext(),true);
            }
        }

}
