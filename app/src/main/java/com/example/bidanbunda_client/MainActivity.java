package com.example.bidanbunda_client;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        getSupportActionBar().hide();

        //       Toast.makeText(getApplicationContext(), getIntent().getStringExtra("fragmentType"),Toast.LENGTH_SHORT).show();
//        if(getIntent().getStringExtra("fragmentType").equals("konsultasi")){
//            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                    R.id.navigation_videomateri, R.id.navigation_grupchat, R.id.navigation_konsultasi, R.id.navigation_notification, R.id.navigation_profile)
//                    .build();
//            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//            NavigationUI.setupWithNavController(navView, navController);
//            navController.navigate(R.id.navigation_konsultasi);
//        }else {
//            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                    R.id.navigation_videomateri, R.id.navigation_grupchat, R.id.navigation_konsultasi, R.id.navigation_notification, R.id.navigation_profile)
//                    .build();
//            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//            NavigationUI.setupWithNavController(navView, navController);
//        }

        if (getIntent().getStringExtra("fragmentType")== null){
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_videomateri, R.id.navigation_grupchat, R.id.navigation_konsultasi, R.id.navigation_notification, R.id.navigation_profile)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }else if (getIntent().getStringExtra("fragmentType").equals("konsultasi")){
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_videomateri, R.id.navigation_grupchat, R.id.navigation_konsultasi, R.id.navigation_notification, R.id.navigation_profile)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
            navController.navigate(R.id.navigation_konsultasi);
        }else if (getIntent().getStringExtra("fragmentType").equals("profile")){
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_videomateri, R.id.navigation_grupchat, R.id.navigation_konsultasi, R.id.navigation_notification, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navController.navigate(R.id.navigation_profile);
    }

        else {
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_videomateri, R.id.navigation_grupchat, R.id.navigation_konsultasi, R.id.navigation_notification, R.id.navigation_profile)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }

    }

}
