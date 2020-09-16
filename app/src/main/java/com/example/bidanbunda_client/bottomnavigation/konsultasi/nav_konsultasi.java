package com.example.bidanbunda_client.bottomnavigation.konsultasi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.grupchat.models.data_gc;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.api.kslService;

import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.models.data_ck;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.showContact.showContact;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.models.data_ksl;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.models.response_ksl;
import com.example.bidanbunda_client.bottomnavigation.konsultasi.utils.PaginationAdapterCallback_ksl;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class nav_konsultasi extends Fragment {

    private static final String TAG = "MainActivity";
    private ArrayList<data_ksl> data;
    private Adapter_ksl adapter;

    FloatingActionButton btn_addfab;
    LinearLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    LinearLayout konsultasiLayout;
    Button btnRetry;
    TextView txtError,tv_konsultasix;


    String cookie,pus_id,user_id;

    boolean isLoadChat;


    public nav_konsultasi() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_konsultasi, container, false);

        mShimmerViewContainer = view.findViewById(R.id.ksl_shimmer_layout);
        errorLayout = view.findViewById(R.id.error_layout);
        konsultasiLayout = view.findViewById(R.id.ln_konsultasi_layout);
        btnRetry = view.findViewById(R.id.error_btn_retry);
        btn_addfab = view.findViewById(R.id.ksl_fab);
        tv_konsultasix = view.findViewById(R.id.tv_konsultasi);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cookie",MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie","");
        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("profile",MODE_PRIVATE);
        pus_id = sharedPreferences2.getString("pus_id","");
        user_id = sharedPreferences2.getString("user_id","");


        rv = view.findViewById(R.id.ksl_recyclervww);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        rv.addItemDecoration(dividerItemDecoration);


        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShimmerViewContainer.startShimmer();
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                loadDataBidan();
                errorLayout.setVisibility(View.GONE);
                isLoadChat = true;
            }
        });

        btn_addfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), showContact.class);
                v.getContext().startActivity(i);
            }
        });

        mShimmerViewContainer.startShimmer();
        loadDataBidan();
        isLoadChat = true;



        return  view;
    }


    private void loadDataBidan() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bidan-bunda.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        kslService api = retrofit.create(kslService.class);
        Call<response_ksl> call =  api.getTopRatedMovies("api/v1/chats", cookie);
        Log.d("cookue",cookie);
        call.enqueue(new Callback<response_ksl>() {
            @Override
            public void onResponse(Call<response_ksl> call, Response<response_ksl> response) {

                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);

                if (response.code() == 200){
                    response_ksl jsonResponse = response.body();
                    data = new ArrayList<>(Arrays.asList(jsonResponse.getData()));

                    isLoadChat = false;


                    if (response.body().getData().length == 0){
                        Log.d("NULLLLBOSSS", "onResponse: ");
                        konsultasiLayout.setVisibility(View.VISIBLE);

                        adapter = new Adapter_ksl(data);
                        rv.setAdapter(adapter);

                        isLoadChat = false;


                    }else {
                        adapter = new Adapter_ksl(data);
                        rv.setAdapter(adapter);
                        isLoadChat = false;
                    }

                }else {
                    mShimmerViewContainer.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<response_ksl> call, Throwable t) {
                Log.d("ERRROR KONSL", t.toString());
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });


    }



    IntentFilter filter = new IntentFilter("konsultasi");
    private BroadcastReceiver smsBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String senderName = intent.getStringExtra("senderName");
            String message = intent.getStringExtra("message");
            String senderId = intent.getStringExtra("senderId");
            long longtimestamp = intent.getLongExtra("timestamp",0);


            if (isLoadChat == false){
                adapter.addNewData(senderId,senderName,message,longtimestamp);
                adapter.notifyDataSetChanged();

                rv.setAdapter(adapter);
                konsultasiLayout.setVisibility(View.GONE);

                Log.d("longtimestamp",String.valueOf(longtimestamp));
                Toast.makeText(getActivity(),"LOAD FALSE", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(),"LOAD TRUE", Toast.LENGTH_SHORT).show();
            }




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
