package com.mallikarjun.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mallikarjun.weather.R;
import com.mallikarjun.weather.common.Common;
import com.mallikarjun.weather.repository.pogos.forecastpojo.Daily;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Daily> mList;

    public WeatherListAdapter(Context context, List<Daily> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        WeatherViewHolder viewHolder = new WeatherViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WeatherViewHolder viewHolder = (WeatherViewHolder)holder;
        Daily day = mList.get(position);
        viewHolder.dateTime.setText(Common.getDateFromMilleSec(day.getDt()));
        viewHolder.rain.setText(new StringBuilder(day.getWeather().get(0).getMain()).append(": ").append(day.getWeather().get(0).getDescription()));

        Picasso.with(mContext)
                .load(new StringBuilder("https://openweathermap.org/img/wn/").append(day.getWeather().get(0).getIcon()).append("@2x.png").toString())
                .into(viewHolder.image);

        viewHolder.sunRiseAndset.setText(new StringBuilder("Sunrise : ")
                .append(Common.getTimeFromMilleSec(day.getSunrise()))
                .append(" ")
                .append("Sunset : ")
                .append(Common.getTimeFromMilleSec(day.getSunset())));
        viewHolder.temprature.setText(new StringBuilder("Temparature:  ").append(day.getTemp().getMax()).append("°C - ").append(day.getTemp().getMin()).append("°C"));
        viewHolder.humidity.setText(new StringBuilder("Humidity: ").append(day.getHumidity()).append("%"));
        viewHolder.pressure.setText(new StringBuilder("Pressure: ").append(day.getPressure()).append(" mbar"));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView dateTime;
        TextView rain;
        ImageView image;
        TextView sunRiseAndset;
        TextView temprature;
        TextView humidity;
        TextView pressure;


        public WeatherViewHolder(View viewItem) {
            super(viewItem);
            dateTime = viewItem.findViewById(R.id.tvDateTime);
            rain = viewItem.findViewById(R.id.tvRain);
            image = viewItem.findViewById(R.id.image);
            sunRiseAndset = viewItem.findViewById(R.id.tv_sun);
            temprature = viewItem.findViewById(R.id.tvTemp);
            humidity = viewItem.findViewById(R.id.tvHumidity);
            pressure = viewItem.findViewById(R.id.tvPressure);

        }

    }

    public void updateList(List list) {
        mList = list;
        notifyDataSetChanged();
    }
}
