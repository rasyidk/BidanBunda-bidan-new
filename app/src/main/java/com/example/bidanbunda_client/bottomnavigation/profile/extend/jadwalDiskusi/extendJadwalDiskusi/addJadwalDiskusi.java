package com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.extendJadwalDiskusi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.SignUp.SignUpToken.SignUp_token;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.extendJadwalDiskusi.api.addjdService;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.jadwalDiskusi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addJadwalDiskusi extends AppCompatActivity implements timeBottomSheetDialog.BottomSheetListener,dateBottomSheetDialog.dateBottomSheetListener {


    ImageView btn_img_date, btn_img_time,btn_img_done,btn_img_close;
    RelativeLayout rl_time,rl_date;
    TextView tv_date,tv_time;
    EditText et_content;
    String ts_time_en,ts_time_st ,ts_date,cookie;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jadwal_diskusi);
        getSupportActionBar().hide();

        btn_img_date = findViewById(R.id.jd_img_date_arrow);
        btn_img_time = findViewById(R.id.jd_img_time_arrow);
        btn_img_done = findViewById(R.id.jd_img_add);
        btn_img_close = findViewById(R.id.jd_image_back);

        rl_date =  findViewById(R.id.jd_rl_date);
        rl_time = findViewById(R.id.jd_rl_time);

        tv_date = findViewById(R.id.jd_tv_date);
        tv_time =  findViewById(R.id.jd_tv_time);

        et_content = findViewById(R.id.jd_et_content);

        progressDialog = ProgressDialog.show(addJadwalDiskusi.this, "",
                "Loading. Please wait...", true);
        progressDialog.dismiss();

        SharedPreferences sharedPreferences = getSharedPreferences("cookie",MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie","");


        rl_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeBTS();
            }
        });

        rl_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateBTS();
            }
        });

        btn_img_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadingData();
            }
        });

        btn_img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtn();
            }
        });

    }

    private void closeBtn() {
        Intent i = new Intent(addJadwalDiskusi.this, jadwalDiskusi.class);
        startActivity(i);
    }

    private void uploadingData() {
        progressDialog.show();

        String time =  tv_time.getText().toString();
        String date = tv_date.getText().toString();
        String content = et_content.getText().toString();

        if (time.equals("Waktu") || date.equals("Tanggal") || content.equals("")){
            Toast.makeText(getApplicationContext(),"kosong", Toast.LENGTH_SHORT).show();

        }else {
            String time_st = ts_date +" "+ ts_time_st;
            String time_en = ts_date +" "+ ts_time_en;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh.mm.ss");
                Date date_st = sdf.parse(time_st);
                Date date_en = sdf.parse(time_en);

                long millis_st = date_st.getTime();
                long millis_en = date_en.getTime();

                Log.d("XXX_ST", String.valueOf(millis_st));
                Log.d("XXX_EN", String.valueOf(millis_en));

                uploadingToServer(millis_st,millis_en,content);

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    private void uploadingToServer(long millis_st, long millis_en, String content) {

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("timestamp",String.valueOf(millis_st));
        hashMap.put("timestamp_end",String.valueOf(millis_en));
        hashMap.put("title",content);

        addjdService api = retrofitapi.getClient(getApplicationContext()).create(addjdService.class);
        Call<ResponseBody> call = api.postJadwalDiskusi(hashMap,cookie);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() ==  201){

                    progressDialog.dismiss();
                    Intent i = new Intent(addJadwalDiskusi.this, jadwalDiskusi.class);
                    startActivity(i);

                    Toast.makeText(getApplicationContext(),"Sukses Menambahkan", Toast.LENGTH_SHORT).show();

                } else if (response.code()== 500){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Tanggal Tidak Boleh Kemarin",Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Jaringan Error",Toast.LENGTH_SHORT);
            }
        });
    }

    private void showTimeBTS() {
        timeBottomSheetDialog  bottomSheet  = new timeBottomSheetDialog();
        bottomSheet.show(getSupportFragmentManager(),"exampleBottomSheet");
    }

    private void showDateBTS() {
        dateBottomSheetDialog  bottomSheet  = new dateBottomSheetDialog();
        bottomSheet.show(getSupportFragmentManager(),"exampleBottomSheet");
    }

    @Override
    public void onTimePick(String time, String tm_st, String tm_en) {
        tv_time.setText(time +" WIB");

        ts_time_en = tm_en;
        ts_time_st = tm_st;
    }

    @Override
    public void onDatePick(String date, String dateforprocess) {

        tv_date.setText(date);
        ts_date = dateforprocess;
    }
}
