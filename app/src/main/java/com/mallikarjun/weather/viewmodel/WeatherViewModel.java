package com.mallikarjun.weather.viewmodel;

import android.app.Application;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mallikarjun.weather.common.Common;
import com.mallikarjun.weather.repository.localrepository.LatLon;
import com.mallikarjun.weather.repository.localrepository.LocalRoomRepo;
import com.mallikarjun.weather.repository.pogos.LiveWeatherPojo;
import com.mallikarjun.weather.repository.pogos.forecastpojo.Daily;
import com.mallikarjun.weather.repository.pogos.forecastpojo.Forecast;
import com.mallikarjun.weather.repository.remotedata.RemoteRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends AndroidViewModel implements LocationProvider.OnLocationChangeListener {

    private static String TAG = WeatherViewModel.class.getSimpleName();

    private MutableLiveData mNextTenDaysForecast = new MutableLiveData<List<Daily>>();

    private RemoteRepository mRemoteRepository;
    private LocalRoomRepo mLocalRepository;
    private LocationProvider mLocationProvider;

    /*Live weather info*/
    public MutableLiveData<CharSequence> mCityName = new MutableLiveData();
    public MutableLiveData<CharSequence> mDate = new MutableLiveData();
    public MutableLiveData<CharSequence> mWeather = new MutableLiveData();
    public MutableLiveData<CharSequence> mSunRiseAndSet = new MutableLiveData();
    public MutableLiveData<CharSequence> mTemp = new MutableLiveData();
    public MutableLiveData<CharSequence> mTempMin = new MutableLiveData();
    public MutableLiveData<CharSequence> mTempMax = new MutableLiveData();
    public MutableLiveData<CharSequence> mPressure = new MutableLiveData();
    public MutableLiveData<CharSequence> mHumidity = new MutableLiveData();

    /*Tomorow Weather Info*/
    public MutableLiveData<CharSequence> mTomorrowDate = new MutableLiveData();
    public MutableLiveData<CharSequence> mTomorrowSunRiseAndSet = new MutableLiveData();
    public MutableLiveData<CharSequence> mTomorrowTempMin = new MutableLiveData();
    public MutableLiveData<CharSequence> mTomorrowTempMax = new MutableLiveData();
    public MutableLiveData<CharSequence> mTomorrowPressure = new MutableLiveData();
    public MutableLiveData<CharSequence> mTomorrowHumidity = new MutableLiveData();

    Callback mTodayforcastCallback = new Callback<LiveWeatherPojo>() {
        @Override
        public void onResponse(Call<LiveWeatherPojo> call, Response<LiveWeatherPojo> response) {

            Log.e(TAG, "WeatherViewModel Today Response Code: " + response.code());

            if (response.isSuccessful()) {
                Log.i(TAG, "Today WeatherViewModel Response Json " + new Gson().toJson(response.body()));
                LiveWeatherPojo liveWeather = response.body();

                mCityName.postValue(liveWeather.getName());
                mCityName.setValue(liveWeather.getName());
                mDate.setValue(Common.getDateTimeFromMilleSec(liveWeather.getDt()));
                mWeather.setValue(new StringBuilder(liveWeather.getWeather().get(0).getMain())
                        .append(" - ").append(liveWeather.getWeather().get(0).getDescription()));
                mSunRiseAndSet.setValue("SunRise: "
                        + Common.getTimeFromMilleSec((liveWeather.getSys().getSunrise()))
                        + " Sunset: " + Common.getTimeFromMilleSec(liveWeather.getSys().getSunset()));
                mTemp.setValue(new StringBuilder("Temparature: ")
                        .append(liveWeather.getMain().getTemp()).append(" °C"));
                mTempMin.setValue(new StringBuilder("Min Temparature: ")
                        .append(liveWeather.getMain().getTempMin()).append(" °C"));
                mTempMax.setValue("Max Temparature: "
                        + String.valueOf(liveWeather.getMain().getTempMax()) + " °C");
                mHumidity.setValue("Humidity: " + liveWeather.getMain().getHumidity() + "%");
                mPressure.setValue("Presure: " + liveWeather.getMain().getPressure() + " mbar");
            } else {
                Log.e(TAG, "WeatherViewModel Today Response Code: " + response.code());
            }
        }

        @Override
        public void onFailure(Call<LiveWeatherPojo> call, Throwable t) {
            Log.e(TAG, t.toString());

        }
    };

    Callback mForcastCallback = new Callback<Forecast>() {
        @Override
        public void onResponse(Call<Forecast> call, Response<Forecast> response) {
            Log.i(TAG, "Tomorrow WeatherViewModel Response Json "
                    + new Gson().toJson(response.body()));

            if (response.isSuccessful()) {
                mNextTenDaysForecast.setValue(response.body().getDaily());
                Daily tomorrow = response.body().getDaily().get(1);
                mTomorrowDate.setValue(Common.getDateFromMilleSec(tomorrow.getDt()));
                mTomorrowSunRiseAndSet.setValue("SunRise: "
                        + Common.getTimeFromMilleSec(tomorrow.getSunrise())
                        + " SunSet: "
                        + Common.getTimeFromMilleSec(tomorrow.getSunset()));
                mTomorrowTempMin.setValue("Minimum Temparature: " + tomorrow.getTemp().getMin());
                mTomorrowTempMax.setValue("Maximum Temparature: " + tomorrow.getTemp().getMax());
                mTomorrowHumidity.setValue("Humidity: " + tomorrow.getHumidity());
                mTomorrowPressure.setValue("Presure: " + tomorrow.getPressure());
            } else {
                Log.e(TAG, "Tomorrow WeatherViewModel Response is Sucessful: "
                        + response.isSuccessful() + " Code: " + response.code());
            }
        }

        @Override
        public void onFailure(Call<Forecast> call, Throwable t) {
            Log.e("Error", t.toString());

        }
    };

    public WeatherViewModel(@NonNull Application application) {
        super(application);

        mRemoteRepository = RemoteRepository.getInstance(application);
        mLocalRepository = new LocalRoomRepo(application);
        mLocationProvider = LocationProvider.getInstance(this);

        mLocationProvider.startListening();
        getWeatherByLocation(null);
    }

    public LiveData<List<Daily>> getNextTenDaysForecast() {
        return mNextTenDaysForecast;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void getWeatherByLocation(Location location) {
        if (location != null) {
            mRemoteRepository.getLiveWeatherByLocation(location, mTodayforcastCallback);
            mRemoteRepository.getTomorrowForecastByLocation(location, mForcastCallback);
        } else {
            location = mLocationProvider.getLocation();
            if (location != null) {
                mRemoteRepository.getLiveWeatherByLocation(location, mTodayforcastCallback);
                mRemoteRepository.getTomorrowForecastByLocation(location, mForcastCallback);
            } else {
                mLocalRepository.getLatestLocation(new LocalRoomRepo.OnLatestLocationRoomRequest() {
                    @Override
                    public void latestRoomLocation(LatLon latLon) {
                        if (latLon != null) {
                            mRemoteRepository.getLiveWeatherByLocation(latLon.getLatitude(), latLon.getLongitude(), mTodayforcastCallback);
                            mRemoteRepository.getTomorrowForecastByLocation(latLon.getLatitude(), latLon.getLongitude(), mForcastCallback);
                        }
                    }
                });
            }
        }
    }

    public void stopListeningLocationChanage() {
        mLocationProvider.stopListening();
    }

    public void startListingLocationChange() {
        mLocationProvider.startListening();
    }

    public void getWeatherByCityName(String cityName) {
        Log.e(TAG, "searchWeatherByCityName");
        Toast.makeText(getApplication(), "Weather by city name", Toast.LENGTH_LONG).show();
        mLocationProvider.stopListening();//mLocationManager.removeUpdates(locationListener);
        if (cityName != null && !cityName.isEmpty()) {
            mRemoteRepository.getLiveWeatherByCity(cityName, mTodayforcastCallback);
            Toast.makeText(getApplication(), "City Location", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChange(Location location) {
        Toast.makeText(getApplication(), "New Location", Toast.LENGTH_LONG).show();
        getWeatherByLocation(location);

        if(location != null) {
            mLocalRepository.insertLocation(new LatLon(String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()),
                    String.valueOf(System.currentTimeMillis())));
        }
    }
}
