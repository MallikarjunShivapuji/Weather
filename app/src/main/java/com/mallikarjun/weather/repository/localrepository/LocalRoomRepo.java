package com.mallikarjun.weather.repository.localrepository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;

public class LocalRoomRepo {
    LocationDao mLocationDao;

    public LocalRoomRepo(Context context) {
        LocationDB locationDB = LocationDB.getInstance(context);
        mLocationDao = locationDB.locationDao();
    }

    public void insertLocation(LatLon location) {
        new InsertLocationAsync().execute(location);
    }

    public void getCount(final OnRoomLocationCountRequest callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callback.roomLocationCount(mLocationDao.getcount());
            }
        }).start();
    }

    public interface OnRoomLocationCountRequest {
        void roomLocationCount(int count);

    }

    public void getLatestLocation(final OnLatestLocationRoomRequest callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callBack.latestRoomLocation(mLocationDao.getLatestLocation());
            }
        }).start();
    }

    public interface OnLatestLocationRoomRequest {
        void latestRoomLocation(LatLon latLon);
    }

    class InsertLocationAsync extends AsyncTask<LatLon, Void, Void> {

        @Override
        protected Void doInBackground(LatLon... latLons) {

            mLocationDao.insertLocation(latLons[0]);

            Log.e("Mallik", "Inserting Location:  " + latLons.toString());
            return null;
        }
    }
}

