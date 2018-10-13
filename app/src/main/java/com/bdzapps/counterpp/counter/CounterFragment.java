package com.bdzapps.counterpp.counter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bdzapps.counterpp.addeditcounter.AddEditCounterActivity;
import com.bdzapps.counterpp.commons.Utils;
import com.bdzapps.counterpp.counterstatistics.CounterStatisticsActivity;
import com.mbo.counter.R;

import static android.text.TextUtils.isEmpty;
import static com.bdzapps.counterpp.addeditcounter.AddEditCounterFragment.ARGUMENT_EDIT_COUNTER_ID;
import static com.bdzapps.counterpp.counterstatistics.CounterStatisticsFragment.ARGUMENT_STATISTICS_COUNTER_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class CounterFragment extends Fragment implements CounterContract.View
{
    public static final String ARGUMENT_COUNTER_ID = "COUNTER_ID";
    public static final int ROTATION_ANGLE = 10;
    public static int progressBarRotation = 90;
    private TextView mName, mCount, mLimit;
    private boolean isDrawableSet; // Required because issue when setting drawable twice

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
                incrementCounter();
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                decrementCounter();
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
    public void setProgressBarDrawable(int limit)
    {
        if (limit == 0 && !isDrawableSet)
        {
            mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_circle_gradient));
            isDrawableSet = true;
        }
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
    public void decrementCounter()
    {
        int count = mPresenter.decrementCounter();
        setCount(count);
        int limit = mPresenter.getLimit();
        if (limit != 0)
            setProgression(limit, count);
        else
            rotateProgressBar(ROTATION_ANGLE);

        Context context = getContext();
        if (context != null)
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            // Handle play sound on increment
            boolean isSoundEnabled = sharedPreferences.getBoolean(getString(R.string.key_activate_sound), false);
            if (isSoundEnabled)
            {
                MediaPlayer decreaseSound = Utils.getDecreaseMediaPlayer(getContext());
                decreaseSound.start();
            }

            // Handle vibrate on increment
            boolean isVibratorEnabled = sharedPreferences.getBoolean(getString(R.string.key_activate_vibrator), false);
            if (isVibratorEnabled)
            {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null)
                {
                    if (Build.VERSION.SDK_INT >= 26)
                        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                    else
                        vibrator.vibrate(100);
                }
            }
        }
    }

    @Override
    public void incrementCounter()
    {
        int count = mPresenter.incrementCounter();
        setCount(count);
        int limit = mPresenter.getLimit();
        if (limit != 0)
            setProgression(limit, count);
        else
            rotateProgressBar(-ROTATION_ANGLE);

        Context context = getContext();
        if (context != null)
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            // Handle play sound on increment
            boolean isSoundEnabled = sharedPreferences.getBoolean(getString(R.string.key_activate_sound), false);
            if (isSoundEnabled)
            {
                MediaPlayer increaseSound = Utils.getIncreaseMediaPlayer(getContext());
                increaseSound.start();
            }

            // Handle vibrate on increment
            boolean isVibratorEnabled = sharedPreferences.getBoolean(getString(R.string.key_activate_vibrator), false);
            if (isVibratorEnabled)
            {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null)
                {
                    if (Build.VERSION.SDK_INT >= 26)
                        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                    else
                        vibrator.vibrate(100);
                }
            }
        }
    }

    @Override
    public void rotateProgressBar(int angle)
    {
        progressBarRotation -= angle;
        mProgressBar.setRotation(progressBarRotation);
    }

    @Override
    public void setColor(String color)
    {
        if (color != null)
            mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color)));
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
        {
            mProgressBar.setProgress(100);
            rotateProgressBar(10);
        }
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
        Intent intent = new Intent(getContext(), CounterStatisticsActivity.class);
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
