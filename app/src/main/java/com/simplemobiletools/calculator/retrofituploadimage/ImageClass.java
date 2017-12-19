package com.simplemobiletools.calculator.retrofituploadimage;

import com.google.gson.annotations.SerializedName;

/**
 * Created by haseeb on 10/6/2017.
 */

public class ImageClass {

    @SerializedName("title")
    private String Title;

    @SerializedName("image")
    private String Image;

    @SerializedName("response")
    private String Response;

    @SerializedName("Password")
    private String Password;

    @SerializedName("Email")
    private String Email;

    @SerializedName("Action")
    private int Action;

    public String getResponse()
    {
        return Response;
    }
}
