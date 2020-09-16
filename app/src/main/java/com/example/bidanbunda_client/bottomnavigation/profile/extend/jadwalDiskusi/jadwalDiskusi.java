package com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.grupchat.api.gcService;
import com.example.bidanbunda_client.bottomnavigation.grupchat.models.data_gc;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.api.jdService;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.models.data_jd;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.models.response_jd;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.utils.PaginationAdapterCallback_jd;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.utils.PaginationScrollListener_jd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.extendJadwalDiskusi.addJadwalDiskusi;

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

public class jadwalDiskusi extends AppCompatActivity implements PaginationAdapterCallback_jd {

    private static final String TAG = "jadwalDiskusi";

    PaginationAdapter_jd adapter;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;
    FloatingActionButton btn_fab;
    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout,null_layout;
    Button btnRetry;
    TextView txtError;
    String cookie;


    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    private jdService movieService;


    ArrayList<data_jd> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_diskusi);
        getSupportActionBar().hide();

        rv = findViewById(R.id.jdmain_recyclervww);
        progressBar = findViewById(R.id.jdmain_main_progress);
        errorLayout = findViewById(R.id.error_layout);
        btnRetry = findViewById(R.id.error_btn_retry);
        txtError = findViewById(R.id.error_txt_cause);
        btn_fab = findViewById(R.id.jdmain_fab);
        null_layout = findViewById(R.id.null_ln_layout);


        progressDialog = ProgressDialog.show(jadwalDiskusi.this, "",
                "Loading. Please wait...", true);
        progressDialog.dismiss();


        SharedPreferences sharedPreferences = getSharedPreferences("cookie",MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie","");


        adapter = new PaginationAdapter_jd(this,cookie);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener_jd(linearLayoutManager) {
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

//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .build();

        Retrofit retrofitx = new Retrofit.Builder()
                .baseUrl("https://bidan-bunda.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
                .build();
        movieService = retrofitapi.getClient(jadwalDiskusi.this).create(jdService.class);
        //
//         movieService = retrofitx.create(jdService.class);

        loadFirstPage();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFirstPage();
            }
        });

        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(jadwalDiskusi.this, addJadwalDiskusi.class);
                startActivity(i);
            }
        });


    }



    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callTopRatedMoviesApi().enqueue(new Callback<response_jd>() {
            @Override
            public void onResponse(Call<response_jd> call, Response<response_jd> response) {
                hideErrorView();

                if (response.code() == 200 || response.code() == 404){

                    List<data_jd> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);

                    if (response.body().getResults().isEmpty()){

                        isLastPage = true;
                        null_layout.setVisibility(View.VISIBLE);

                    }

                    TOTAL_PAGES = response.body().getPages();



                    Log.d("TOTAL PAGES", String.valueOf(TOTAL_PAGES));

                    if(currentPage == TOTAL_PAGES) {
                        isLastPage = true;
                    }else if (currentPage <= TOTAL_PAGES){
                        adapter.addLoadingFooter();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),response.code(), Toast.LENGTH_SHORT).show();
                    String  errr= String.valueOf(response.code());
                    showErrorViewstr(errr);
                }



            }

            @Override
            public void onFailure(Call<response_jd> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }

    /**
     * @param response extracts List<{@link data_jd >} from response
     * @return
     */
    private List<data_jd> fetchResults(Response<response_jd> response) {
        response_jd topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<response_jd>() {
            @Override
            public void onResponse(Call<response_jd> call, Response<response_jd> response) {
//                Log.i(TAG, "onResponse: " + currentPage
//                        + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                adapter.removeLoadingFooter();
                isLoading = false;

                List<data_jd> results = fetchResults(response);
                adapter.addAll(results);

                TOTAL_PAGES = response.body().getPages();


                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<response_jd> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }



    private Call<response_jd> callTopRatedMoviesApi() {
        return movieService.getTopRatedMovies(
                currentPage,10,cookie
        );
    }


    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    @Override
    public void showProgressBar() {
        progressDialog.show();
    }

    @Override
    public void dismissProgressBar() {
        progressDialog.dismiss();
    }


    /**
     * @param throwable required for {@link #fetchErrorMessage(Throwable)}
     * @return
     */
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

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
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

    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
