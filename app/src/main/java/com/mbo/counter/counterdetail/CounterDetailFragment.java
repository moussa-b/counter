package com.mbo.counter.counterdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mbo.counter.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CounterDetailFragment extends Fragment implements CounterDetailContract.View
{

    public CounterDetailFragment()
    {
    }

    public static CounterDetailFragment newInstance()
    {
        return new CounterDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.counter_detail_fragment, container, false);
        return root;
    }

    @Override
    public void setPresenter(CounterDetailContract.Presenter presenter)
    {

    }

}
