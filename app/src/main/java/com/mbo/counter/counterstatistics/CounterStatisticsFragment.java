package com.mbo.counter.counterstatistics;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.ConfigurationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.mbo.counter.R;
import com.mbo.counter.commons.AndroidChartDateAxisFormatter;
import com.mbo.counter.commons.Utils;
import com.mbo.counter.data.model.Statistics;
import com.mbo.counter.data.model.StatisticsType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class CounterStatisticsFragment extends Fragment implements CounterStatisticsContract.View
{
    public static final String ARGUMENT_STATISTICS_COUNTER_ID = "STATISTICS_COUNTER_ID";

    private static final long ONE_DAY_MILLIS = 86400000L;

    private CounterStatisticsContract.Presenter mPresenter;

    private StatisticsAdapter mListAdapter;

    private TextView mNoStatisticsTextView;

    private ListView mStatisticsListView;

    private BarChart mChart;

    public CounterStatisticsFragment()
    {
    }

    public static CounterStatisticsFragment newInstance()
    {
        return new CounterStatisticsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    { // Lifecycle : called first, for doing any non-graphical initialisations
        super.onCreate(savedInstanceState);
        mListAdapter = new StatisticsAdapter(new ArrayList<StatisticsAdapter.Row>(0));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.statistics_fragment, container, false);

        // Set up tasks view
        mStatisticsListView = root.findViewById(R.id.statistics_list_view);
        mStatisticsListView.setAdapter(mListAdapter);

        // Set up  no tasks view
        mNoStatisticsTextView = root.findViewById(R.id.no_statistics_text_view);

        mChart = root.findViewById(R.id.chart);

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(CounterStatisticsContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void showStatistics(List<Statistics> statistics)
    {
        if (statistics == null || statistics.size() == 0)
        {
            mStatisticsListView.setVisibility(View.VISIBLE);
            mNoStatisticsTextView.setVisibility(View.GONE);
            mChart.setVisibility(View.GONE);
        }
        else
        {
            mStatisticsListView.setVisibility(View.VISIBLE);
            mNoStatisticsTextView.setVisibility(View.GONE);
            Map<Integer, Integer> decrementStatistics = new HashMap<>();
            Map<Integer, Integer> incrementStatistics = new HashMap<>();
            Map<Integer, Integer> resetStatistics = new HashMap<>();
            int startYear = 0;

            for (Statistics stat : statistics)
            {
                if (startYear == 0)
                    startYear = (int) (stat.getDateTimeStamp() / ONE_DAY_MILLIS);

                Integer dayNumber = (int) (stat.getDateTimeStamp() / ONE_DAY_MILLIS);
                switch (stat.getType())
                {
                    case DECREMENT:
                        if (decrementStatistics.get(dayNumber) == null)
                            decrementStatistics.put(dayNumber, 1);
                        else
                            decrementStatistics.put(dayNumber, decrementStatistics.get(dayNumber) + 1);
                        break;
                    case INCREMENT:
                        if (incrementStatistics.get(dayNumber) == null)
                            incrementStatistics.put(dayNumber, 1);
                        else
                            incrementStatistics.put(dayNumber, incrementStatistics.get(dayNumber) + 1);
                        break;
                    case RESET:
                        if (resetStatistics.get(dayNumber) == null)
                            resetStatistics.put(dayNumber, 1);
                        else
                            resetStatistics.put(dayNumber, resetStatistics.get(dayNumber) + 1);
                        break;
                }
            }

            List<StatisticsAdapter.Row> statisticsRows = new ArrayList<>();

            List<BarEntry> incrementEntries = new ArrayList<>();

            for (Integer day : incrementStatistics.keySet())
            {
                incrementEntries.add(new BarEntry(day, incrementStatistics.get(day)));
                statisticsRows.add(new StatisticsAdapter.Row(day * ONE_DAY_MILLIS, StatisticsType.INCREMENT, incrementStatistics.get(day)));
            }

            BarDataSet incrementDataSet = new BarDataSet(incrementEntries, getString(R.string.increment_statistics));
            incrementDataSet.setColor(Color.GREEN);

            List<BarEntry> decrementEntries = new ArrayList<>();

            for (Integer day : decrementStatistics.keySet())
            {
                decrementEntries.add(new BarEntry(day, decrementStatistics.get(day)));
                statisticsRows.add(new StatisticsAdapter.Row(day * ONE_DAY_MILLIS, StatisticsType.DECREMENT, decrementStatistics.get(day)));
            }

            BarDataSet decrementDataSet = new BarDataSet(decrementEntries, getString(R.string.decrement_statistics));
            decrementDataSet.setColor(Color.YELLOW);

            List<BarEntry> resetEntries = new ArrayList<>();

            for (Integer day : resetStatistics.keySet())
            {
                resetEntries.add(new BarEntry(day, resetStatistics.get(day)));
                statisticsRows.add(new StatisticsAdapter.Row(day * ONE_DAY_MILLIS, StatisticsType.RESET, resetStatistics.get(day)));
            }

            // TODO : sort statisticsRows by date then increment/decrement/reset
            mListAdapter.replaceData(statisticsRows);

            BarDataSet resetDataSet = new BarDataSet(resetEntries, getString(R.string.reset_statistics));
            resetDataSet.setColor(Color.RED);

            float groupSpace = 0.04f;
            float barSpace = 0.02f; // x2 dataset
            float barWidth = 0.1f; // x2 dataset

            Locale locale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
            if (getContext() != null)
                locale = ConfigurationCompat.getLocales(getContext().getResources().getConfiguration()).get(0);

            IAxisValueFormatter xAxisFormatter = new AndroidChartDateAxisFormatter(locale);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setValueFormatter(xAxisFormatter);

            BarData barData = new BarData(incrementDataSet, decrementDataSet, resetDataSet);
            mChart.setData(barData);
            mChart.getBarData().setBarWidth(barWidth);
            mChart.getXAxis().setAxisMinimum(startYear);
            mChart.groupBars(startYear, groupSpace, barSpace);
            mChart.invalidate(); // refresh
        }

    }

    @Override
    public void showNoStatistics()
    {
        mStatisticsListView.setVisibility(View.GONE);
        mChart.setVisibility(View.GONE);
        mNoStatisticsTextView.setVisibility(View.VISIBLE);
    }

    private static class StatisticsAdapter extends BaseAdapter
    {
        private List<StatisticsAdapter.Row> mStatistics;

        public static class Row
        {
            private long timeStamp;
            private StatisticsType type;
            private int value;

            public Row(long timeStamp, StatisticsType type, int value)
            {
                this.timeStamp = timeStamp;
                this.type = type;
                this.value = value;
            }

            public long getTimeStamp()
            {
                return timeStamp;
            }

            public StatisticsType getType()
            {
                return type;
            }

            public int getValue()
            {
                return value;
            }
        }

        public StatisticsAdapter(List<StatisticsAdapter.Row> statistics)
        {
            this.mStatistics = statistics;
        }

        @Override
        public int getCount()
        {
            return mStatistics.size();
        }

        @Override
        public StatisticsAdapter.Row getItem(int position)
        {
            return mStatistics.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View rowView = convertView;
            Context context = parent.getContext();

            if (rowView == null)
            {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                rowView = inflater.inflate(R.layout.statistics_item, parent, false);
            }

            final StatisticsAdapter.Row statistics = getItem(position);

            TextView dateTextView = rowView.findViewById(R.id.date_text_view);
            Locale locale = ConfigurationCompat.getLocales(context.getResources().getConfiguration()).get(0);
            String date = Utils.formatDateForDisplay(statistics.getTimeStamp(), locale);
            dateTextView.setText(date);

            TextView typeTextView = rowView.findViewById(R.id.type_text_view);
            String label = "";
            switch (statistics.getType())
            {
                case DECREMENT:
                    label = context.getString(R.string.decrement);
                    break;
                case INCREMENT:
                    label = context.getString(R.string.increment);
                    break;
                case RESET:
                    label = context.getString(R.string.reset);
                    break;
            }
            typeTextView.setText(label);

            TextView valueTextView = rowView.findViewById(R.id.value_text_view);
            valueTextView.setText(String.valueOf(statistics.getValue()));

            return rowView;
        }

        private void replaceData(List<StatisticsAdapter.Row> statistics)
        {
            this.mStatistics = statistics;
            notifyDataSetChanged();
        }
    }
}
