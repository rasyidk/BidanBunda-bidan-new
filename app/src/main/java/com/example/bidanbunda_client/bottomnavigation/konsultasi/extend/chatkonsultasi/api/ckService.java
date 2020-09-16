package com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.api;

import com.example.bidanbunda_client.SignIn.api.response_signin;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.models.response_ck;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ckService {

    @POST
    Call<ResponseBody> sendMessageCk(@Url String url, @Body HashMap body,@Header("cookie") String cookie);


    @GET
    Call<response_ck> getTopRatedMovies(
            @Url String url,
            @Query("page") int page,
            @Query("page_size") int pagesize,
            @Header("cookie") String cookie
    );
}
