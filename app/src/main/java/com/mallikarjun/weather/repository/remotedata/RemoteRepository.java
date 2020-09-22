package com.mallikarjun.weather.repository.remotedata;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.mallikarjun.weather.common.Common;
import com.mallikarjun.weather.repository.Api;
import com.mallikarjun.weather.repository.pogos.LiveWeatherPojo;
import com.mallikarjun.weather.repository.pogos.forecastpojo.Forecast;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteRepository {

    private static String TAG = RemoteRepository.class.getSimpleName();
    private static RemoteRepository instance;

    private Api api;

    private RemoteRepository(final Context context) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(context.getCacheDir(), 5 * 1024 * 1024))
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (Common.isConnected(context))
                            request = request.newBuilder().header("Cache-Control",
                                    "public, max-age=" + 60).build();
                        else
                            request = request.newBuilder().header("Cache-Control",
                                    "public, only-if-cached, max-stale=" + 60 * 60 * 24)
                                    .build();
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        api = retrofit.create(Api.class);
    }

    public static synchronized RemoteRepository getInstance(Context context) {
        if (instance == null) {
            instance = new RemoteRepository(context);
        }
        return instance;
    }

    public void getLiveWeatherByCity(String cityName, Callback callback) {
        Call<LiveWeatherPojo> weather = api.getLiveWeatherByCity(cityName, "metric", Api.KEY);
        weather.enqueue(callback);
    }

    public void getForecastByCity(String cityName, Callback callback) {
        Call<Forecast> weather = api.getForecastByCity(cityName, "current,minutely,hourly",
                "metric", Api.KEY);

        weather.enqueue(callback);
    }

    public void getLiveWeatherByLocation(Location location, Callback callback) {
        if (location != null) {
            Call<LiveWeatherPojo> weather =
                    api.getLiveWeatherByLocation(String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()), "metric", Api.KEY);
            weather.enqueue(callback);
        } else {
            Log.e(TAG, "getWeatherByLatAndLon LOCATION is null");
        }
    }

    public void getTomorrowForecastByLocation(Location location, Callback callback) {
        Log.e(TAG, "getTomorrowForecast");
        if (location != null) {
            Call<Forecast> weather = api.getTomorrowForecastByLocation(String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()), "current,minutely,hourly", "metric", Api.KEY);

            weather.enqueue(callback);
        } else {
            Log.e(TAG, "getTomorrowForecast LOCATION is null");
        }
    }

    public void getLiveWeatherByLocation(String lat, String lon, Callback callback) {
        Call<LiveWeatherPojo> weather =
                api.getLiveWeatherByLocation(lat, lon, "metric", Api.KEY);
        weather.enqueue(callback);
    }

    public void getTomorrowForecastByLocation(String lat, String lon, Callback callback) {
        Call<Forecast> weather = api.getTomorrowForecastByLocation(lat, lon, "current,minutely,hourly", "metric", Api.KEY);

        weather.enqueue(callback);

    }

}
