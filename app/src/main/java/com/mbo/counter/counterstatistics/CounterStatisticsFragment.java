package com.mbo.counter.counterstatistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.ConfigurationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.mbo.counter.R;
import com.mbo.counter.commons.AndroidChartDateAxisFormatter;
import com.mbo.counter.commons.AndroidChartValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class CounterStatisticsFragment extends Fragment implements CounterStatisticsContract.View
{
    public static final String ARGUMENT_STATISTICS_COUNTER_ID = "STATISTICS_COUNTER_ID";

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
    public void showStatistics(BarData barData, List<Long> timeStampGroups, List<StatisticsAdapter.Row> statisticsRows)
    {
        if (barData == null)
        {
            mStatisticsListView.setVisibility(View.VISIBLE);
            mNoStatisticsTextView.setVisibility(View.GONE);
            mChart.setVisibility(View.GONE);
        }
        else
        {
            mNoStatisticsTextView.setVisibility(View.GONE);
            mStatisticsListView.setVisibility(View.VISIBLE);
            LayoutInflater inflaterHeader = getLayoutInflater();
            ViewGroup header = (ViewGroup) inflaterHeader.inflate(R.layout.statistics_list_header, mStatisticsListView, false);
            mStatisticsListView.addHeaderView(header);

            // Configure X axis
            Locale locale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
            if (getContext() != null)
                locale = ConfigurationCompat.getLocales(getContext().getResources().getConfiguration()).get(0);
            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new AndroidChartDateAxisFormatter(locale, timeStampGroups));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            mChart.getXAxis().setAxisMinimum(0);

            // Configure Y axis
            mChart.getAxisLeft().setAxisMinimum(0);
            mChart.getAxisRight().setEnabled(false);

            // Customize the look of the graph
            mChart.setDrawBorders(true);
            mChart.setBackgroundColor(getResources().getColor(R.color.white));
            mChart.setBorderWidth(1);
            mChart.setBorderColor(getResources().getColor(R.color.black));
            barData.setValueFormatter(new AndroidChartValueFormatter());
            mChart.setHighlightPerTapEnabled(false);
            mChart.getDescription().setEnabled(false);
            mChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            mChart.setPinchZoom(false);

            // Add data and display
            // (0.03 + 0.27) * 3 + 0.1 = 1.00 -> interval per "group" / multiply by 3 because 3 dataset
            float groupSpace = 0.1f;
            float barSpace = 0.03f;
            float barWidth = 0.27f;
            mChart.setData(barData);
            mChart.getBarData().setBarWidth(barWidth);
            mChart.groupBars(0, groupSpace, barSpace);
            mChart.invalidate(); // refresh

            mListAdapter.replaceData(statisticsRows);
        }
    }

    @Override
    public void showNoStatistics()
    {
        mStatisticsListView.setVisibility(View.GONE);
        mChart.setVisibility(View.GONE);
        mNoStatisticsTextView.setVisibility(View.VISIBLE);
    }
}
