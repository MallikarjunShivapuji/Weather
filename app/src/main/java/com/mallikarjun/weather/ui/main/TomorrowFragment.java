package com.mallikarjun.weather.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mallikarjun.weather.R;
import com.mallikarjun.weather.databinding.FragmentTomorrowBinding;
import com.mallikarjun.weather.viewmodel.WeatherViewModel;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TomorrowFragment extends Fragment {

    private WeatherViewModel mWetherViewModel;

    private static TomorrowFragment thisFragment;

    public static synchronized TomorrowFragment getInstance() {

        if(thisFragment == null) {
            thisFragment = new TomorrowFragment();
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

        FragmentTomorrowBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tomorrow, container, false);
        binding.setLifecycleOwner(this);
        binding.setVm(mWetherViewModel);

        setHasOptionsMenu(false);

        return binding.getRoot();
    }
}