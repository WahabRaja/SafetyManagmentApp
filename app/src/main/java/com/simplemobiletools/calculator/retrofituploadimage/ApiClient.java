package com.simplemobiletools.calculator.retrofituploadimage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haseeb on 10/6/2017.
 */

public class ApiClient {

    private  static final String BaseUri="http://www.dibukhanmathematician.com/UploadImages1/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient()
    {
        if(retrofit==null)
        {
            retrofit= new Retrofit.Builder().baseUrl(BaseUri).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
            return retrofit;
    }
}
