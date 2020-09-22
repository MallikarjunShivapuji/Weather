package com.mallikarjun.weather.repository.localrepository;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "location")
public class LatLon {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    int id;
    @ColumnInfo(name = "lat")
    String lat;

    @ColumnInfo(name = "lon")
    String lon;

    @ColumnInfo(name = "datetime")
    String datetime;

    public LatLon(int id, String lat, String lon, String datetime) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "LatLon{" +
                "id=" + id +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }

    public String getLatitude() {
        return lat;
    }

    public String getLongitude() {
        return lon;
    }

    @Ignore
    public LatLon(String lat, String lon, String datetime) {
        this.lat = lat;
        this.lon = lon;
        this.datetime = datetime;
    }
}
