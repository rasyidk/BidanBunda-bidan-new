package com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.showPatient.api;




import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.showPatient.models.response_sp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface spService {

    @GET
    Call<response_sp> getTopRatedMovies(
            @Url String url,
            @Query("page") int page,
            @Query("page_size") int pagesize,
            @Header("cookie") String cookie

    );

}
