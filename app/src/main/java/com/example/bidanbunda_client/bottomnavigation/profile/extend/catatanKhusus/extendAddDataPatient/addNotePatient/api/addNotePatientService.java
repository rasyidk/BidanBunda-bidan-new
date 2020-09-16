package com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.addNotePatient.api;

import com.example.bidanbunda_client.SignIn.api.response_signin;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface addNotePatientService {

    @POST
    Call<ResponseBody> addNote(@Url String url, @Body HashMap body, @Header("cookie") String cookie);

    @PUT
    Call<ResponseBody> editNote(@Url String url, @Body HashMap body, @Header("cookie") String cookie);
}
