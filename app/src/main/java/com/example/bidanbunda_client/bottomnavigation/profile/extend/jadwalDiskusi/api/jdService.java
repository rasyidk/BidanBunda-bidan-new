package com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.api;


import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.models.response_jd;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface jdService {

    @GET("/api/v1/jadwaldiskusi")
    Call<response_jd> getTopRatedMovies(
            @Query("page") int page,
            @Query("page_size") int pagesize,
            @Header("cookie") String cookie
    );

    @DELETE
    Call<ResponseBody> deleteJadwal(
            @Url String url,
            @Header("cookie") String cookie
    );

}
