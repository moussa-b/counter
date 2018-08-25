package com.mbo.counter.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.ConfigurationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mbo.counter.utils.Utils;
import com.mbo.counter.R;
import com.mbo.counter.data.model.Statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        mListAdapter = new StatisticsAdapter(new ArrayList<Statistics.Row>(0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.statistics_fragment, container, false);

        // Set up tasks view
        mStatisticsListView = (ListView) root.findViewById(R.id.statistics_list_view);
        mStatisticsListView.setAdapter(mListAdapter);

        // Set up  no tasks view
        mNoStatisticsTextView = (TextView) root.findViewById(R.id.no_statistics_text_view);

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
    public void showStatistics(List<Statistics.Row> statistics)
    {
        mListAdapter.replaceData(statistics);
        mStatisticsListView.setVisibility(View.VISIBLE);
        mNoStatisticsTextView.setVisibility(View.GONE);
    }

    @Override
    public void showNoStatistics()
    {
        mStatisticsListView.setVisibility(View.GONE);
        mNoStatisticsTextView.setVisibility(View.VISIBLE);
    }

    private static class StatisticsAdapter extends BaseAdapter
    {
        private List<Statistics.Row> mStatistics;

        public StatisticsAdapter(List<Statistics.Row> statistics)
        {
            this.mStatistics = statistics;
        }

        @Override
        public int getCount()
        {
            return mStatistics.size();
        }

        @Override
        public Statistics.Row getItem(int position)
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

            final Statistics.Row statistics = getItem(position);

            TextView dateTextView = (TextView) rowView.findViewById(R.id.date_text_view);
            Locale locale = ConfigurationCompat.getLocales(dateTextView.getContext().getResources().getConfiguration()).get(0);
            dateTextView.setText(Utils.formatDateForDisplay(statistics.getDate(), locale));

            TextView valueTextView = (TextView) rowView.findViewById(R.id.value_text_view);
            valueTextView.setText(String.valueOf(statistics.getValue()));

            return rowView;
        }

        private void replaceData(List<Statistics.Row> statistics)
        {
            this.mStatistics = statistics;
            notifyDataSetChanged();
        }
    }
}
