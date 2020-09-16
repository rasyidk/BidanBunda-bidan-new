package com.example.bidanbunda_client.bottomnavigation.videomateri;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.videomateri.api.vmService;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bidanbunda_client.bottomnavigation.videomateri.models.data_vm;
import com.example.bidanbunda_client.bottomnavigation.videomateri.models.response_vm;

import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.api.utils.PaginationAdapterCallback;
import com.example.bidanbunda_client.api.utils.PaginationScrollListener;
import com.facebook.shimmer.ShimmerFrameLayout;


import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class nav_videomateri extends Fragment implements PaginationAdapterCallback {

    private static final String TAG = "MainActivity";

    private PaginationAdapter_vm adapter;

    LinearLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;


    //pagination
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    private vmService movieService;

    String cookie;

    public nav_videomateri() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_videomateri, container, false);


        rv = view.findViewById(R.id.main_recycler);
        progressBar = view.findViewById(R.id.main_progress);
        errorLayout = view.findViewById(R.id.error_layout);
        btnRetry = view.findViewById(R.id.error_btn_retry);
        txtError = view.findViewById(R.id.error_txt_cause);
        mShimmerViewContainer = view.findViewById(R.id.vm_shimmer_layout);


        adapter = new PaginationAdapter_vm(view.getContext(), nav_videomateri.this);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cookie",MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie","");

        Log.d("CKBOS", cookie);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        rv.addItemDecoration(dividerItemDecoration);


        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                Integer cupage = currentPage;
                Log.d("CUPAGE", "loadNextPage: " + cupage.toString());
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
        movieService = retrofitapi.getClient(getContext()).create(vmService.class);

        loadFirstPage();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFirstPage();

                mShimmerViewContainer.startShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        });


        //load shimmer
        mShimmerViewContainer.startShimmer();
        return  view;
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callTopRatedMoviesApi().enqueue(new Callback<response_vm>() {
            @Override
            public void onResponse(Call<response_vm> call, Response<response_vm> response) {

                if(response.code() == 200){
                    hideErrorView();

                    List<data_vm> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);

                    TOTAL_PAGES = response.body().getPages();

                    if(currentPage == TOTAL_PAGES) {
                        isLastPage = true;
                    }else if (currentPage <= TOTAL_PAGES){
                        adapter.addLoadingFooter();
                    }
                }else {
                    showErrorViewstr(String.valueOf(response.code()));
                }

                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<response_vm> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);

                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        });
    }

    private List<data_vm> fetchResults(Response<response_vm> response) {
        response_vm topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }


    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);


        callTopRatedMoviesApi().enqueue(new Callback<response_vm>() {
            @Override
            public void onResponse(Call<response_vm> call, Response<response_vm> response) {
//                Log.i(TAG, "onResponse: " + currentPage
//                        + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                adapter.removeLoadingFooter();
                isLoading = false;

                TOTAL_PAGES = response.body().getPages();

                List<data_vm> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<response_vm> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }
    private Call<response_vm> callTopRatedMoviesApi() {
        return movieService.getTopRatedMovies(
                currentPage,10,cookie
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
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }




}
