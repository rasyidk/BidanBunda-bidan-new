package com.example.bidanbunda_client.SignUp.api;

import com.example.bidanbunda_client.SignIn.api.response_signin;
import com.example.bidanbunda_client.SignUp.models.response_signup;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface signup_interface {

    @POST("api/v1/auth/signup")
    Call<response_signup> signUp(@Body HashMap body);

    @GET
    Call<response_signup> getUser(@Url String url, @Header("cookie") String cookie);

    @GET
    Call<ResponseBody> cekUsername(@Url String url);

}
