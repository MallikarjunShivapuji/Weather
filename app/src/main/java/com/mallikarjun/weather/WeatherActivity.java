package com.mallikarjun.weather;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;
import com.mallikarjun.weather.notification.PeriodicNotification;
import com.mallikarjun.weather.ui.main.SectionsPagerAdapter;
import com.mallikarjun.weather.viewmodel.WeatherViewModel;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class WeatherActivity extends AppCompatActivity {

    private static String TAG = WeatherActivity.class.getSimpleName();

    private PeriodicWorkRequest mPeriodicWorkRequest;
    private WeatherViewModel mWetherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter sectionsPagerAdapter =
                new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        mWetherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            askRunTimePermissions();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mPeriodicWorkRequest = new PeriodicWorkRequest.Builder(PeriodicNotification.class,
                    1, TimeUnit.MINUTES).build();
        }
        WorkManager.getInstance(this).enqueue(mPeriodicWorkRequest);

        LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
    }

    private void askRunTimePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean fineLocationPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (fineLocationPermission) {
            Log.e(TAG, "onRequestPermissionsResult RunTimePermission granted");
            mWetherViewModel.startListingLocationChange();
            mWetherViewModel.getWeatherByLocation(null);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}