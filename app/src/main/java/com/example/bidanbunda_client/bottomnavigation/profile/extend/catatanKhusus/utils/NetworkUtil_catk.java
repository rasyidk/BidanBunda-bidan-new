package com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Pagination
 * Created by Suleiman19 on 2/17/19.
 * Copyright (c) 2019. Suleiman Ali Shakir. All rights reserved.
 */
public class NetworkUtil_catk {

    public static boolean hasNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) isConnected = true;
        return isConnected;

    }

}
