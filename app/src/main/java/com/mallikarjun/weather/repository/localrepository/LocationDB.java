package com.mallikarjun.weather.repository.localrepository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.work.Constraints;

@Database(entities = LatLon.class, exportSchema = false, version = 1)
public abstract class LocationDB  extends RoomDatabase {

    private static final String DB_NAME = "location_db";

    private static LocationDB instance;

    public static synchronized LocationDB getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context, LocationDB.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

    public abstract LocationDao locationDao();

}
