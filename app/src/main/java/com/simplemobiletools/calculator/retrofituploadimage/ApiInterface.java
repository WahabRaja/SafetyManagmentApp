package com.simplemobiletools.calculator.retrofituploadimage;



import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by haseeb on 10/6/2017.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("Upload.php")
    retrofit2.Call<ImageClass> uploadImage(@Field("title") String title, @Field("image") String image, @Field("Action") int Action);


    @FormUrlEncoded
    @POST("Upload.php")
    retrofit2.Call<ImageClass> RegisterUser(@Field("Email") String email, @Field("password") String password, @Field("Action") int Action);

    @FormUrlEncoded
    @POST("Upload.php")
    retrofit2.Call<loginClass> LoginUser(@Field("Email") String email, @Field("password") String password, @Field("Action") int Action);
    //Call<ImageClass>uploadImage(@Field("title") String title, @Field("image") String image);

    @FormUrlEncoded
    @POST("Upload.php")
    retrofit2.Call<update> Update(@Field("Email") String email, @Field("password") String password, @Field("Action") int Action);
}
