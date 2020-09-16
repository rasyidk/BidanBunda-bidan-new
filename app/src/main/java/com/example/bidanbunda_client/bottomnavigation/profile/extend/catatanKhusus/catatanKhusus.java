package com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bidanbunda_client.MainActivity;
import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.grupchat.api.gcService;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.adapter.PaginationAdapter_catk;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.api.catkService;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.showPatient.showPatient;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.models.data_catk;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.models.response_catk;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.utils.PaginationAdapterCallback_catk;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.utils.PaginationScrollListener_catk;
import  com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.utils.PaginationAdapterCallback_catk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class catatanKhusus extends AppCompatActivity implements PaginationAdapterCallback_catk {

    private static final String TAG = "catatanKhusus";

    PaginationAdapter_catk adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;
    ImageView img_add,catk_image_back;

    Button bt_add;
    String cookie;

    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;

    private catkService movieService;


    ArrayList<data_catk> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catatan_khusus);
        getSupportActionBar().hide();

        rv = findViewById(R.id.catk_card_recycler);
        progressBar = findViewById(R.id.catk_main_progress);
        errorLayout = findViewById(R.id.error_layout);
        btnRetry = findViewById(R.id.error_btn_retry);
        txtError = findViewById(R.id.error_txt_cause);
        img_add =  findViewById(R.id.catk_img_add);
        catk_image_back = findViewById(R.id.catk_image_back);

        SharedPreferences sharedPreferences = getSharedPreferences("cookie",MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie","");


        adapter = new PaginationAdapter_catk(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener_catk(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bidan-bunda.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
//        movieService = retrofitapi.getClient(getActivity()).create(gcService.class);
        movieService = retrofit.create(catkService.class);

        //init service and load data
        //movieService = retrofitapi.getClient(this).create(catkService.class);

        loadFirstPage();

      btnRetry.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              loadFirstPage();
          }
      });

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(catatanKhusus.this, showPatient.class);
                startActivity(i);
            }
        });


        catk_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(v.getContext(), MainActivity.class);
                i.putExtra("fragmentType","profile");
                v.getContext().startActivity(i);
            }
        });

    }



    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callTopRatedMoviesApi().enqueue(new Callback<response_catk>() {
            @Override
            public void onResponse(Call<response_catk> call, Response<response_catk> response) {

                if (response.code() == 200) {


                    hideErrorView();

                    List<data_catk> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);


                    TOTAL_PAGES = response.body().getPages();


                    Log.d("TOTAL PAGES", String.valueOf(TOTAL_PAGES));

                    if (currentPage == TOTAL_PAGES) {
                        isLastPage = true;
                    } else if (currentPage <= TOTAL_PAGES) {
                        adapter.addLoadingFooter();
                    }
                }else {
                    showErrorViewstr(String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<response_catk> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private List<data_catk> fetchResults(Response<response_catk> response) {
        response_catk topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<response_catk>() {
            @Override
            public void onResponse(Call<response_catk> call, Response<response_catk> response) {


                adapter.removeLoadingFooter();
                isLoading = false;

                List<data_catk> results = fetchResults(response);
                adapter.addAll(results);

                TOTAL_PAGES = response.body().getPages();


                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<response_catk> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }


    private Call<response_catk> callTopRatedMoviesApi() {
        return movieService.getTopRatedMovies(
                currentPage,10,cookie
        );
    }


    @Override
    public void retryPageLoad() {
        loadNextPage();
    }



    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private void showErrorViewstr(String throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(throwable);
        }
    }


    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    // Helpers -------------------------------------------------------------------------------------


    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
