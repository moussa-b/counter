package com.mbo.counter.counterlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.addeditcounter.AddEditCounterActivity;
import com.mbo.counter.counter.CounterActivity;
import com.mbo.counter.data.model.Counter;

import java.util.ArrayList;
import java.util.List;

import static com.mbo.counter.counter.CounterFragment.ARGUMENT_COUNTER_ID;

public class CounterListFragment extends Fragment implements CounterListContract.View
{
    private CounterListAdapter mRecyclerAdapter;

    private static final int REQUEST_COUNTER = 100;
    private RecyclerView mCounterRecyclerView;
    private TextView mNoCounterTextView;
    private CounterListFragment.CounterItemListener mCounterListener = new CounterListFragment.CounterItemListener()
    {
        @Override
        public void onCounterClick(Counter clickedCounter)
        {
            mPresenter.openCounter(clickedCounter);
        }

        @Override
        public void onCounterDecrement(int index, int counterId)
        {
            mPresenter.decrementCounter(index, counterId);
        }

        @Override
        public void onCounterIncrement(int index, int counterId)
        {
            mPresenter.incrementCounter(index, counterId);
        }

        @Override
        public void onCounterReset(int index, Counter clickedCounter)
        {

        }

        @Override
        public void onCounterShowMenu(Counter clickedCounter)
        {

        }

        @Override
        public void delete(Counter clickedCounter)
        {
            mPresenter.deleteCounter(clickedCounter.getId());
        }
    };
    private CounterListContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    { // Lifecycle : called first, for doing any non-graphical initialisations
        super.onCreate(savedInstanceState);
        mRecyclerAdapter = new CounterListAdapter(new ArrayList<Counter>(0), mCounterListener);
    }

    public CounterListFragment()
    {
    }

    public static CounterListFragment newInstance()
    {
        return new CounterListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.counter_list_fragment, container, false);

        // Set up counters view
        mCounterRecyclerView = root.findViewById(R.id.counter_recycler_view);
        mCounterRecyclerView.setAdapter(mRecyclerAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL);
        mCounterRecyclerView.addItemDecoration(itemDecoration);
        mCounterRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        // Set up no counters view
        mNoCounterTextView = root.findViewById(R.id.no_counter_text_view);
        mNoCounterTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showAddCounter();
            }
        });

        return root;
    }

    @Override
    public void setCount(int index, int count)
    {
        CounterListAdapter.CounterViewHolder counterViewHolder = (CounterListAdapter.CounterViewHolder) mCounterRecyclerView.findViewHolderForAdapterPosition(index);
        if (counterViewHolder != null)
            counterViewHolder.countTextView.setText(String.valueOf(count));
    }

    @Override
    public void setProgression(int index, int progression)
    {
        CounterListAdapter.CounterViewHolder counterViewHolder = (CounterListAdapter.CounterViewHolder) mCounterRecyclerView.findViewHolderForAdapterPosition(index);
        if (counterViewHolder != null)
        {
            counterViewHolder.counterItemProgressBar.setProgress(progression);
            counterViewHolder.progressionTextView.setText(String.format("%s%%", String.valueOf(progression)));
        }
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_edit:
                break;
        }
        return true;
    }

    @Override
    public void showCounterMenu(int counterId)
    {

    }

    @Override
    public void showAddCounter()
    {
        Intent intent = new Intent(getContext(), AddEditCounterActivity.class);
        startActivityForResult(intent, AddEditCounterActivity.REQUEST_ADD_COUNTER);
    }

    @Override
    public void showCounters(List<Counter> counters)
    {
        mRecyclerAdapter.replaceData(counters);
        mCounterRecyclerView.setVisibility(View.VISIBLE);
        mNoCounterTextView.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(CounterListContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void showEditCounterUi(int counterId)
    {

    }

    @Override
    public void showCounterUi(int counterId)
    {
        Intent intent = new Intent(getContext(), CounterActivity.class);
        intent.putExtra(ARGUMENT_COUNTER_ID, counterId);
        startActivityForResult(intent, REQUEST_COUNTER);
    }

    @Override
    public void showNoCounters()
    {
        mCounterRecyclerView.setVisibility(View.GONE);
        mNoCounterTextView.setVisibility(View.VISIBLE);
    }

    public interface CounterItemListener
    {
        void delete(Counter clickedCounter);

        void onCounterClick(Counter clickedCounter);

        void onCounterDecrement(int index, int counterId);

        void onCounterIncrement(int index, int counterId);

        void onCounterReset(int index, Counter clickedCounter);

        void onCounterShowMenu(Counter clickedCounter);
    }
}