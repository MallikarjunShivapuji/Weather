package com.mallikarjun.weather.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.mallikarjun.weather.repository.localrepository.LocalRoomRepo;

import androidx.core.app.ActivityCompat;

public class LocationProvider {

    private static LocationProvider instance;
    private Location mLatestLocation;
    private LocationManager mLocationManager;
    private WeatherViewModel mViewModel;
    private LocalRoomRepo mLocalRepoLocation;

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mViewModel.onLocationChange(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(mViewModel.getApplication(), "Location provider turned ON", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onProviderDisabled(String s) {

            Toast.makeText(mViewModel.getApplication(), "Location provider turned OFF", Toast.LENGTH_LONG).show();
            mViewModel.onLocationChange(mLatestLocation);
        }
    };

    private LocationProvider(WeatherViewModel viewModel) {
        mViewModel = viewModel;
        mLocationManager =
                (LocationManager) viewModel.getApplication().getSystemService(Context.LOCATION_SERVICE);
        startListening();
    }

    public static synchronized LocationProvider getInstance(WeatherViewModel viewModel) {
        if (instance == null) {
            instance = new LocationProvider(viewModel);
        }
        return instance;
    }

    public Location getLocation() {
        if (mLatestLocation == null && ActivityCompat.checkSelfPermission(mViewModel.getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mLatestLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return mLatestLocation;
    }

    public void stopListening() {
        mLocationManager.removeUpdates(mLocationListener);
    }

    public void startListening() {
        if (ActivityCompat.checkSelfPermission(mViewModel.getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,
                    500, mLocationListener);
        }
    }

    public interface OnLocationChangeListener {
        void onLocationChange(Location location);
    }
}
