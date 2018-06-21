package com.mbo.counter.statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mbo.counter.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class StatisticsFragment extends Fragment implements StatisticsContract.View
{

    public StatisticsFragment()
    {
    }

    public static StatisticsFragment newInstance()
    {
        return new StatisticsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.statistics_fragment, container, false);
        return root;
    }

    @Override
    public void setPresenter(StatisticsContract.Presenter presenter)
    {

    }

}
