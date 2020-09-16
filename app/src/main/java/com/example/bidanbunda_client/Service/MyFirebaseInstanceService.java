package com.example.bidanbunda_client.Service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.bidanbunda_client.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

public class MyFirebaseInstanceService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

//        showNotification("HAHAHA","HEHEHE");
//        Log.d("NOTIF","MESSAGE ANYAR");
//        Log.d("DATA",remoteMessage.getData().toString());
//
//        Intent myIntent = new Intent("FBR-IMAGE");
//        myIntent.putExtra("action",remoteMessage.getData().toString());
//        this.sendBroadcast(myIntent);
//
//        showNotification("JADWALLL","AHAHA");
//

        Log.e("DATA",remoteMessage.getData().toString());
        try
        {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            Log.e("JSON OBJECT", object.toString());

            String type = object.getString("type");
            String senderId = object.getString("senderId");


            String user_id;
            SharedPreferences sharedPreferences2 = getSharedPreferences("profile",MODE_PRIVATE);
            user_id = sharedPreferences2.getString("user_id","");

            showNotification(type,"AHAHA");

            if (type.equals("group")) {
                if (senderId.equals(user_id)) {

                } else {
                    Intent intent = new Intent("group");
                    intent.putExtra("message", object.getString("message"));
                    intent.putExtra("senderId", object.getString("senderId"));
                    intent.putExtra("senderName", object.getString("senderName"));
                    intent.putExtra("timestamp", object.getLong("timestamp"));
                    this.sendBroadcast(intent);
                    showNotification("GROUPCHAT", "" + object.getString("senderName") + ": " + object.getString("message") + "");

                }
            }
            if (type.equals("konsultasi") && !senderId.equals(user_id)) {
                if (senderId.equals(user_id)) {

                } else {
                    Intent i = new Intent("konsultasi");
                    i.putExtra("message", object.getString("message"));
                    i.putExtra("senderId", object.getString("senderId"));
                    i.putExtra("senderName", object.getString("senderName"));
                    i.putExtra("timestamp", object.getString("timestamp"));
                    i.putExtra("timestamp", object.getLong("timestamp"));
                    this.sendBroadcast(i);
                    showNotification("Konsultasi", "" + object.getString("senderName") + ": " + object.getString("message") + "");
                }
            }


//            if (type.equals("group") && senderId.equals(user_id)){
//
//            }else {
//                Intent intent = new Intent("group");
//                intent.putExtra("message", object.getString("message"));
//                intent.putExtra("senderId", object.getString("senderId"));
//                intent.putExtra("senderName", object.getString("senderName"));
//                this.sendBroadcast(intent);
//
//                showNotification("GROUPCHAT",""+ object.getString("senderName") +": "+ object.getString("message")  +"");
//            }
//
//
//            if (type.equals("konsultasi") && senderId.equals(user_id)){
//
//            }else {
//                Intent i = new Intent("konsultasi");
//                i.putExtra("message", object.getString("message"));
//                i.putExtra("senderId", object.getString("senderId"));
//                i.putExtra("senderName", object.getString("senderName"));
//                i.putExtra("timestamp", object.getString("timestamp"));
//                i.putExtra("timestamp", object.getLong("timestamp"));
//                this.sendBroadcast(i);
//                showNotification("Konsultasi",""+ object.getString("senderName") +": "+ object.getString("message")  +"");
//            }

        }catch(Exception ex) {
            Log.e("HHE", "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
            ex.printStackTrace();
        }


    }

    private void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "rasyidk";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("BOYS");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder noitificationBuilder  = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        noitificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_home_black_24dp)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("INFO");

        notificationManager.notify(new Random().nextInt(),noitificationBuilder.build());
    }

//    @Override
//    public void onNewToken(@NonNull String s) {
//
//        Log.d("BOSS", s);
//        Toast.makeText(getApplicationContext(), s,Toast.LENGTH_SHORT).show();
//
//        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
//
//
//    }


}
