package com.mbo.counter.counterstatistics;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mbo.counter.R;
import com.mbo.counter.commons.Utils;
import com.mbo.counter.data.model.Statistics;
import com.mbo.counter.data.model.StatisticsType;
import com.mbo.counter.data.source.CounterDataSource;

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
    public static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L;
    public static final long ONE_WEEK_MILLIS = 7 * ONE_DAY_MILLIS;
    public static final long TWO_WEEK_MILLIS = 2 * 7 * ONE_DAY_MILLIS;
    public static final long ONE_MONTH_MILLIS = 30 * ONE_DAY_MILLIS;
    @NonNull
    private final CounterStatisticsContract.View mStatisticsView;
    @NonNull
    private final CounterDataSource mCounterDataSource;
    private long mStartTimeStamp;
    private long mEndTimeStamp;
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
        loadStatistics();
    }

    @Override
    public void loadStatistics()
    {
        // By default startTimeStamp = now - 7 week
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date()); // Today
        cal.add(Calendar.DATE, 1); //End of current day is considered as beginning of next day
        mEndTimeStamp = Utils.atStartOfDay(cal);
        cal.add(Calendar.DATE, -7);
        mStartTimeStamp = Utils.atStartOfDay(cal);

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

    private void processStatistics(List<Statistics> statistics)
    {
        Map<Long, Integer> decrementStatistics = new HashMap<>();
        Map<Long, Integer> incrementStatistics = new HashMap<>();
        Map<Long, Integer> resetStatistics = new HashMap<>();

        // group statistics by day
        List<Long> timeStampGroups = getTimeStampGroups(mStartTimeStamp, mEndTimeStamp, ONE_DAY_MILLIS);
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
