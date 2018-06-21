package com.mbo.counter.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.addeditcounter.AddEditCounterActivity;
import com.mbo.counter.data.model.Counter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment implements HomeContract.View
{

    private HomeContract.Presenter mPresenter;

    private CountersAdapter mListAdapter;

    private TextView mNoCounterTextView;

    private ListView mCountListView;

    public HomeFragment()
    {
    }

    public static HomeFragment newInstance()
    {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    { // Lifecycle : called first, for doing any non-graphical initialisations
        super.onCreate(savedInstanceState);
        mListAdapter = new CountersAdapter(new ArrayList<Counter>(0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    { // Lifecycle : called after onCreate, for doing any graphical initialisations
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        // Set up tasks view
        mCountListView = (ListView) root.findViewById(R.id.counter_list_view);
        mCountListView.setAdapter(mListAdapter);

        // Set up  no tasks view
        mNoCounterTextView = (TextView) root.findViewById(R.id.no_counter_text_view);
        mNoCounterTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showAddCounter();
            }
        });

        // Set up floating action button
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_counter);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAddCounter();
            }
        });

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void showCounters(List<Counter> counters)
    {
        mListAdapter.replaceData(counters);

        mCountListView.setVisibility(View.VISIBLE);
        mNoCounterTextView.setVisibility(View.GONE);
    }

    @Override
    public void showAddCounter()
    {
        Intent intent = new Intent(getContext(), AddEditCounterActivity.class);
        startActivityForResult(intent, AddEditCounterActivity.REQUEST_ADD_COUNTER);
    }

    @Override
    public void showNoCounters()
    {
        mCountListView.setVisibility(View.GONE);
        mNoCounterTextView.setVisibility(View.VISIBLE);
    }

    private static class CountersAdapter extends BaseAdapter
    {

        private List<Counter> mCounters;

        public CountersAdapter(List<Counter> mCounters)
        {
            this.mCounters = mCounters;
        }

        @Override
        public int getCount()
        {
            return mCounters.size();
        }

        @Override
        public Counter getItem(int i)
        {
            return mCounters.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            View rowView = view;

            if (rowView == null)
            {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.counter_item, viewGroup, false);
            }

            final Counter counter = getItem(i);

            TextView title = (TextView) rowView.findViewById(R.id.title);
            title.setText(counter.getName());

            return rowView;
        }

        private void replaceData(List<Counter> counters)
        {
            this.mCounters = counters;
            notifyDataSetChanged();
        }
    }
}
