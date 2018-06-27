package com.mbo.counter.counter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.addeditcounter.AddEditCounterActivity;
import com.mbo.counter.settings.SettingsActivity;

import static com.mbo.counter.addeditcounter.AddEditCounterFragment.ARGUMENT_EDIT_COUNTER_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class CounterFragment extends Fragment implements CounterContract.View
{
    public static final String ARGUMENT_COUNTER_ID = "COUNTER_ID";

    private TextView mName, mCount, mTotal;

    private ProgressBar mProgressBar;

    private CounterContract.Presenter mPresenter;

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

        mName = (TextView) root.findViewById(R.id.counter_name_text_view);
        mCount = (TextView) root.findViewById(R.id.counter_count_text_view);
        mTotal = (TextView) root.findViewById(R.id.counter_total_text_view);
        mProgressBar = (ProgressBar) root.findViewById(R.id.counter_progress_bar);
        mProgressBar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int count = mPresenter.incrementCounter();
                setCount(count);
                setProgression(mPresenter.getTotal(), count);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_edit:
                showEditCounter();
                break;
            case R.id.action_statistics:
                showCounterStatistics();
                break;
            case android.R.id.home:
                showCountersList();
                break;
        }
        return true;
    }

    @Override
    public void setPresenter(CounterContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void setName(String name)
    {
        mName.setText(name);
    }

    @Override
    public void setTotal(int total)
    {
        mTotal.setText(getString(R.string.objective) + " : " + String.valueOf(total));
    }

    @Override
    public void setCount(int count)
    {
        mCount.setText(String.valueOf(count));
    }

    @Override
    public void setProgression(int total, int count)
    {
        float progession = 100 * (float) count / (float) total;
        mProgressBar.setProgress((int) progession);
    }

    @Override
    public void showEditCounter()
    {
        Intent intent = new Intent(getContext(), AddEditCounterActivity.class);
        intent.putExtra(ARGUMENT_EDIT_COUNTER_ID, mPresenter.getCounterId());
        startActivityForResult(intent, AddEditCounterActivity.REQUEST_ADD_COUNTER);
    }

    @Override
    public void showCounterStatistics()
    {
        startActivity(new Intent(getContext(), SettingsActivity.class));
    }

    @Override
    public void showCountersList()
    {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
