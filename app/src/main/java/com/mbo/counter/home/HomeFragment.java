package com.mbo.counter.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.addeditcounter.AddEditCounterActivity;
import com.mbo.counter.counter.CounterActivity;
import com.mbo.counter.data.model.Counter;

import java.util.ArrayList;
import java.util.List;

import static com.mbo.counter.counter.CounterFragment.ARGUMENT_COUNTER_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment implements HomeContract.View
{
    private static final int REQUEST_COUNTER = 100;

    private HomeContract.Presenter mPresenter;

    private CountersAdapter mListAdapter;

    private TextView mNoCounterTextView;

    private ListView mCountListView;

    private Menu menu;

    private CounterItemListener mCounterListener = new CounterItemListener()
    {
        @Override
        public void onCounterClick(Counter clickedCounter)
        {
            mPresenter.openCounter(clickedCounter);
        }

        @Override
        public void delete(Counter clickedCounter)
        {
            mPresenter.deleteCounter(clickedCounter.getId());
        }
    };

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
        mListAdapter = new CountersAdapter(new ArrayList<Counter>(0), mCounterListener);
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
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_edit:
                editCounters(true);
                break;
        }
        return true;
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
    public void showCounterUi(int counterId)
    {
        Intent intent = new Intent(getContext(), CounterActivity.class);
        intent.putExtra(ARGUMENT_COUNTER_ID, counterId);
        startActivityForResult(intent, REQUEST_COUNTER);
    }

    @Override
    public void editCounters(boolean activeEdition)
    {
        mListAdapter.toggleEditMode();

        if (mListAdapter.isActiveEditMode())
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_done));
        else
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_edit));

        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoCounters()
    {
        mCountListView.setVisibility(View.GONE);
        mNoCounterTextView.setVisibility(View.VISIBLE);
    }

    public interface CounterItemListener
    {
        void onCounterClick(Counter clickedCounter);

        void delete(Counter clickedCounter);
    }

    private static class CountersAdapter extends BaseAdapter
    {
        private List<Counter> mCounters;

        private CounterItemListener mCounterListener;

        private boolean activeEditMode = false;

        public CountersAdapter(List<Counter> counters, CounterItemListener itemListener)
        {
            this.mCounters = counters;
            this.mCounterListener = itemListener;
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
        public View getView(final int i, View view, ViewGroup viewGroup)
        {
            View rowView = view;

            if (rowView == null)
            {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.counter_item, viewGroup, false);
            }

            final Counter counter = getItem(i);

            rowView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (!activeEditMode)
                        mCounterListener.onCounterClick(counter);
                }
            });

            TextView countTextView = (TextView) rowView.findViewById(R.id.count_text_view);
            countTextView.setText(String.valueOf(counter.getCount()));

            TextView nameTextView = (TextView) rowView.findViewById(R.id.name_text_view);
            nameTextView.setText(counter.getName());

            TextView limitTextView = (TextView) rowView.findViewById(R.id.limit_text_view);
            limitTextView.setText("(" + counter.getLimit() + ")");

            TextView progressionTextView = (TextView) rowView.findViewById(R.id.progression_text_view);
            ProgressBar counterItemProgressBar = (ProgressBar) rowView.findViewById(R.id.counter_item_progress_bar);
            ImageButton deleteImageButton = (ImageButton) rowView.findViewById(R.id.delete_image_button);

            if (!activeEditMode)
            {
                progressionTextView.setVisibility(View.VISIBLE);
                counterItemProgressBar.setVisibility(View.VISIBLE);
                deleteImageButton.setVisibility(View.GONE);

                int progression = (int) (100 * (float) counter.getCount() / ((float) counter.getLimit()));
                progressionTextView.setText(String.valueOf(progression) + "%");

                counterItemProgressBar.setProgress(progression);
            }
            else
            {
                progressionTextView.setVisibility(View.GONE);
                counterItemProgressBar.setVisibility(View.GONE);
                deleteImageButton.setVisibility(View.VISIBLE);
            }

            deleteImageButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (activeEditMode)
                    {
                        mCounterListener.delete(counter);
                        mCounters.remove(i);
                        notifyDataSetChanged();
                    }
                }
            });

            return rowView;
        }

        private void replaceData(List<Counter> counters)
        {
            this.mCounters = counters;
            notifyDataSetChanged();
        }

        private void toggleEditMode()
        {
            activeEditMode = !activeEditMode;
        }

        private boolean isActiveEditMode()
        {
            return activeEditMode;
        }
    }
}
