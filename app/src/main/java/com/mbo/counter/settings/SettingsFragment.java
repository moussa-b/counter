package com.mbo.counter.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mbo.counter.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends Fragment implements SettingsContract.View {

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_activity, container, false);
        return root;
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {

    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

}
