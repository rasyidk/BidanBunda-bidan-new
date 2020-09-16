package com.example.bidanbunda_client.bottomnavigation.notification.api;



import com.example.bidanbunda_client.bottomnavigation.notification.models.response_notif;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;




public interface notifService {

    @GET("api/v1/notifications")
    Call<response_notif> getTopRatedMovies(
            @Query("page") int pageIndex,
            @Query("page_size") int pageSize,
            @Header("cookie") String cookie
    );

}
