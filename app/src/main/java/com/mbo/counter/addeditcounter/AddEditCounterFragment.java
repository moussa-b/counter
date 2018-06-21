package com.mbo.counter.addeditcounter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mbo.counter.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditCounterFragment extends Fragment implements AddEditCounterContract.View
{

    public AddEditCounterFragment()
    {
    }

    public static AddEditCounterFragment newInstance()
    {
        return new AddEditCounterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.add_edit_counter_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_counter_done);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return root;
    }

    @Override
    public void setPresenter(AddEditCounterContract.Presenter presenter)
    {

    }

}
