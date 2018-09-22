package com.mbo.counter.folderstatistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.mbo.counter.R;
import com.mbo.counter.data.model.Counter;

import java.util.ArrayList;
import java.util.List;

public class FolderStatisticsFragment extends Fragment implements FolderStatisticsContract.View
{
    public static final String ARGUMENT_STATISTICS_FOLDER_ID = "STATISTICS_FOLDER_ID";

    private FolderStatisticsContract.Presenter mPresenter;

    private FolderStatisticsAdapter mListAdapter;

    private TextView mNoStatisticsTextView;

    private ListView mStatisticsListView;

    private PieChart mChart;

    public FolderStatisticsFragment()
    {
    }

    public static FolderStatisticsFragment newInstance()
    {
        return new FolderStatisticsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    { // Lifecycle : called first, for doing any non-graphical initialisations
        super.onCreate(savedInstanceState);
        mListAdapter = new FolderStatisticsAdapter(new ArrayList<Counter>(0));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.folder_statistics_fragment, container, false);
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
    public void setPresenter(FolderStatisticsContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void showNoStatistics()
    {
        mStatisticsListView.setVisibility(View.GONE);
        mChart.setVisibility(View.GONE);
        mNoStatisticsTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showStatistics(PieDataSet dataSet, List<Counter> counters, List<Integer> colors)
    {
        if (dataSet == null)
        {
            mStatisticsListView.setVisibility(View.VISIBLE);
            mNoStatisticsTextView.setVisibility(View.GONE);
            mChart.setVisibility(View.GONE);
        }
        else if (getContext() != null)
        {
            mNoStatisticsTextView.setVisibility(View.GONE);
            mStatisticsListView.setVisibility(View.VISIBLE);
            LayoutInflater inflaterHeader = getLayoutInflater();
            ViewGroup header = (ViewGroup) inflaterHeader.inflate(R.layout.folder_statistics_list_header, mStatisticsListView, false);
            mStatisticsListView.addHeaderView(header);
            mListAdapter.replaceData(counters);
            dataSet.setColors(colors);
            PieData pieData = new PieData(dataSet);
            mChart.setUsePercentValues(true);
            mChart.getDescription().setEnabled(false);
            pieData.setValueFormatter(new PercentFormatter());
            mChart.animateY(1000);
            mChart.setData(pieData);
            mChart.invalidate();
        }
    }
}
