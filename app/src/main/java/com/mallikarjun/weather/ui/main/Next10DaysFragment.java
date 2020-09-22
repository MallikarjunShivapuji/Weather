package com.mallikarjun.weather.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mallikarjun.weather.R;
import com.mallikarjun.weather.adapter.WeatherListAdapter;
import com.mallikarjun.weather.repository.pogos.forecastpojo.Daily;
import com.mallikarjun.weather.viewmodel.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public class Next10DaysFragment extends Fragment {

    private static String TAG = Next10DaysFragment.class.getSimpleName();

    private WeatherViewModel pageViewModel;

    private static Next10DaysFragment thisFragment;

    public static synchronized Next10DaysFragment getInstance() {

        if (thisFragment == null) {
            thisFragment = new Next10DaysFragment();
        }
        return thisFragment;
    }

    private Context mContext;

    private WeatherListAdapter mAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_next10_days, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.listweather);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new WeatherListAdapter(mContext, new ArrayList<Daily>());
        recyclerView.setAdapter(mAdapter);

        pageViewModel.getNextTenDaysForecast().observe(getViewLifecycleOwner(), new Observer<List<Daily>>() {
            @Override
            public void onChanged(List<Daily> dailies) {
                mAdapter.updateList(dailies);
            }
        });

        setHasOptionsMenu(false);
        return root;
    }
}
