package com.example.bidanbunda_client.SignIn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bidanbunda_client.MainActivity;
import com.example.bidanbunda_client.SignIn.api.response_signin;
import com.example.bidanbunda_client.SignUp.SignUp;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.SignIn.api.signin_interface;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignIn extends AppCompatActivity {


    private EditText et_username, et_password;
    private Button bt_masuk, bt_daftar;
    private TextView tv_daftar;
    Integer visibility = 0;
    Integer user_id;
    String cookie;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();

        et_username = findViewById(R.id.in_et_username);
        et_password =  findViewById(R.id.in_et_password);
        bt_masuk =  findViewById(R.id.in_bt_masuk);
        tv_daftar = findViewById(R.id.in_tv_daftar);

        progressDialog = ProgressDialog.show(SignIn.this, "",
                "Loading. Please wait...", true);
        progressDialog.dismiss();


        bt_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                progressDialog.show();
            }
        });

        tv_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(SignIn.this, SignUp.class);
                startActivity(i);
            }
        });

        et_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_password.getRight() - et_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (visibility == 0){
                            et_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            visibility = 1;
                            et_password.setTransformationMethod(null);
                        }else if (visibility == 1){
                            et_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            visibility = 0;
                            et_password.setTransformationMethod(new PasswordTransformationMethod());
                        }

                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void login() {

        String password = et_password.getText().toString();
        String username = et_username.getText().toString();

        if (password.equals("") || username.equals("")){
            Toast.makeText(getApplicationContext(), "tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else {

                     Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://bidan-bunda.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            String device_token =  FirebaseInstanceId.getInstance().getToken();
            Log.d("DEVICE TOKEN", device_token);

            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("username",username);
            hashMap.put("password",password);
            hashMap.put("user_type","b");
            hashMap.put("device_token",device_token);


           signin_interface api =retrofitapi.getClient(getApplicationContext()).create(signin_interface.class);

//            signin_interface api = retrofit.create(signin_interface.class);
            Call<response_signin> call = api.signIn(hashMap);
            call.enqueue(new Callback<response_signin>() {
                @Override
                public void onResponse(Call<response_signin> call, Response<response_signin> response) {

                    if (response.code() == 200) {


                        cookie = response.headers().get("Set-Cookie");
                        user_id = response.body().getData().getUser_id();
 //                       Toast.makeText(getApplicationContext(), response.body().getData().getUser_id().toString(), Toast.LENGTH_SHORT).show();


                        getUser();
                    }else {
                        Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<response_signin> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"jaringan eror", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });


        }

    }

    private void getUser() {

        signin_interface api =retrofitapi.getClient(getApplicationContext()).create(signin_interface.class);
        Call<response_signin> call = api.profilePicture("api/v1/users/" + user_id +"", cookie);
        call.enqueue(new Callback<response_signin>() {
            @Override
            public void onResponse(Call<response_signin> call, Response<response_signin> response) {

                if (response.code() == 200){
                    //Toast.makeText(getApplicationContext(), response.body().getData().getName(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Sukses login", Toast.LENGTH_SHORT).show();

                    Integer id = response.body().getData().getId();
                    String user_type = response.body().getData().getUser_type();
                    String name = response.body().getData().getName();
                    String usernamex =  response.body().getData().getUser_name();
                    String full_address = response.body().getData().getFull_address();
                    String profile_image = response.body().getData().getProfile_image();
                    String telephone = response.body().getData().getTelephone();
                    String pus_id = response.body().getData().getPus_id();


                    SharedPreferences.Editor editor = getSharedPreferences("profile", MODE_PRIVATE).edit();

                    editor.putString("user_id",String.valueOf(user_id));
                    editor.putString("user_type",user_type);
                    editor.putString("name",name);
                    editor.putString("username",usernamex);
                    editor.putString("password",et_password.getText().toString());
                    editor.putString("full_address",full_address);
                    editor.putString("profile_image",profile_image);
                    editor.putString("telephone",telephone);
                    editor.putString("pus_id",pus_id);
                    editor.apply();

                    SharedPreferences.Editor editor2 = getSharedPreferences("cookie", MODE_PRIVATE).edit();
                    editor2.putString("cookie", cookie);
                    editor2.apply();

                    Log.d("cookie",cookie);

                    Intent i = new Intent(SignIn.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                    progressDialog.dismiss();


                    SharedPreferences.Editor logstatus = getSharedPreferences("logstatus", MODE_PRIVATE).edit();
                    logstatus.putString("logstatus", "1");
                    logstatus.apply();

                }else {
                    Toast.makeText(getApplicationContext(),"server error", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                }


            }

            @Override
            public void onFailure(Call<response_signin> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Jaringan Error", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        });

    }


}
