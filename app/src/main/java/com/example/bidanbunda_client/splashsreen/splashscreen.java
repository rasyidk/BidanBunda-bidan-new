package com.example.bidanbunda_client.splashsreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.bidanbunda_client.MainActivity;
import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.SignIn.SignIn;

import java.util.Timer;
import java.util.TimerTask;

import static maes.tech.intentanim.CustomIntent.customType;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences("logstatus",MODE_PRIVATE);
        String logstatus = sharedPreferences.getString("logstatus","0");
        splashscreenact(logstatus);
    }

    private void splashscreenact(String logstatus) {

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (logstatus.equals("0")) {
                    Intent i = new Intent(splashscreen.this, SignIn.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    customType(splashscreen.this,"fadein-to-fadeout");
                    finish();
                }else {
                    Intent i = new Intent(splashscreen.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    customType(splashscreen.this,"fadein-to-fadeout");
                    finish();
                }
            }
        },2000);



    }


}
