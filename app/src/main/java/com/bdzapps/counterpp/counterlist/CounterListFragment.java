package com.bdzapps.counterpp.counterlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bdzapps.counterpp.addeditcounter.AddEditCounterActivity;
import com.bdzapps.counterpp.commons.ItemOffsetDecoration;
import com.bdzapps.counterpp.counter.CounterActivity;
import com.bdzapps.counterpp.counterstatistics.CounterStatisticsActivity;
import com.bdzapps.counterpp.data.model.Counter;
import com.mbo.counter.R;

import java.util.ArrayList;
import java.util.List;

import static com.bdzapps.counterpp.addeditcounter.AddEditCounterFragment.ARGUMENT_EDIT_COUNTER_ID;
import static com.bdzapps.counterpp.counter.CounterFragment.ARGUMENT_COUNTER_ID;
import static com.bdzapps.counterpp.counterstatistics.CounterStatisticsFragment.ARGUMENT_STATISTICS_COUNTER_ID;

public class CounterListFragment extends Fragment implements CounterListContract.View
{
    public static final String ARGUMENT_FOLDER_ID = "FOLDER_ID";
    public static final String ARGUMENT_FOLDER_NAME = "FOLDER_NAME";
    private static final int REQUEST_COUNTER = 100;
    private RecyclerView mCounterRecyclerView;
    private CounterListAdapter mRecyclerAdapter;
    private TextView mNoCounterTextView;
    private CounterListContract.Presenter mPresenter;
    private int mFolderId;
    private CounterItemListener mCounterListener = new CounterItemListener()
    {
        @Override
        public void onCounterDecrement(int position, int counterId, int limit)
        {
            mPresenter.decrementCounter(position, counterId, limit);
        }

        @Override
        public void onCounterIncrement(int position, int counterId, int limit)
        {
            mPresenter.incrementCounter(position, counterId, limit);
        }

        @Override
        public void onCounterShowMenu(View view, final Counter clickedCounter, final int position)
        {
            if (getActivity() != null)
            {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.action_full_screen:
                                showCounterUi(clickedCounter.getId());
                                return true;
                            case R.id.action_reset:
                                mPresenter.resetCounter(position, clickedCounter.getId(), clickedCounter.getLimit());
                                return true;
                            case R.id.action_edit:
                                showEditCounterUi(clickedCounter.getId());
                                return true;
                            case R.id.action_duplicate:
                                mPresenter.duplicateCounter(clickedCounter.getId());
                                return true;
                            case R.id.action_delete:
                                mPresenter.deleteCounter(clickedCounter.getId());
                                return true;
                            case R.id.action_statistics:
                                showCounterStatisticsUi(clickedCounter.getId());
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.inflate(R.menu.popup_menu_counter);
                popup.show();
            }
        }

        @Override
        public void onItemMove(Counter counter)
        {
            mPresenter.saveCounter(counter);
        }
    };

    public CounterListFragment()
    {
    }

    public static CounterListFragment newInstance()
    {
        return new CounterListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    { // Lifecycle : called first, for doing any non-graphical initialisations
        super.onCreate(savedInstanceState);
        mRecyclerAdapter = new CounterListAdapter(new ArrayList<Counter>(0), mCounterListener);
        setHasOptionsMenu(true); // The fragment handle the menu
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.counter_list_fragment, container, false);

        // Set up counters view
        mCounterRecyclerView = root.findViewById(R.id.counter_recycler_view);
        mCounterRecyclerView.setAdapter(mRecyclerAdapter);
        // RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL);
        mCounterRecyclerView.addItemDecoration(new ItemOffsetDecoration(container.getContext(), R.dimen.recycler_view_item_offset));
        mCounterRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        // Set up drag and drop
        ItemTouchHelper.Callback callback = new CounterListItemTouchHelperCallback(mRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mCounterRecyclerView);

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

        Bundle folderIdBundle = getArguments();
        if (folderIdBundle != null)
            mFolderId = getArguments().getInt(CounterListFragment.ARGUMENT_FOLDER_ID, 0);

        return root;
    }

    @Override
    public void setCount(int position, int count)
    {
        CounterListAdapter.CounterViewHolder counterViewHolder = (CounterListAdapter.CounterViewHolder) mCounterRecyclerView.findViewHolderForAdapterPosition(position);
        if (counterViewHolder != null)
            counterViewHolder.countTextView.setText(String.valueOf(count));
    }

    @Override
    public void setProgression(int position, int progression)
    {
        CounterListAdapter.CounterViewHolder counterViewHolder = (CounterListAdapter.CounterViewHolder) mCounterRecyclerView.findViewHolderForAdapterPosition(position);
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
    public void onHiddenChanged(boolean hidden)
    {
        // Required to update counters when change between counter groups and counter list
        if (!hidden)
            mPresenter.loadCounters();
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
            case R.id.action_add:
                showAddCounter();
                return true;
        }
        return false;
    }

    @Override
    public void showAddCounter()
    {
        Intent intent = new Intent(getContext(), AddEditCounterActivity.class);
        if (mFolderId != 0)
            intent.putExtra(ARGUMENT_FOLDER_ID, mFolderId);
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
        Intent intent = new Intent(getContext(), AddEditCounterActivity.class);
        intent.putExtra(ARGUMENT_EDIT_COUNTER_ID, counterId);
        startActivityForResult(intent, REQUEST_COUNTER);
    }

    @Override
    public void showCounterUi(int counterId)
    {
        Intent intent = new Intent(getContext(), CounterActivity.class);
        intent.putExtra(ARGUMENT_COUNTER_ID, counterId);
        startActivityForResult(intent, REQUEST_COUNTER);
    }

    @Override
    public void showCounterStatisticsUi(int counterId)
    {
        Intent intent = new Intent(getContext(), CounterStatisticsActivity.class);
        intent.putExtra(ARGUMENT_STATISTICS_COUNTER_ID, counterId);
        startActivity(intent);
    }

    @Override
    public void showNoCounters()
    {
        mCounterRecyclerView.setVisibility(View.GONE);
        mNoCounterTextView.setVisibility(View.VISIBLE);
    }
}