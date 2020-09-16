package com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.addNotePatient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.SignIn.SignIn;
import com.example.bidanbunda_client.SignIn.api.signin_interface;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.catatanKhusus;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.addNotePatient.api.addNotePatientService;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addNotePatient extends AppCompatActivity {


    ImageView img_done, note_image_backbutton;
    TextView tv_name,tv_medicalrecord;
    EditText et_note;
    String cookie, user_id,targetid;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_patient);
        getSupportActionBar().hide();

        img_done = findViewById(R.id.note_img_done);
        note_image_backbutton = findViewById(R.id.note_image_backbutton);
        et_note =  findViewById(R.id.note_et_note);
        tv_medicalrecord = findViewById(R.id.note_tv_medicalrecord);
        tv_name = findViewById(R.id.note_tv_name);

        progressDialog = ProgressDialog.show(addNotePatient.this, "",
                "Loading. Please wait...", true);
        progressDialog.dismiss();

        SharedPreferences sharedPreferences = getSharedPreferences("cookie",MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie","");

        SharedPreferences sharedPreferences2 = getSharedPreferences("profile",MODE_PRIVATE);
        user_id = sharedPreferences2.getString("user_id","");


        String type =  getIntent().getStringExtra("type");
        targetid =  getIntent().getStringExtra("targetid");
        String medicalrecord = getIntent().getStringExtra("medicalrecord");
        String name = getIntent().getStringExtra("name");
        String note = getIntent().getStringExtra("note");
        Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();


        et_note.requestFocus();

        if (type.equals("edit")){
            tv_name.setText(name);
            tv_medicalrecord.setText(medicalrecord);
            et_note.setText(note);
        }else if(type.equals("add")){
            tv_name.setText(name);
            tv_medicalrecord.setText(medicalrecord);
            et_note.setText("");
        }

        img_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = et_note.getText().toString();
                if (note.equals("")){
                    Toast.makeText(getApplicationContext(),"Catatan Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    sendNote(note,type);
                }
            }
        });

        note_image_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(addNotePatient.this, catatanKhusus.class);
                startActivity(i);
            }
        });
    }

    private void sendNote(String note, String type) {

        progressDialog.show();

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("patient_note",note);
        hashMap.put("user_id",targetid);

        Log.d("NOTE",note);

        if (type.equals("add")){

            addNotePatientService api = retrofitapi.getClient(getApplicationContext()).create(addNotePatientService.class);
            Call<ResponseBody> call = api.addNote("api/v1/patients",hashMap, cookie);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200 || response.code() ==201){
                        Toast.makeText(getApplicationContext(), "Sukses Menambahkan", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent i = new Intent(addNotePatient.this, catatanKhusus.class);
                        startActivity(i);

                    }else {
                        Toast.makeText(getApplicationContext(), "Gagal Menambahkan", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Jaringan Error", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }
            });

        }else if(type.equals("edit")){

            addNotePatientService api = retrofitapi.getClient(getApplicationContext()).create(addNotePatientService.class);
            Call<ResponseBody> call = api.editNote("api/v1/patients/"+ targetid +"",hashMap, cookie);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200 || response.code() ==201){
                        Toast.makeText(getApplicationContext(), "Sukses Mengupdate", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(addNotePatient.this, catatanKhusus.class);
                        startActivity(i);

                    }else {
                        Toast.makeText(getApplicationContext(), "Gagal Menambahkan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Jaringan Error", Toast.LENGTH_SHORT).show();
                }
            });

        }



    }
}
