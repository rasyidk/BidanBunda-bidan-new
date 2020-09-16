package com.example.bidanbunda_client.bottomnavigation.profile.extend.vaksin.api;



import com.example.bidanbunda_client.bottomnavigation.profile.extend.vaksin.models.response_vaksin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface vaksinService {

    @GET("/api/v1/vaccines")
    Call<response_vaksin> getTopRatedMovies(
            @Query("page") int page,
            @Query("page_size") int pagesize,
            @Header("cookie") String cookie
    );

}
