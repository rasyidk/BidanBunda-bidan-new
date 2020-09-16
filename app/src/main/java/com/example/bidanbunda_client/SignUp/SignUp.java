package com.example.bidanbunda_client.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bidanbunda_client.MainActivity;
import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.SignIn.SignIn;
import com.example.bidanbunda_client.SignUp.SignUpToken.SignUp_token;
import com.example.bidanbunda_client.SignUp.api.signup_interface;
import com.example.bidanbunda_client.SignUp.models.response_signup;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.google.android.material.textfield.TextInputLayout;
import com.santalu.maskedittext.MaskEditText;

import maes.tech.intentanim.CustomIntent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {


    EditText et_namalengkap,et_username, et_password ,et_password_con, et_alamat;
    MaskEditText et_phone;
    Button bt_lanjut, bt_batal;
    TextInputLayout input_password;
    int visibility =0;

    ProgressDialog progressDialog;

    String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();


        et_namalengkap =  findViewById(R.id.up_et_nama_lengkap);
        et_username = findViewById(R.id.up_et_username);
        et_password_con =  findViewById(R.id.up_et_password_con);
        et_phone =  findViewById(R.id.up_et_phone);
        et_alamat =  findViewById(R.id.up_et_alamat);
        et_password = findViewById(R.id.up_et_password);
        bt_lanjut = findViewById(R.id.up_bt_lanjut);
        bt_batal =  findViewById(R.id.up_bt_batal);

        input_password = findViewById(R.id.up_input_password);

        progressDialog = ProgressDialog.show(SignUp.this, "",
                "Loading. Please wait...", true);
        progressDialog.dismiss();


        showPassword();

        bt_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();

            }
        });

        bt_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });

    }

    private void getData() {
        String name, username, password, passwordcon ,full_address, telephone;
        name = et_namalengkap.getText().toString();
        username = et_username.getText().toString();
        password =  et_password.getText().toString();
        passwordcon = et_password_con.getText().toString();
        full_address =  et_alamat.getText().toString();
        telephone = et_phone.getText().toString();
        telephone = telephone.replace("(+62) ", "");


        if (name.equals("") || username.equals("") || password.equals("") || passwordcon.equals("") || full_address.equals("") || telephone.equals("")){
            Toast.makeText(getApplicationContext(), "disini", Toast.LENGTH_SHORT).show();
        }else {
            if (password.equals(passwordcon)){

                if (password.length() < 8){
                    Toast.makeText(getApplicationContext(), "password min 8 karakter", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(getApplicationContext(), "MATCH", Toast.LENGTH_SHORT).show();
                    cekUsernameAvailable(name, username, password,full_address, telephone);
                    progressDialog.show();
//                    phone = phone.replace("(+62) ", "");
//                    phone = "62"+ phone +"";
//                    uploadingData(namalengkap, username, password,alamat, phone);
                }

            }else {
                Toast.makeText(getApplicationContext(),"password tidak cocok", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void cekUsernameAvailable(String name, String username, String password , String full_address, String telephone) {





        signup_interface api = retrofitapi.getClient(getApplicationContext()).create(signup_interface.class);
        Call<ResponseBody> call = api.cekUsername("api/v1/users/"+ username +"");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    Toast.makeText(getApplicationContext(),"DIGUNAKAN",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    showPopUpError();

                }else if (response.code() == 404){
                    Toast.makeText(getApplicationContext(),"ADA",Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(SignUp.this, SignUp_token.class);
                    intent.putExtra("name", name);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("full_address", full_address);
                    intent.putExtra("telephone", telephone);
                    startActivity(intent);
                    CustomIntent.customType(SignUp.this, "left-to-right");

                    progressDialog.dismiss();

                }else{
                    Toast.makeText(getApplicationContext(),"terjadi error",Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }

    private void showPopUpError() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        builder.setTitle("Konfirmasi");

        String message = "Username Telah digunakan";

        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });




        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();

    }


    private void getUser(Integer user_id) {

        signup_interface api = retrofitapi.getClient(getApplicationContext()).create(signup_interface.class);
        Call<response_signup> call = api.getUser("api/v1/users/" + user_id +"", cookie);
        call.enqueue(new Callback<response_signup>() {
            @Override
            public void onResponse(Call<response_signup> call, Response<response_signup> response) {

                if (response.code() == 200){
                    Toast.makeText(getApplicationContext(), "Sukses login", Toast.LENGTH_SHORT).show();
                    Integer id = response.body().getData().getId();
                    String user_type = response.body().getData().getUser_type();
                    String name = response.body().getData().getName();
                    String usernamex =  response.body().getData().getUser_name();
                    String full_address = response.body().getData().getFull_address();
                    String profile_image = response.body().getData().getProfile_image();
                    double  telephone = response.body().getData().getTelephone();


                    SharedPreferences.Editor editor = getSharedPreferences("profile", MODE_PRIVATE).edit();
                    editor.putInt("user_id",user_id);
                    editor.putString("user_type",user_type);
                    editor.putString("name",name);
                    editor.putString("username",usernamex);
                    editor.putString("password",et_password.getText().toString());
                    editor.putString("full_address",full_address);
                    editor.putString("profile_image",profile_image);
                    editor.putString("telephone",String.valueOf(telephone));
                    editor.apply();

                    SharedPreferences.Editor editor2 = getSharedPreferences("cookie", MODE_PRIVATE).edit();
                    editor2.putString("cookie", cookie);
                    editor2.apply();

                    Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(SignUp.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"server error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<response_signup> call, Throwable t) {
                Log.d("erro", t.toString());
            }
        });
    }


    private void showPassword() {

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < input_password.getCounterMaxLength()){
                    input_password.setError("Min karakter " + input_password.getCounterMaxLength());
                }else {
                    input_password.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {



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
}
