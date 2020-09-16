package com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bidanbunda_client.MainActivity;
import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.grupchat.models.data_gc;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.adapter.PaginationAdapter_ck;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.api.ckService;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.models.data_ck;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.models.response_ck;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.utils.PaginationAdapterCallback_ck;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.utils.PaginationScrollListener_ck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import at.markushi.ui.CircleButton;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class chatkonsultasi extends AppCompatActivity implements PaginationAdapterCallback_ck {

    private static final String TAG = "chatkonsultasi";

    PaginationAdapter_ck adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar,lineProgressbar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError, tv_name;
    EditText et_msg;
    ImageView ck_image_backbutton;

    CircleButton bt_sendmsg;

    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean isChatLoad = true;
    private boolean isSendingChat = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;

    private ckService movieService;

    String name, targetid, user_id,cookie;

    ArrayList<data_ck> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatkonsultasi);
        getSupportActionBar().hide();

        rv = findViewById(R.id.ck_recyclervww);
        progressBar = findViewById(R.id.ck_main_progress);
        errorLayout = findViewById(R.id.error_layout);
        btnRetry = findViewById(R.id.error_btn_retry);
        txtError = findViewById(R.id.error_txt_cause);
        tv_name= findViewById(R.id.ck_tv_name);
        bt_sendmsg = findViewById(R.id.ck_btn_sendksl);
        et_msg = findViewById(R.id.ck_et_msg);
        ck_image_backbutton =  findViewById(R.id.ck_image_backbutton);
        lineProgressbar = findViewById(R.id.ck_lineprogressbar);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        targetid = intent.getStringExtra("targetid");


        tv_name.setText(name);


        SharedPreferences sharedPreferences2 = getSharedPreferences("profile",MODE_PRIVATE);
        user_id = sharedPreferences2.getString("user_id","");

        SharedPreferences sharedPreferences = getSharedPreferences("cookie",MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie","");


        adapter = new PaginationAdapter_ck(this, user_id);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener_ck(linearLayoutManager) {
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
        movieService =  retrofit.create(ckService.class);


        //old
//        movieService = retrofitapi.getClient(this).create(ckService.class);

        loadFirstPage();

        btnRetry.setOnClickListener(view -> loadFirstPage());

        ck_image_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(chatkonsultasi.this, MainActivity.class);
                i.putExtra("fragmentType","konsultasi");
                startActivity(i);

            }
        });

        data = new ArrayList<>();
        bt_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message =  et_msg.getText().toString();
                if (message.equals("")){

                }else {
                    if (isChatLoad == false){
                        if (isSendingChat == false){
                            isSendingChat =true;
                            lineProgressbar.setVisibility(View.VISIBLE);
                            sendMessageToServer(message);
                            et_msg.setText("");

                        }else {
                            Toast.makeText(getApplicationContext(),"Masih Mengirim", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        et_msg.setText("");
                        Toast.makeText(getApplicationContext(),"Masih Loading", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void sendMessageToServer(String message) {

        HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("message",message);

                    ckService api  =  retrofitapi.getClient(this).create(ckService.class);
                    Call<ResponseBody> call = api.sendMessageCk("api/v1/chats/"+ targetid +"",hashMap, cookie);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.code() == 201){
                                Toast.makeText(getApplicationContext(), "SUKSES", Toast.LENGTH_SHORT).show();

                                lineProgressbar.setVisibility(View.GONE);
                                isSendingChat =false;
                                addChatToObject(message);

                            }else {
                                Toast.makeText(getApplicationContext(), "Pesan tidak terkirim", Toast.LENGTH_SHORT).show();
                                lineProgressbar.setVisibility(View.GONE);
                                isSendingChat =false;
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Jaringan Error", Toast.LENGTH_SHORT).show();
                            lineProgressbar.setVisibility(View.GONE);
                            isSendingChat =false;
                        }
                    });
    }

    private void addChatToObject(String message) {

        Long timestamp = System.currentTimeMillis()/1000;

        List<data_ck> list = new ArrayList<data_ck>();
        list.add(0, new data_ck(message,user_id,timestamp));
        adapter.addAll2(list);
        adapter.notifyDataSetChanged();
           et_msg.setText("");

    }


    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callTopRatedMoviesApi().enqueue(new Callback<response_ck>() {
            @Override
            public void onResponse(Call<response_ck> call, Response<response_ck> response) {
                hideErrorView();


            if (response.code() == 200) {
//                if (response.body().getResults().isEmpty()) {
//                    isLastPage = true;
//                    progressBar.setVisibility(View.GONE);
//
//
//                    Log.d("GET RESULTT", "NULLLL BROOO");
//                    Toast.makeText(getApplicationContext(), "NULLLLL", Toast.LENGTH_SHORT).show();
//
//                } else {
                    // Got data. Send it to adapter
                    List<data_ck> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);
                    TOTAL_PAGES = response.body().getPages();


                    isChatLoad = false;
                    Log.d("GET RESULTT", "ADAAAA");

                    if (currentPage == TOTAL_PAGES) {
                        isLastPage = true;
                    } else if (currentPage <= TOTAL_PAGES) {
                        adapter.addLoadingFooter();
                    //}
                }

            }else if (response.code() ==404){
                progressBar.setVisibility(View.GONE);
                isLastPage = true;
                isChatLoad = false;

            }else {
                showErrorViewString("Terjadi error "+ response.code() +"");
                progressBar.setVisibility(View.GONE);
            }

            }

            @Override
            public void onFailure(Call<response_ck> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private List<data_ck> fetchResults(Response<response_ck> response) {
        response_ck topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<response_ck>() {
            @Override
            public void onResponse(Call<response_ck> call, Response<response_ck> response) {

                adapter.removeLoadingFooter();
                isLoading = false;

                List<data_ck> results = fetchResults(response);
                adapter.addAll(results);

                TOTAL_PAGES = response.body().getPages();

                isChatLoad = false;

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<response_ck> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }


    private Call<response_ck> callTopRatedMoviesApi() {
        return movieService.getTopRatedMovies(
               "api/v1/chats/"+ targetid +"",currentPage,10,cookie
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

    private void showErrorViewString(String error) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(error);
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


    IntentFilter filter = new IntentFilter("konsultasi");
    private BroadcastReceiver smsBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String senderName = intent.getStringExtra("senderName");
            String messagex = intent.getStringExtra("message");
            String senderId = intent.getStringExtra("senderId");


            if (isChatLoad == false){
                if (senderId.equals(targetid)){
                    Long timestamp = System.currentTimeMillis()/1000;

//                Toast.makeText(getApplicationContext(), messagex, Toast.LENGTH_SHORT).show();
                    List<data_ck> list = new ArrayList<data_ck>();
                    list.add(0, new data_ck(messagex,senderId,timestamp));
                    adapter.addAll2(list);
                    adapter.notifyDataSetChanged();
                }
            }





        }
    };



    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(smsBroadcastReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(smsBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        Intent i =  new Intent(chatkonsultasi.this, MainActivity.class);
        i.putExtra("fragmentType","konsultasi");
        startActivity(i);
    }
}
