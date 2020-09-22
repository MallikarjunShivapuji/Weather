package com.mallikarjun.weather.notification;

import android.content.Context;
import android.util.Log;

import com.mallikarjun.weather.common.Common;
import com.mallikarjun.weather.repository.localrepository.LatLon;
import com.mallikarjun.weather.repository.localrepository.LocalRoomRepo;
import com.mallikarjun.weather.repository.pogos.LiveWeatherPojo;
import com.mallikarjun.weather.repository.remotedata.RemoteRepository;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PeriodicNotification extends Worker {

    private static String TAG = PeriodicNotification.class.getSimpleName();

    private static Context mContext;

    public PeriodicNotification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.e(TAG, "Constructor PeriodicNotification");
    }

    private static PeriodicWorkRequest getOwnWorkRequest() {
        return new PeriodicWorkRequest.Builder(
                PeriodicNotification.class, 1, TimeUnit.HOURS).build();
    }

    public static void enqueueSelf() {
        WorkManager.getInstance(mContext).enqueueUniquePeriodicWork("", ExistingPeriodicWorkPolicy.KEEP, getOwnWorkRequest());
        //WorkManager.getInstance().enqueueUniquePeriodicWork( uniqueWorkName, ExistingPeriodicWorkPolicy.KEEP, getOwnWorkRequest() );
    }

    @NonNull
    @Override
    public Result doWork() {
        new LocalRoomRepo(getApplicationContext()).getLatestLocation(
                new LocalRoomRepo.OnLatestLocationRoomRequest() {
                    @Override
                    public void latestRoomLocation(LatLon latLon) {
                        if (latLon != null) {

                            RemoteRepository.getInstance(getApplicationContext())
                                    .getLiveWeatherByLocation(latLon.getLatitude(), latLon.getLongitude(), new Callback() {
                                        @Override
                                        public void onResponse(Call call, Response response) {

                                            if (response.isSuccessful()) {
                                                LiveWeatherPojo profile = (LiveWeatherPojo) response.body();

                                                Common.showNotification(getApplicationContext(),
                                                        profile.getName(),
                                                        profile.getWeather().get(0).getDescription());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call call, Throwable t) {

                                        }
                                    });
                        }
                    }
                });

        return Result.success();
    }
}
