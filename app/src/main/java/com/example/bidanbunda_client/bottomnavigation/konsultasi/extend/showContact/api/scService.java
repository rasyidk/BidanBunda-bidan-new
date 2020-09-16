package com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.showContact.api;


import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.showContact.models.response_sc;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

public interface scService {

    @GET
    Call<response_sc> getContactBidan(@Url String url, @Header("cookie") String cookie);

}
