package com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.api;


import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.models.response_catk;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface catkService {

    @GET("/api/v1/patients")
    Call<response_catk> getTopRatedMovies(
            @Query("page") int page,
            @Query("page_size") int pagesize,
            @Header("cookie") String cookie
    );

}
