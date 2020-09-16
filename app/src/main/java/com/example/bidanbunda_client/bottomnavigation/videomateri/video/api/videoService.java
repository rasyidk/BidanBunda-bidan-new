package com.example.bidanbunda_client.bottomnavigation.videomateri.video.api;


import com.example.bidanbunda_client.bottomnavigation.videomateri.video.models.response_video;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface videoService {

    @GET("api/v1/videos")
    Call<response_video> getTopRatedMovies(
            @Query("page") int page,
            @Query("page_size") int pagesize,
            @Query("week") int week,
            @Header("cookie") String cookie
    );

}
