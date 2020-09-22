package com.mallikarjun.weather.repository;

import com.mallikarjun.weather.repository.pogos.LiveWeatherPojo;
import com.mallikarjun.weather.repository.pogos.forecastpojo.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    String KEY = "0a0dd51f36507e68d52655e95c27bd49";
    String BASE_URI = "https://api.openweathermap.org/data/2.5/";

    @GET("weather")
    Call<LiveWeatherPojo> getLiveWeatherByCity(@Query("q") String cityName,
                                               @Query("units") String unit,
                                               @Query("appid") String key);

    @GET("onecall")
    Call<Forecast> getForecastByCity(@Query("q") String city,
                                     @Query("exclude") String exclude,
                                     @Query("units") String unit,
                                     @Query("appid") String key);

    @GET("weather")
    Call<LiveWeatherPojo> getLiveWeatherByLocation(@Query("lat") String lat,
                                                   @Query("lon") String lon,
                                                   @Query("units") String unit,
                                                   @Query("appid") String key);

    @GET("onecall")
    Call<Forecast> getTomorrowForecastByLocation(@Query("lat") String lat,
                                                 @Query("lon") String lon,
                                                 @Query("exclude") String exclude,
                                                 @Query("units") String unit,
                                                 @Query("appid") String key);

}


