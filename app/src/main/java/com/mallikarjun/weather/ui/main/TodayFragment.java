package com.mallikarjun.weather.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.mallikarjun.weather.R;
import com.mallikarjun.weather.databinding.FragmentTodayBinding;
import com.mallikarjun.weather.viewmodel.WeatherViewModel;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class TodayFragment extends Fragment {

    private WeatherViewModel mWetherViewModel;

    private static TodayFragment thisFragment;

    public static synchronized TodayFragment getInstance() {
        if (thisFragment == null) {
            thisFragment = new TodayFragment();
        }
        return thisFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWetherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        FragmentTodayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_today, null, false);

        binding.setLifecycleOwner(this);
        binding.setVm(mWetherViewModel);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_searchview);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setQueryHint("Search by city name");

        /*OnClosing SearchView*/
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mWetherViewModel.startListingLocationChange();
                mWetherViewModel.getWeatherByLocation(null);
                return false;
            }
        });
        /*OnClick on SearchView*/
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWetherViewModel.stopListeningLocationChanage();
                mWetherViewModel.getWeatherByCityName(null);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String cityName) {
                mWetherViewModel.getWeatherByCityName(cityName);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

}