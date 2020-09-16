package com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.showContact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bidanbunda_client.MainActivity;
import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.Adapter_ksl;

import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.api.ckService;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.showContact.adapter.adapter_sc;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.showContact.api.scService;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.showContact.models.data_sc;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.showContact.models.response_sc;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.models.data_ksl;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.models.response_ksl;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class showContact extends AppCompatActivity {

    private ArrayList<data_sc> data;
    private adapter_sc adapter;
    FloatingActionButton btn_addfab;
    LinearLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    RecyclerView rv;
    LinearLayout errorLayout;
    Button btnRetry;

    ImageView sc_image_back;

    String cookie, pus_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);
        getSupportActionBar().hide();


        mShimmerViewContainer = findViewById(R.id.sc_shimmer_layout);
        errorLayout = findViewById(R.id.error_layout);
        btnRetry = findViewById(R.id.error_btn_retry);
        sc_image_back = findViewById(R.id.sc_image_back);

        SharedPreferences sharedPreferences = getSharedPreferences("cookie",MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie","");
        SharedPreferences sharedPreferences2 = getSharedPreferences("profile",MODE_PRIVATE);
        pus_id = sharedPreferences2.getString("pus_id","");

        rv = findViewById(R.id.sc_recyclervww);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        rv.addItemDecoration(dividerItemDecoration);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShimmerViewContainer.startShimmer();
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                loadContactBidan();
                errorLayout.setVisibility(View.GONE);
            }
        });

        sc_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(showContact.this, MainActivity.class);
                i.putExtra("fragmentType","konsultasi");
                startActivity(i);
            }
        });

        mShimmerViewContainer.startShimmer();
        loadContactBidan();
    }

    private void loadContactBidan() {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bidan-bunda.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        //new
        scService api = retrofit.create(scService.class);

        //old
//        scService api = retrofitapi.getClient(showContact.this).create(scService.class);


        Call<response_sc> call =  api.getContactBidan("api/v1/puskesmas/"+ pus_id +"/bunda", cookie);
        call.enqueue(new Callback<response_sc>() {
            @Override
            public void onResponse(Call<response_sc> call, Response<response_sc> response) {

                response_sc jsonResponse = response.body();
                data = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
                adapter = new adapter_sc(data);
                rv.setAdapter(adapter);

                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<response_sc> call, Throwable t) {
                Log.d("ERRROR SC", t.toString());
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });
     }
}
