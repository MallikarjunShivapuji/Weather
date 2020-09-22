package com.mallikarjun.weather.common;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioAttributes;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mallikarjun.weather.R;
import com.mallikarjun.weather.WeatherActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.core.app.NotificationCompat;

public class Common {

    private static String TAG = Common.class.getSimpleName();

    public static String getDateTimeFromMilleSec(long milleseconds) {
        Date date = new Date(milleseconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy HH:mm");
        return sdf.format(date);
    }

    public static String getTimeFromMilleSec(long milleseconds) {
        Date date = new Date(milleseconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    public static String getDateFromMilleSec(long milleseconds) {
        Date date = new Date(milleseconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy");
        return sdf.format(date);
    }

    private static String NOTIFICATION_CHANNEL_ID = "102";
    private static int NOTIFICATION_ID = 101;

    public static void showNotification(Context context, String title, String subTitle) {

        Intent notificationIntent = new Intent(context, WeatherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, notificationIntent, 0);

       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,
                NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(subTitle)
                .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new
                    NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

            notificationChannel.enableVibration(false);
            //notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
            //notificationChannel.setSound(sound , audioAttributes) ;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public static Boolean isConnected(Context context) {
        Boolean isConnected = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting())
            isConnected = true;
        return isConnected;
    }


}
