package com.example.bidanbunda_client.bottomnavigation.profile;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.bidanbunda_client.MainActivity;
import com.example.bidanbunda_client.R;
import com.example.bidanbunda_client.SignIn.SignIn;
import com.example.bidanbunda_client.api.retrofit.retrofitapi;
import com.example.bidanbunda_client.bottomnavigation.profile.SignOut.signOutService;
import com.example.bidanbunda_client.bottomnavigation.profile.adapter.DrawerItemCustomAdapter;
import com.example.bidanbunda_client.bottomnavigation.profile.drawerModels.drawerModel;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.catatanKhusus;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.jadwalDiskusi;
import com.example.bidanbunda_client.bottomnavigation.profile.extend.vaksin.vaksin;
import com.example.bidanbunda_client.bottomnavigation.videomateri.api.vmService;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class nav_profile extends Fragment {


    private TextView tv_alamat, tv_name, tv_phone, tv_logout;
    private ImageView img_pro, img_menu;
    String cookie;

    //drawer
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;


    ProgressDialog progressDialog;
    public nav_profile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_profile, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("profile",MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        String telephone = sharedPreferences.getString("telephone","");
        String full_address = sharedPreferences.getString("full_address","");
        String profile_image = sharedPreferences.getString("profile_image","");


        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("cookie",MODE_PRIVATE);
        cookie = sharedPreferences2.getString("cookie", "");

        tv_alamat =  view.findViewById(R.id.pro_tv_alamat);
        tv_name =  view.findViewById(R.id.pro_tv_name);
        tv_phone = view.findViewById(R.id.pro_tv_notelp);
        img_pro =  view.findViewById(R.id.pro_image_main);
        img_menu = view.findViewById(R.id.pro_img_menu);

        //drawer
        mTitle = mDrawerTitle = getActivity().getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) view.findViewById(R.id.left_drawer);
        mDrawerList.setDivider(null);

        drawerModel[] drawerItem = new drawerModel[5];

        drawerItem[0] = new drawerModel(0, name);
        drawerItem[1] = new drawerModel(R.drawable.ic_vaksin, "Vaksin");
        drawerItem[2] = new drawerModel(R.drawable.ic_catatankhusus, "Catatan Khusus");
        drawerItem[3] = new drawerModel(R.drawable.ic_jadwaldiskusi, "Jadwal Diskusi");
        drawerItem[4] = new drawerModel(R.drawable.ic_arrow_forward_black_24dp, "Keluar");

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(getContext(), R.layout.row_pro_drawer, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
//        tv_logout = view.findViewById(R.id.pro_tv_logout);


        progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading. Please wait...", true);
        progressDialog.dismiss();

        tv_alamat.setText(full_address);
        tv_phone.setText(telephone);
        tv_name.setText(name);

//        if (profile_image.equals("")){
//
//        }else {
//            Glide.with(img_pro.getContext()).load(profile_image).into(img_pro);
//        }

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                Toast.makeText(getActivity(),"AA", Toast.LENGTH_SHORT).show();
            }
        });

//        tv_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signout();
//                progressDialog.show();
//            }
//        });



        return view;
    }

    private void signout() {

        String device_token =  FirebaseInstanceId.getInstance().getToken();
        Log.d("DEVICE TOKEN", device_token);

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("device_token",device_token);


        signOutService api = retrofitapi.getClient(getContext()).create(signOutService.class);
        Call<ResponseBody> call = api.signOut(cookie,hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200){

                    Intent i = new Intent(getActivity(), SignIn.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(i);
                    getActivity().finish();


                    SharedPreferences.Editor logstatus = getActivity().getSharedPreferences("logstatus", MODE_PRIVATE).edit();
                    logstatus.putString("logstatus", "0");
                    logstatus.apply();

                    progressDialog.dismiss();
                }else {
                    Toast.makeText(getActivity(),"Gagal", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(),"Gagal", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }




    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {

            case 1:


                Intent intent = new Intent(getActivity(),vaksin.class);
             startActivity(intent);
                break;

            case 2:

                Intent intent2 = new Intent(getActivity(), catatanKhusus.class);
                startActivity(intent2);

                break;

            case 3:


                Intent intent3 = new Intent(getActivity(), jadwalDiskusi.class);
                startActivity(intent3);

                break;
            case 4:
                signout();
                progressDialog.show();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
//            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void setTitle(CharSequence title) {
//        mTitle = title;
//        getSupportActionBar().setTitle(mTitle);
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }



    void setupDrawerToggle(){
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }
}
