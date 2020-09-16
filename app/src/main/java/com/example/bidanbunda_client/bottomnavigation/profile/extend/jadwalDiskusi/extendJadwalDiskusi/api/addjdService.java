package com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.extendJadwalDiskusi.api;

import com.example.bidanbunda_client.SignIn.api.response_signin;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface addjdService {

    @POST("api/v1/jadwaldiskusi")
    Call<ResponseBody> postJadwalDiskusi(@Body HashMap body, @Header("cookie") String cookie);
}
