package com.example.bidanbunda_client.bottomnavigation.videomateri.api;


import com.example.bidanbunda_client.bottomnavigation.videomateri.models.response_vm;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Pagination
 * Created by Suleiman19 on 10/27/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */

public interface vmService {

    @GET("api/v1/videomateri")
    Call<response_vm> getTopRatedMovies(
            @Query("page") int pageIndex,
            @Query("page_size") int pageSize,
            @Header("cookie") String cookie
    );

}
