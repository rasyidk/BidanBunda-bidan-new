package com.example.bidanbunda_client.SignUp.SignUpToken;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bidanbunda_client.MainActivity;
import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.SignIn.SignIn;
import com.example.bidanbunda_client.SignUp.SignUp;
import com.example.bidanbunda_client.SignUp.SignUpToken.api.signuptoken_interface;
import com.example.bidanbunda_client.SignUp.SignUpToken.models.response_signuptoken;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp_token extends AppCompatActivity {

    String name, username, password, passwordcon ,full_address, telephone;
    Button bt_daftar ,bt_kembali;
    EditText et_token;

    ProgressDialog progressDialog;

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_token);
        getSupportActionBar().hide();


        bt_daftar =  findViewById(R.id.token_bt_daftar);
        bt_kembali = findViewById(R.id.token_bt_kembali);
        et_token = findViewById(R.id.tk);

        progressDialog = ProgressDialog.show(SignUp_token.this, "",
                "Loading. Please wait...", true);
        progressDialog.dismiss();
        //Getintent
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        full_address = intent.getStringExtra("full_address");
        telephone = intent.getStringExtra("telephone");


        bt_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token =  et_token.getText().toString();
                cekToken(token);
            }
        });

        bt_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(SignUp_token.this, SignUp.class);
                startActivity(i);
                CustomIntent.customType(SignUp_token.this, "right-to-left");
            }
        });
    }

    private void cekToken(String token) {

        if (token.equals("")) {
            Toast.makeText(getApplicationContext(), "masukan token dahulu", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.show();
            signuptoken_interface api = retrofitapi.getClient(getApplicationContext()).create(signuptoken_interface.class);
            Call<JsonObject> call = api.cekToken("api/v1/puskesmas/tokens/"+ token +"");
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    if (response.code() == 200){
                        Toast.makeText(getApplicationContext(),"Token terdaftar", Toast.LENGTH_SHORT).show();


                        JsonObject mainObject = response.body();
                        JsonArray results = mainObject.getAsJsonArray("data");
                        JsonObject innerResult = (JsonObject) results.get(0);
                        JsonObject geometry = (JsonObject) innerResult.get("puskesmas");
                        String puskesmasname = geometry.get("name").getAsString();
                        String puskesmasaddress = geometry.get("full_address").getAsString();


                        progressDialog.dismiss();
                          showPopUp(puskesmasname, puskesmasaddress);

                    }else {
                        Toast.makeText(getApplicationContext(),"token tidak tersedia", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }

    }

    private void showPopUp(String puskesmasname, String puskesmasaddress) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp_token.this);
        builder.setTitle("Konfirmasi");

        String message = "Puskesmas : "+ puskesmasname +""+
                "\nAlamat : "+ puskesmasaddress +"";

        builder.setMessage(message);



        builder.setPositiveButton("Benar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                uploadingData();

                progressDialog.show();
            }
        });



        builder.setNegativeButton("Salah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when No button clicked

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();

    }

    private void uploadingData() {

        String device_token =  FirebaseInstanceId.getInstance().getToken();
        Log.d("DEVICE TOKEN", device_token);

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("user_type","b");
        hashMap.put("username",username);
        hashMap.put("password",password);
        hashMap.put("full_address",full_address);
        hashMap.put ("telephone", telephone);
        hashMap.put ("puskesmas_token", token);
        hashMap.put ("device_token", device_token);

        signuptoken_interface api = retrofitapi.getClient(getApplicationContext()).create(signuptoken_interface.class);
        Call<response_signuptoken> call = api.signUp(hashMap);
        call.enqueue(new Callback<response_signuptoken>() {
            @Override
            public void onResponse(Call<response_signuptoken> call, Response<response_signuptoken> response) {
                if (response.code() == 200){
                    Toast.makeText(getApplicationContext(),"SUKSES", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(),response.body().getData().getUser_id().toString(), Toast.LENGTH_SHORT).show();


                    String user_id;
                    user_id = response.body().getData().getUser_id().toString();

                    String cookie = response.headers().get("Set-Cookie");
                    getUserData(user_id,cookie);

                }else {
                    Toast.makeText(getApplicationContext(),"gagal mendaftar", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<response_signuptoken> call, Throwable t) {
               Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void getUserData(String user_id, String cookie) {
        signuptoken_interface api = retrofitapi.getClient(getApplicationContext()).create(signuptoken_interface.class);
        Call<response_signuptoken> call = api.getDataUser("api/v1/users/"+ user_id +"", cookie);
        call.enqueue(new Callback<response_signuptoken>() {
            @Override
            public void onResponse(Call<response_signuptoken> call, Response<response_signuptoken> response) {

                if (response.code() == 200){

                    Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_SHORT).show();
                    Integer id = response.body().getData().getId();
                    String user_type = response.body().getData().getUser_type();
                    String name = response.body().getData().getName();
                    String usernamex =  response.body().getData().getUser_name();
                    String full_address = response.body().getData().getFull_address();
                    String profile_image = response.body().getData().getProfile_image();
                    String  telephone = response.body().getData().getTelephone();
                    String  pus_id = response.body().getData().getPus_id();

                    SharedPreferences.Editor editor = getSharedPreferences("profile", MODE_PRIVATE).edit();
                    editor.putString("user_id", user_id);
                    editor.putString("user_type",user_type);
                    editor.putString("name",name);
                    editor.putString("username",usernamex);
                    editor.putString("password",password);
                    editor.putString("full_address",full_address);
                    editor.putString("profile_image",profile_image);
                    editor.putString("telephone",telephone);
                    editor.putString("pus_id",pus_id);
                    editor.apply();




                    SharedPreferences.Editor editorx = getSharedPreferences("cookie", MODE_PRIVATE).edit();
                    editorx.putString("cookie",cookie);
                    editorx.apply();

                    Log.d("cookie", cookie);

   //                 Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(SignUp_token.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                    SharedPreferences.Editor logstatus = getSharedPreferences("logstatus", MODE_PRIVATE).edit();
                    logstatus.putString("logstatus", "1");
                    logstatus.apply();



                }else {
                    Toast.makeText(getApplicationContext(),"SUKSES Mendaftar", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUp_token.this, SignIn.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                }

            }

            @Override
            public void onFailure(Call<response_signuptoken> call, Throwable t) {

                Intent i = new Intent(SignUp_token.this, SignIn.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

            }
        });
    }


}
