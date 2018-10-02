package com.bdzapps.counterpp.countersettings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mbo.counter.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CounterSettingsFragment extends Fragment implements CounterSettingsContract.View
{

    public CounterSettingsFragment()
    {
    }

    public static CounterSettingsFragment newInstance()
    {
        return new CounterSettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.counter_settings_fragment, container, false);
        return root;
    }

    @Override
    public void setPresenter(CounterSettingsContract.Presenter presenter)
    {

    }

}
