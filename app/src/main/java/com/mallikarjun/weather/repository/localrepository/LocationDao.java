package com.mallikarjun.weather.repository.localrepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM location")
    List<LatLon> getAllLocations();

    @Insert
    void insertLocation(LatLon location);

    @Query("SELECT COUNT(*) FROM location")
    int getcount();

    @Query("SELECT * FROM location ORDER BY id DESC LIMIT 1")
    LatLon getLatestLocation();

}
