package com.bdzapps.counterpp.counterstatistics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.ConfigurationCompat;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bdzapps.counterpp.commons.AndroidChartDateAxisFormatter;
import com.bdzapps.counterpp.commons.AndroidChartValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.mbo.counter.R;

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

    private CounterStatisticsAdapter mListAdapter;

    private TextView mNoStatisticsTextView;

    private ListView mStatisticsListView;

    private BarChart mChart;

    private boolean isHeaderActive;

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
        mListAdapter = new CounterStatisticsAdapter(new ArrayList<CounterStatisticsAdapter.Row>(0));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.counter_statistics_fragment, container, false);

        // Set up tasks view
        mStatisticsListView = root.findViewById(R.id.statistics_list_view);
        mStatisticsListView.setAdapter(mListAdapter);

        // Set up  no tasks view
        mNoStatisticsTextView = root.findViewById(R.id.no_statistics_text_view);

        mChart = root.findViewById(R.id.chart);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        Context context = getContext();
        if (context != null)
        {
            inflater.inflate(R.menu.menu_statistics, menu);
            MenuItem item = menu.findItem(R.id.spinner);
            Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.period_statistics, R.layout.spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    if (mPresenter != null)
                    {
                        switch (position)
                        {
                            case 0:
                                mPresenter.updateStatistics(CounterStatisticsPresenter.ONE_WEEK_MILLIS);
                                break;
                            case 1:
                                mPresenter.updateStatistics(CounterStatisticsPresenter.ONE_MONTH_MILLIS);
                                break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void setPresenter(CounterStatisticsContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public String getStringById(int id)
    {
        if (getContext() != null)
            return getContext().getResources().getString(id);
        else
            return null;
    }

    @Override
    public void showStatistics(BarData barData, List<Long> timeStampGroups, List<CounterStatisticsAdapter.Row> statisticsRows)
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
            if (!isHeaderActive)
            {
                LayoutInflater inflaterHeader = getLayoutInflater();
                ViewGroup header = (ViewGroup) inflaterHeader.inflate(R.layout.counter_statistics_list_header, mStatisticsListView, false);
                mStatisticsListView.addHeaderView(header);
                isHeaderActive = true;
            }

            // Configure X axis
            Locale locale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
            if (getContext() != null)
                locale = ConfigurationCompat.getLocales(getContext().getResources().getConfiguration()).get(0);
            XAxis xAxis = mChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setAxisMaximum((float) (timeStampGroups.size() - 1));
            xAxis.setValueFormatter(new AndroidChartDateAxisFormatter(locale, timeStampGroups));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            mChart.getXAxis().setAxisMinimum(0);

            // Configure Y axis
            mChart.getAxisLeft().setAxisMinimum(0);
            mChart.getAxisRight().setEnabled(false);

            // Customize the look of the graph
            mChart.animateY(1000);
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
            mChart.notifyDataSetChanged();
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
