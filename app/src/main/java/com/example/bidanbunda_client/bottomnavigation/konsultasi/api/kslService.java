package com.example.bidanbunda_client.bottomnavigation.konsultasi.api;



import com.example.bidanbunda_client.bottomnavigation.konsultasi.models.response_ksl;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface kslService {

    @GET
    Call<response_ksl> getTopRatedMovies(@Url String url,
            @Header("cookie") String cookie
    );

}
