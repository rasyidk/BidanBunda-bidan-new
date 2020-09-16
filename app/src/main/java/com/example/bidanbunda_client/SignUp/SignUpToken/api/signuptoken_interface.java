package com.example.bidanbunda_client.SignUp.SignUpToken.api;

import com.example.bidanbunda_client.SignUp.SignUpToken.models.response_signuptoken;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface signuptoken_interface {


    @POST("api/v1/auth/signup")
    Call<response_signuptoken> signUp(@Body HashMap body);



//    @GET("api/v1/puskesmas")
//    Call<ResponseBody> cekToken(
//            @Query("token") String token);

    @GET
    Call<JsonObject> cekToken(@Url String url);

    @GET
    Call<response_signuptoken> getDataUser(@Url String url, @Header("cookie") String cookie);
}
