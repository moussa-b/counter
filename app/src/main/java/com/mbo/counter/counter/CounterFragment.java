package com.mbo.counter.counter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mbo.counter.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CounterFragment extends Fragment implements CounterContract.View
{
    public static final String ARGUMENT_COUNTER_ID = "COUNTER_ID";

    private TextView mName, mCurrentCount, mCount;

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
        mCurrentCount = (TextView) root.findViewById(R.id.counter_count_text_view);
        mCount = (TextView) root.findViewById(R.id.count_text_view);
        mProgressBar = (ProgressBar) root.findViewById(R.id.counter_progress_bar);

        mCurrentCount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int currentCount = mPresenter.incrementCounter();
                setCurrentCount(currentCount);
                setProgression(mPresenter.getCount(), currentCount);
            }
        });
        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
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
    public void setCount(int count)
    {
        mCount.setText(getString(R.string.objective) + " : " + String.valueOf(count));
    }

    @Override
    public void setCurrentCount(int currentCount)
    {
        mCurrentCount.setText(String.valueOf(currentCount));
    }

    @Override
    public void setProgression(int count, int currentCount)
    {
        float progession = 100 * (float) currentCount / (float) count;
        mProgressBar.setProgress((int)progession);
    }
}
