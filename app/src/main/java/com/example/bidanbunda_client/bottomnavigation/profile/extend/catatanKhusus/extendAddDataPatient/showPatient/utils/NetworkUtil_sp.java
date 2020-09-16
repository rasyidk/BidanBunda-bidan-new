package com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.showPatient.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkUtil_sp {

    public static boolean hasNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) isConnected = true;
        return isConnected;

    }

}
