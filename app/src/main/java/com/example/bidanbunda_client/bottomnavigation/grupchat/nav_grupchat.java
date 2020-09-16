package com.example.bidanbunda_client.bottomnavigation.grupchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.SignUp.api.signup_interface;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.grupchat.api.gcService;
import com.example.bidanbunda_client.bottomnavigation.grupchat.models.data_gc;
import com.example.bidanbunda_client.bottomnavigation.grupchat.models.response_gc;
import com.example.bidanbunda_client.bottomnavigation.grupchat.utils.PaginationAdapterCallback_gc;
import com.example.bidanbunda_client.bottomnavigation.grupchat.utils.PaginationScrollListener_gc;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import at.markushi.ui.CircleButton;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class nav_grupchat extends Fragment implements PaginationAdapterCallback_gc {

    private static final String TAG = "MainActivity";

    private PaginationAdapter_gc adapter;

    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar,lineProgressbar;
    LinearLayout errorLayout;
    Button btnRetry;
    CircleButton  bt_send;
    TextView txtError;
    EditText et_msg;



    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    boolean isChatLoad;
    boolean isSendingChat = false;


    int TOTAL_PAGES;
    private int currentPage = PAGE_START;



    private gcService movieService;

    String cookie,user_id;

    public nav_grupchat() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_grupchat, container, false);


        rv = view.findViewById(R.id.card_recycler_view_gc);
        progressBar = view.findViewById(R.id.main_progress);
        errorLayout = view.findViewById(R.id.error_layout);
        btnRetry = view.findViewById(R.id.error_btn_retry);
        txtError = view.findViewById(R.id.error_txt_cause);
        bt_send =  view.findViewById(R.id.gc_btn_sendgroup);
        et_msg = view.findViewById(R.id.gc_et_msg);
        lineProgressbar = view.findViewById(R.id.gc_lineprogressbar);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cookie",MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie","");

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("profile",MODE_PRIVATE);
        user_id = sharedPreferences2.getString("user_id","");



        adapter = new PaginationAdapter_gc(view.getContext(), nav_grupchat.this, user_id);



 //       Toast.makeText(getActivity(),user_id, Toast.LENGTH_SHORT).show();

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener_gc(linearLayoutManager) {
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
        //movieService = MovieApi.getClient(getContext()).create(gcService.class);


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
        movieService = retrofit.create(gcService.class);

        loadFirstPage();
        isChatLoad = true;





        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFirstPage();
                isChatLoad = true;
            }
        });


//        lineProgressbar.bringToFront();
//        lineProgressbar.setIndeterminate(false);

        lineProgressbar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = et_msg.getText().toString();

                if (message.equals("")){

                }else {

                    if (isChatLoad == false){

                        if (isSendingChat == false){
                            sendMessageToServer(message);
                            lineProgressbar.setVisibility(View.VISIBLE);
                            isSendingChat = true;
                            et_msg.setText("");
                        }else {
                            Toast.makeText(getActivity(),"MASIH NGIRIM", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        et_msg.setText("");
                        Toast.makeText(getActivity(),"Masih Loading", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        return  view;
    }

    private void sendMessageToServer(String message) {

        gcService api = retrofitapi.getClient(getActivity()).create(gcService.class);

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("message",message);

        Call<ResponseBody> call = api.sendMessageToServer(hashMap,cookie);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    isSendingChat = false;
                    List<data_gc> list = new ArrayList<data_gc>();
                    Long timestamp = System.currentTimeMillis()/1000;
                    list.add(0, new data_gc("me",user_id,message,timestamp));
                    adapter.addAll2(list);
                    adapter.notifyDataSetChanged();
                    et_msg.setText("");

                    lineProgressbar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(getActivity(),"Gagal Mengirim Pesan", Toast.LENGTH_SHORT).show();
                    isSendingChat = false;
                    lineProgressbar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(),"Jaringan Error", Toast.LENGTH_SHORT).show();
                isSendingChat = false;
                lineProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callTopRatedMoviesApi().enqueue(new Callback<response_gc>() {
            @Override
            public void onResponse(Call<response_gc> call, Response<response_gc> response) {

                hideErrorView();


                if (response.code() == 200 || response.code() == 404){
                    isChatLoad = false;

                    List<data_gc> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);

                    TOTAL_PAGES = response.body().getPages();

                    if (response.body().getResults().isEmpty()){
                        isLastPage = true;
                        Log.d("LAStPASGE","TRUE");
                    }

                    if(currentPage == TOTAL_PAGES) {
                        isLastPage = true;
                    }else if (currentPage <= TOTAL_PAGES){
                        adapter.addLoadingFooter();
                    }
                }else {
                    Toast.makeText(getContext(),response.code(),Toast.LENGTH_SHORT).show();
                    String  errr= String.valueOf(response.code());
                    showErrorViewstr(errr);
                }




            }

            @Override
            public void onFailure(Call<response_gc> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }

    private List<data_gc> fetchResults(Response<response_gc> response) {
        response_gc topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }


    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);


        callTopRatedMoviesApi().enqueue(new Callback<response_gc>() {
            @Override
            public void onResponse(Call<response_gc> call, Response<response_gc> response) {

                adapter.removeLoadingFooter();
                isLoading = false;
                TOTAL_PAGES = response.body().getPages();
                List<data_gc> results = fetchResults(response);
                adapter.addAll(results);


                isChatLoad = false;


                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

            }

            @Override
            public void onFailure(Call<response_gc> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }
    private Call<response_gc> callTopRatedMoviesApi() {
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
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }




        IntentFilter filter = new IntentFilter("group");
    private BroadcastReceiver smsBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String senderName = intent.getStringExtra("senderName");
            String message = intent.getStringExtra("message");
            String senderId = intent.getStringExtra("senderId");
            long timestamp = intent.getLongExtra("timestamp",0);

            if (isChatLoad == false){
                List<data_gc> list = new ArrayList<data_gc>();
                list.add(0, new data_gc(senderName,senderId,message,timestamp));
                adapter.addAll2(list);
                adapter.notifyDataSetChanged();
            }else {
                Toast.makeText(getActivity(),"CHATLOAD TRUE", Toast.LENGTH_SHORT).show();
            }



    //        Toast.makeText(getContext(),""+ senderName +" "+ message +"" + senderId +"",Toast.LENGTH_SHORT).show();


        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(smsBroadcastReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(smsBroadcastReceiver);
    }


}
