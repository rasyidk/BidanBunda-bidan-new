package com.example.bidanbunda_client.bottomnavigation.videomateri.video;

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


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.videomateri.video.api.videoService;
import com.example.bidanbunda_client.bottomnavigation.videomateri.video.models.data_video;
import com.example.bidanbunda_client.bottomnavigation.videomateri.video.models.response_video;
import com.example.bidanbunda_client.bottomnavigation.videomateri.video.utils.PaginationAdapterCallback_video;
import com.example.bidanbunda_client.bottomnavigation.videomateri.video.utils.PaginationScrollListener_video;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class video extends AppCompatActivity implements PaginationAdapterCallback_video {

    private static final String TAG = "video";

    PaginationAdapter_video adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError, tv_content,tv_week;
    SwipeRefreshLayout swipeRefreshLayout;

    private static final int PAGE_START = 1;

    String week,content,cookie;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;
    private videoService movieService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        rv = findViewById(R.id.vdo_card_recycler_view_list_vm);
        progressBar = findViewById(R.id.main_progress);
        errorLayout = findViewById(R.id.error_layout);
        btnRetry = findViewById(R.id.error_btn_retry);
        txtError = findViewById(R.id.error_txt_cause);
        tv_week = findViewById(R.id.vdo_tv_week);
        tv_content = findViewById(R.id.vdo_tv_content);

        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        week = intent.getStringExtra("week");

        tv_week.setText("Minggu Ke- "+ week +"" );
        tv_content.setText(content);

        SharedPreferences sharedPreferences = getSharedPreferences("cookie",MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie","");


        getSupportActionBar().hide();
        adapter = new PaginationAdapter_video(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new PaginationScrollListener_video(linearLayoutManager) {
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

        //init service and load data
        movieService = retrofitapi.getClient(this).create(videoService.class);

        loadFirstPage();

        btnRetry.setOnClickListener(view -> loadFirstPage());



    }



    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callTopRatedMoviesApi().enqueue(new Callback<response_video>() {
            @Override
            public void onResponse(Call<response_video> call, Response<response_video> response) {
                hideErrorView();



//                Log.i(TAG, "onResponse: " + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                // Got data. Send it to adapter
                List<data_video> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);


                TOTAL_PAGES = response.body().getPages();



                Log.d("TOTAL PAGES",String.valueOf(TOTAL_PAGES));

                if(currentPage == TOTAL_PAGES) {
                    isLastPage = true;
                }else if (currentPage <= TOTAL_PAGES){
                    adapter.addLoadingFooter();
                }
            }

            @Override
            public void onFailure(Call<response_video> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }

    /**
     * @param response extracts List<{@link data_video >} from response
     * @return
     */
    private List<data_video> fetchResults(Response<response_video> response) {
        response_video topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<response_video>() {
            @Override
            public void onResponse(Call<response_video> call, Response<response_video> response) {
//                Log.i(TAG, "onResponse: " + currentPage
//                        + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                adapter.removeLoadingFooter();
                isLoading = false;

                List<data_video> results = fetchResults(response);
                adapter.addAll(results);

                TOTAL_PAGES = response.body().getPages();


                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<response_video> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }


    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener_video} to load next page.
     */
    private Call<response_video> callTopRatedMoviesApi() {
        return movieService.getTopRatedMovies(
                currentPage,10, Integer.parseInt(week) ,cookie
        );
    }


    @Override
    public void retryPageLoad() {
        loadNextPage();
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