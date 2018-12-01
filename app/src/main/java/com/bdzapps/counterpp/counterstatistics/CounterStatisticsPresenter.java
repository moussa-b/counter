package com.bdzapps.counterpp.counterstatistics;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.bdzapps.counterpp.commons.Utils;
import com.bdzapps.counterpp.data.model.Statistics;
import com.bdzapps.counterpp.data.model.StatisticsType;
import com.bdzapps.counterpp.data.source.CounterDataSource;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mbo.counter.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CounterStatisticsPresenter implements CounterStatisticsContract.Presenter
{
    private static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L;
    static final long ONE_WEEK_MILLIS = 7 * ONE_DAY_MILLIS;
    static final long TWO_WEEK_MILLIS = 2 * 7 * ONE_DAY_MILLIS;
    static final long ONE_MONTH_MILLIS = 30 * ONE_DAY_MILLIS;
    static final long THREE_MONTH_MILLIS = 3 * 30 * ONE_DAY_MILLIS;
    static final long SIX_MONTH_MILLIS = 6 * 30 * ONE_DAY_MILLIS;
    @NonNull
    private final CounterStatisticsContract.View mStatisticsView;
    @NonNull
    private final CounterDataSource mCounterDataSource;
    private long mCurrentPeriod;
    private long mStartTimeStamp;
    private long mEndTimeStamp;
    private long mInterval;
    private int mCounterId;

    public CounterStatisticsPresenter(int counterId, @NonNull CounterDataSource counterDataSource, @NonNull CounterStatisticsContract.View statisticsView)
    {
        this.mCounterId = counterId;
        this.mStatisticsView = statisticsView;
        this.mCounterDataSource = counterDataSource;
        mStatisticsView.setPresenter(this);
    }


    @Override
    public void start()
    {
        // mCounterDataSource.generateRandomStatistics(mCounterId, StatisticsType.RESET, 31, 20);
        // mCounterDataSource.generateRandomStatistics(mCounterId, StatisticsType.INCREMENT, 31, 180);
        // mCounterDataSource.generateRandomStatistics(mCounterId, StatisticsType.DECREMENT, 31, 60);
        updateStatistics(ONE_WEEK_MILLIS);
    }

    @Override
    public void loadStatistics()
    {
        mCounterDataSource.getStatisticsInInterval(mCounterId, mStartTimeStamp, mEndTimeStamp, new CounterDataSource.LoadStatisticsCallback()
        {
            @Override
            public void onStatisticsLoaded(List<Statistics> statistics)
            {
                processStatistics(statistics);
            }

            @Override
            public void onDataNotAvailable()
            {
                processEmptyStatistics();
            }
        });
    }

    @Override
    public void updateStatistics(long period)
    {
        if (mCurrentPeriod != period) // Avoid making several times the same plot
        {
            mCurrentPeriod = period;
            Calendar cal = Calendar.getInstance();
            if (period == ONE_WEEK_MILLIS)
            {
                cal.setTime(new Date()); // Today
                cal.add(Calendar.DATE, 1); //End of current day is considered as beginning of next day
                mEndTimeStamp = Utils.atStartOfDay(cal);
                cal.add(Calendar.DATE, -7);
                mStartTimeStamp = Utils.atStartOfDay(cal);
                mInterval = ONE_DAY_MILLIS;
            }
            else if (period == TWO_WEEK_MILLIS)
            {
                cal.setTime(new Date()); // Today
                cal.add(Calendar.DATE, 1); //End of current day is considered as beginning of next day
                mEndTimeStamp = Utils.atStartOfDay(cal);
                cal.add(Calendar.DATE, -14);
                mStartTimeStamp = Utils.atStartOfDay(cal);
                mInterval = ONE_DAY_MILLIS;
            }
            else if (period == ONE_MONTH_MILLIS)
            {
                cal.setTime(new Date()); // Today
                cal.add(Calendar.DATE, 1); //End of current day is considered as beginning of next day
                mEndTimeStamp = Utils.atStartOfDay(cal);
                cal.add(Calendar.DATE, -30);
                mStartTimeStamp = Utils.atStartOfDay(cal);
                mInterval = ONE_WEEK_MILLIS;
            }
            else if (period == THREE_MONTH_MILLIS)
            {
                cal.setTime(new Date()); // Today
                cal.add(Calendar.DATE, 1); //End of current day is considered as beginning of next day
                mEndTimeStamp = Utils.atStartOfDay(cal);
                cal.add(Calendar.DATE, -90);
                mStartTimeStamp = Utils.atStartOfDay(cal);
                mInterval = TWO_WEEK_MILLIS;
            }
            else if (period == SIX_MONTH_MILLIS)
            {
                cal.setTime(new Date()); // Today
                cal.add(Calendar.DATE, 1); //End of current day is considered as beginning of next day
                mEndTimeStamp = Utils.atStartOfDay(cal);
                cal.add(Calendar.DATE, -180);
                mStartTimeStamp = Utils.atStartOfDay(cal);
                mInterval = ONE_MONTH_MILLIS;
            }

            loadStatistics();
        }
    }

    private void processStatistics(List<Statistics> statistics)
    {
        Map<Long, Integer> decrementStatistics = new HashMap<>();
        Map<Long, Integer> incrementStatistics = new HashMap<>();
        Map<Long, Integer> resetStatistics = new HashMap<>();

        // group statistics by day
        List<Long> timeStampGroups = getTimeStampGroups(mStartTimeStamp, mEndTimeStamp, mInterval);
        for (int i = 0; i < timeStampGroups.size(); i++)
        {
            decrementStatistics.put(timeStampGroups.get(i), 0);
            incrementStatistics.put(timeStampGroups.get(i), 0);
            resetStatistics.put(timeStampGroups.get(i), 0);
        }

        for (int i = 0; i < statistics.size(); i++)
        {
            StatisticsType statType = statistics.get(i).getType();
            long statDateTimeStamp = statistics.get(i).getDateTimeStamp();
            for (int j = 0; j < timeStampGroups.size() - 1; j++)
            {
                if (statDateTimeStamp >= timeStampGroups.get(j) && statDateTimeStamp < timeStampGroups.get(j + 1))
                {
                    switch (statType)
                    {
                        case DECREMENT:
                            decrementStatistics.put(timeStampGroups.get(j), decrementStatistics.get(timeStampGroups.get(j)) + 1);
                            break;
                        case INCREMENT:
                            incrementStatistics.put(timeStampGroups.get(j), incrementStatistics.get(timeStampGroups.get(j)) + 1);
                            break;
                        case RESET:
                            resetStatistics.put(timeStampGroups.get(j), resetStatistics.get(timeStampGroups.get(j)) + 1);
                            break;
                    }
                }
            }
        }

        List<BarEntry> incrementEntries = new ArrayList<>();
        List<BarEntry> decrementEntries = new ArrayList<>();
        List<BarEntry> resetEntries = new ArrayList<>();

        for (int i = 0; i < timeStampGroups.size(); i++)
        {
            decrementEntries.add(new BarEntry(i, decrementStatistics.get(timeStampGroups.get(i))));
            incrementEntries.add(new BarEntry(i, incrementStatistics.get(timeStampGroups.get(i))));
            resetEntries.add(new BarEntry(i, resetStatistics.get(timeStampGroups.get(i))));
        }

        BarDataSet decrementDataSet = new BarDataSet(decrementEntries, mStatisticsView.getStringById(R.string.decrement));
        decrementDataSet.setColor(Color.YELLOW);
        BarDataSet incrementDataSet = new BarDataSet(incrementEntries, mStatisticsView.getStringById(R.string.increment));
        incrementDataSet.setColor(Color.GREEN);
        BarDataSet resetDataSet = new BarDataSet(resetEntries, mStatisticsView.getStringById(R.string.reset));
        resetDataSet.setColor(Color.RED);

        List<CounterStatisticsAdapter.Row> statisticsRows = new ArrayList<>();

        for (Long timeStamp : timeStampGroups)
        {
            if (incrementStatistics.get(timeStamp) != 0)
                statisticsRows.add(new CounterStatisticsAdapter.Row(timeStamp, StatisticsType.INCREMENT, incrementStatistics.get(timeStamp)));
            if (decrementStatistics.get(timeStamp) != 0)
                statisticsRows.add(new CounterStatisticsAdapter.Row(timeStamp, StatisticsType.DECREMENT, decrementStatistics.get(timeStamp)));
            if (resetStatistics.get(timeStamp) != 0)
                statisticsRows.add(new CounterStatisticsAdapter.Row(timeStamp, StatisticsType.RESET, resetStatistics.get(timeStamp)));
        }

        Collections.sort(statisticsRows, new Comparator<CounterStatisticsAdapter.Row>()
        {
            @Override
            public int compare(CounterStatisticsAdapter.Row row1, CounterStatisticsAdapter.Row row2)
            {
                if (row1.getTimeStamp() - row2.getTimeStamp() < 0)
                    return -1;
                else if (row1.getTimeStamp() - row2.getTimeStamp() > 0)
                    return 1;
                else
                    return row1.getType().ordinal() - row2.getType().ordinal();
            }
        });

        mStatisticsView.showStatistics(new BarData(incrementDataSet, decrementDataSet, resetDataSet), timeStampGroups, statisticsRows);
    }

    private void processEmptyStatistics()
    {
        mStatisticsView.showNoStatistics();
    }

    private List<Long> getTimeStampGroups(long startTimeStamp, long endTimeStamp, long interval)
    {
        int n = 0;
        List<Long> xValues = new ArrayList<>();
        while ((startTimeStamp + n * interval) <= endTimeStamp)
            xValues.add(startTimeStamp + interval * n++);

        return xValues;
    }
}
