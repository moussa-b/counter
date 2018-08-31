package com.mbo.counter.counter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.addeditcounter.AddEditCounterActivity;
import com.mbo.counter.commons.Utils;
import com.mbo.counter.statistics.StatisticsActivity;

import static android.text.TextUtils.isEmpty;
import static com.mbo.counter.addeditcounter.AddEditCounterFragment.ARGUMENT_EDIT_COUNTER_ID;
import static com.mbo.counter.statistics.StatisticsFragment.ARGUMENT_STATISTICS_COUNTER_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class CounterFragment extends Fragment implements CounterContract.View
{
    public static final String ARGUMENT_COUNTER_ID = "COUNTER_ID";
    public static int progressBarRotation = 90;
    public static final int ROTATION_ANGLE = 10;

    private TextView mName, mCount, mLimit;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.counter_fragment, container, false);

        mName = root.findViewById(R.id.counter_name_text_view);
        mCount = root.findViewById(R.id.counter_count_text_view);
        mLimit = root.findViewById(R.id.counter_limit_text_view);
        Button increaseButton = root.findViewById(R.id.counter_increase_button);
        Button decreaseButton = root.findViewById(R.id.counter_decrease_button);
        Button resetButton = root.findViewById(R.id.counter_reset_button);
        mProgressBar = root.findViewById(R.id.counter_progress_bar);

        increaseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int count = mPresenter.incrementCounter();
                setCount(count);
                int limit = mPresenter.getLimit();
                if (limit != 0)
                    setProgression(limit, count);
                else
                    rotateProgressBar(-ROTATION_ANGLE);

                MediaPlayer increaseSound = Utils.getIncreaseMediaPlayer(getContext());
                increaseSound.start();
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int count = mPresenter.decrementCounter();
                setCount(count);
                int limit = mPresenter.getLimit();
                if (limit != 0)
                    setProgression(limit, count);
                else
                    rotateProgressBar(ROTATION_ANGLE);

                MediaPlayer decreaseSound = Utils.getDecreaseMediaPlayer(getContext());
                decreaseSound.start();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPresenter.resetCounter();
                setCount(0);
                if (mPresenter.getLimit() != 0)
                    setProgression(mPresenter.getLimit(), 0);
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
        if (!isEmpty(name))
            mName.setText(name);
        else
            mName.setVisibility(View.GONE);
    }

    @Override
    public void setLimit(int limit)
    {
        if (limit != 0)
            mLimit.setText(String.format("%s : %s", getString(R.string.objective), String.valueOf(limit)));
        else
            mLimit.setVisibility(View.GONE);
    }

    @Override
    public void rotateProgressBar(int angle)
    {
        progressBarRotation -= angle;
        mProgressBar.setRotation(progressBarRotation);
    }

    @Override
    public void setCount(int count)
    {
        mCount.setText(String.valueOf(count));
    }

    @Override
    public void setProgression(int limit, int count)
    {
        if (limit == 0)
            mProgressBar.setProgress(100);
        else
        {
            float progression = 100 * (float) count / (float) limit;
            mProgressBar.setProgress((int) progression);
        }
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
        Intent intent = new Intent(getContext(), StatisticsActivity.class);
        intent.putExtra(ARGUMENT_STATISTICS_COUNTER_ID, mPresenter.getCounterId());
        startActivity(intent);
    }

    @Override
    public void showCountersList()
    {
        Activity activity = getActivity();
        if (activity != null)
        {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }
}
