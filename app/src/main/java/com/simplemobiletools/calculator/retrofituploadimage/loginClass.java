package com.simplemobiletools.calculator.retrofituploadimage;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by haseeb on 10/6/2017.
 */

public class loginClass {

    @SerializedName("UserInfo")
    private ArrayList<loginClassDetail> Response;

    public ArrayList<loginClassDetail> getResponse()
    {
        return Response;
    }
}
