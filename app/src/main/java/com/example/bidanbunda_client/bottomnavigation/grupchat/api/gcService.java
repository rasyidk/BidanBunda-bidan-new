package com.example.bidanbunda_client.bottomnavigation.grupchat.api;



import com.example.bidanbunda_client.bottomnavigation.grupchat.models.response_gc;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;



public interface gcService {

    @GET("api/v1/chatgroups")
    Call<response_gc> getTopRatedMovies(
            @Query("page") int pageIndex,
            @Query("page_size") int pageSize,
            @Header("cookie") String cookie
    );

    @POST("api/v1/chatgroups")
    Call<ResponseBody> sendMessageToServer(
            @Body HashMap message,
            @Header("cookie") String cookie);

}
