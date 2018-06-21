package com.mbo.counter.counter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mbo.counter.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CounterFragment extends Fragment implements CounterContract.View
{

    public CounterFragment()
    {
    }

    public static CounterFragment newInstance()
    {
        return new CounterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.counter_fragment, container, false);
        return root;
    }

    @Override
    public void setPresenter(CounterContract.Presenter presenter)
    {

    }

}
