package com.example.bidanbunda_client.api.retrofit;

import android.content.Context;

import com.example.bidanbunda_client.api.utils.NetworkUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

public class retrofitapi {
    private static retrofit2.Retrofit retrofit = null;

    private final static long CACHE_SIZE = 10 * 1024 * 1024; // 10MB Cache size

    private static OkHttpClient buildClient(Context context) {

        // Build interceptor
        final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
            Response originalResponse = chain.proceed(chain.request());
            if (NetworkUtil.hasNetwork(context)) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        };

        // Create Cache
        Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE);

        return new OkHttpClient
                .Builder()
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cache(cache)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }


    // TODO: 2/17/19 Update API to v4
    public static retrofit2.Retrofit getClient(Context context) {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .client(buildClient(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://bidan-bunda.herokuapp.com/")
                    .build();
        }
        return retrofit;
    }
}
