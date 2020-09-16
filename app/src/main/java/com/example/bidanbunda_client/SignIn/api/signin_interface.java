package com.example.bidanbunda_client.SignIn.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface signin_interface {


    @POST("api/v1/auth/signin")
    Call<response_signin> signIn(@Body HashMap body);

    @GET
    Call<response_signin> profilePicture(@Url String url, @Header("cookie") String cookie);

}
