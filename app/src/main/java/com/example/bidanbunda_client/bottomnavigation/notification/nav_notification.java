package com.example.bidanbunda_client.bottomnavigation.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.notification.api.notifService;
import com.example.bidanbunda_client.bottomnavigation.notification.models.data_notif;
import com.example.bidanbunda_client.bottomnavigation.notification.models.response_notif;
import com.example.bidanbunda_client.bottomnavigation.notification.utils.PaginationAdapterCallback_notif;
import com.example.bidanbunda_client.bottomnavigation.notification.utils.PaginationScrollListener_notif;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;
import java.util.concurrent.TimeoutException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static android.content.Context.MODE_PRIVATE;


public class nav_notification extends Fragment implements PaginationAdapterCallback_notif {

    private static final String TAG = "MainActivity";

    private PaginationAdapter_notif adapter;

    LinearLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout,null_layout;
    Button btnRetry;
    TextView txtError, tv_nl_head,tv_nl_caption;


    //pagination
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    private notifService movieService;

    String cookie;

    public nav_notification() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_notification, container, false);


        rv = view.findViewById(R.id.notif_recycler);
        progressBar = view.findViewById(R.id.notif_progress);
        errorLayout = view.findViewById(R.id.error_layout);
        btnRetry = view.findViewById(R.id.error_btn_retry);
        txtError = view.findViewById(R.id.error_txt_cause);
        mShimmerViewContainer = view.findViewById(R.id.notif_shimmer_layout);
        null_layout = view.findViewById(R.id.null_ln_layout);
        tv_nl_head = view.findViewById(R.id.null_tv_head);
        tv_nl_caption = view.findViewById(R.id.null_tv_caption);

        adapter = new PaginationAdapter_notif(view.getContext(), nav_notification.this);


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


        rv.addOnScrollListener(new PaginationScrollListener_notif(linearLayoutManager) {
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


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bidan-bunda.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        movieService = retrofit.create(notifService.class);
        //init service and load data
//        movieService = retrofitapi.getClient(getContext()).create(notifService.class);

        loadFirstPage();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFirstPage();
                null_layout.setVisibility(View.GONE);
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

        callTopRatedMoviesApi().enqueue(new Callback<response_notif>() {
            @Override
            public void onResponse(Call<response_notif> call, Response<response_notif> response) {

                if(response.code() == 200){
                    hideErrorView();

                    if (response.body().getResults().isEmpty()){

                        null_layout.setVisibility(View.VISIBLE);
                        tv_nl_caption.setText("");
                        tv_nl_head.setText("Notification Kosong");

                    }


                    List<data_notif> results = fetchResults(response);
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
            public void onFailure(Call<response_notif> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);

                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        });
    }

    private List<data_notif> fetchResults(Response<response_notif> response) {
        response_notif topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }


    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);


        callTopRatedMoviesApi().enqueue(new Callback<response_notif>() {
            @Override
            public void onResponse(Call<response_notif> call, Response<response_notif> response) {
//                Log.i(TAG, "onResponse: " + currentPage
//                        + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                adapter.removeLoadingFooter();
                isLoading = false;

                TOTAL_PAGES = response.body().getPages();

                List<data_notif> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<response_notif> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }
    private Call<response_notif> callTopRatedMoviesApi() {
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
