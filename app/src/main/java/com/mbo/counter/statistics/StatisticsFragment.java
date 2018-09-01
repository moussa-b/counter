package com.mbo.counter.statistics;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class StatisticsFragment extends Fragment implements StatisticsContract.View
{
    public static final String ARGUMENT_STATISTICS_COUNTER_ID = "STATISTICS_COUNTER_ID";

    private StatisticsContract.Presenter mPresenter;

    private StatisticsAdapter mListAdapter;

    private TextView mNoStatisticsTextView;

    private ListView mStatisticsListView;

    private BarChart mChart;

    public StatisticsFragment()
    {
    }

    public static StatisticsFragment newInstance()
    {
        return new StatisticsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    { // Lifecycle : called first, for doing any non-graphical initialisations
        super.onCreate(savedInstanceState);
        mListAdapter = new StatisticsAdapter(new ArrayList<Statistics>(0));
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
    public void setPresenter(StatisticsContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void showStatistics(List<Statistics> statistics)
    {
        mListAdapter.replaceData(statistics);
        mStatisticsListView.setVisibility(View.VISIBLE);
        mNoStatisticsTextView.setVisibility(View.GONE);

        if (statistics != null && statistics.size() > 0)
        {
            Map<Integer, Integer> decrementStatistics = new HashMap<>();
            Map<Integer, Integer> incrementStatistics = new HashMap<>();
            Map<Integer, Integer> resetStatistics = new HashMap<>();
            int startYear = 0;

            for (Statistics stat : statistics)
            {
                if (startYear == 0)
                    startYear = (int) (stat.getDateTimeStamp() / 86400000);

                Integer dayNumber = (int) (stat.getDateTimeStamp() / 86400000);
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

            List<BarEntry> incrementEntries = new ArrayList<>();

            for (Integer day : incrementStatistics.keySet())
                incrementEntries.add(new BarEntry(day, incrementStatistics.get(day)));

            BarDataSet incrementDataSet = new BarDataSet(incrementEntries, getString(R.string.increment_statistics));
            incrementDataSet.setColor(Color.GREEN);

            List<BarEntry> decrementEntries = new ArrayList<>();

            for (Integer day : decrementStatistics.keySet())
                decrementEntries.add(new BarEntry(day, decrementStatistics.get(day)));

            BarDataSet decrementDataSet = new BarDataSet(decrementEntries, getString(R.string.decrement_statistics));
            decrementDataSet.setColor(Color.YELLOW);

            List<BarEntry> resetEntries = new ArrayList<>();

            for (Integer day : resetStatistics.keySet())
                resetEntries.add(new BarEntry(day, resetStatistics.get(day)));

            BarDataSet resetDataSet = new BarDataSet(resetEntries, getString(R.string.reset_statistics));
            resetDataSet.setColor(Color.RED);

            float groupSpace = 0.04f;
            float barSpace = 0.02f; // x2 dataset
            float barWidth = 0.1f; // x2 dataset

            Locale locale = ConfigurationCompat.getLocales(getContext().getResources().getConfiguration()).get(0);

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
        mNoStatisticsTextView.setVisibility(View.VISIBLE);
    }

    private static class StatisticsAdapter extends BaseAdapter
    {
        private List<Statistics> mStatistics;

        public StatisticsAdapter(List<Statistics> statistics)
        {
            this.mStatistics = statistics;
        }

        @Override
        public int getCount()
        {
            return mStatistics.size();
        }

        @Override
        public Statistics getItem(int position)
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

            if (rowView == null)
            {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                rowView = inflater.inflate(R.layout.statistics_item, parent, false);
            }

            final Statistics statistics = getItem(position);

            TextView dateTextView = rowView.findViewById(R.id.date_text_view);
            Locale locale = ConfigurationCompat.getLocales(dateTextView.getContext().getResources().getConfiguration()).get(0);
            dateTextView.setText(Utils.formatDateForDisplay(statistics.getDateTimeStamp(), locale));

            TextView valueTextView = rowView.findViewById(R.id.value_text_view);
            valueTextView.setText(String.valueOf(statistics.getValue()));

            return rowView;
        }

        private void replaceData(List<Statistics> statistics)
        {
            this.mStatistics = statistics;
            notifyDataSetChanged();
        }
    }
}
