package com.example.bidanbunda_client.bottomnavigation.profile.SignOut;

import com.example.bidanbunda_client.SignIn.api.response_signin;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface signOutService {

    @POST("api/v1/auth/signout")
    Call<ResponseBody> signOut(@Header("cookie") String cookie,@Body HashMap body);

}
