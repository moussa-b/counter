package com.mbo.counter.folderlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.addeditcounter.AddEditCounterActivity;
import com.mbo.counter.counter.CounterActivity;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.CounterGroup;
import com.mbo.counter.statistics.StatisticsActivity;
import com.mbo.counter.utils.CallBack;
import com.mbo.counter.utils.CounterGroupUtils;

import java.util.ArrayList;
import java.util.List;

import static com.mbo.counter.addeditcounter.AddEditCounterFragment.ARGUMENT_EDIT_COUNTER_ID;
import static com.mbo.counter.counter.CounterFragment.ARGUMENT_COUNTER_ID;
import static com.mbo.counter.statistics.StatisticsFragment.ARGUMENT_STATISTICS_COUNTER_ID;

public class CounterGroupListFragment extends Fragment implements CounterGroupListContract.View
{
    private static final int REQUEST_COUNTER = 100;
    private ExpandableListView mCounterExpandableListView;
    private TextView mNoCounterTextView;
    private CounterGroupListContract.Presenter mPresenter;
    private CounterExpandableListAdapter mExpandableAdapter;
    private CounterGroupItemListener mCounterGroupListener = new CounterGroupItemListener()
    {
        @Override
        public void onCounterDecrement(int groupPosition, int childPosition, int counterId)
        {
            mPresenter.decrementCounter(groupPosition, childPosition, counterId);
        }

        @Override
        public void onCounterIncrement(int groupPosition, int childPosition, int counterId)
        {
            mPresenter.incrementCounter(groupPosition, childPosition, counterId);
        }

        @Override
        public void onCounterShowMenu(View view, final Counter clickedCounter, final int groupPosition, final int childPosition)
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
                                mPresenter.resetCounter(groupPosition, childPosition, clickedCounter.getId());
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
        public void onCounterGroupShowMenu(View view, final CounterGroup clickedCounterGroup, final int groupPosition)
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
                            case R.id.action_rename:
                                renameCounterGroup(groupPosition);
                                return true;
                            case R.id.action_reset_all:
                                mPresenter.resetCountersInGroup(clickedCounterGroup.getId());
                                return true;
                            case R.id.action_delete_all:
                                mPresenter.deleteCountersInGroup(clickedCounterGroup.getId());
                                return true;
                            case R.id.action_duplicate:
                                mPresenter.duplicateCounterGroup(clickedCounterGroup.getId());
                                return true;
                            case R.id.action_delete:
                                mPresenter.deleteCounterGroup(clickedCounterGroup.getId());
                                return true;
                            case R.id.action_statistics:
                                // TODO faire les statistics par groupe
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.inflate(R.menu.popup_menu_counter_group);
                popup.show();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mExpandableAdapter = new CounterExpandableListAdapter(new ArrayList<CounterGroup>(0), mCounterGroupListener);
        setHasOptionsMenu(true); // The fragment handle the menu
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.counter_group_list_fragment, container, false);
        mCounterExpandableListView = root.findViewById(R.id.counter_expandable_list_view);
        mCounterExpandableListView.setAdapter(mExpandableAdapter);
        mNoCounterTextView = root.findViewById(R.id.no_counter_text_view);
        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                CounterGroupUtils.showAddCounterGroup(getContext(), new CallBack()
                {
                    @Override
                    public void execute(Object data)
                    {
                        CounterGroup counterGroup = new CounterGroup((String) data);
                        mPresenter.saveCounterGroup(counterGroup);
                        mExpandableAdapter.notifyDataSetChanged();
                    }
                });
                return true;
        }
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        if (!hidden)
            mPresenter.loadCounterGroups();
    }

    public static CounterGroupListFragment newInstance()
    {
        return new CounterGroupListFragment();
    }

    @Override
    public void setPresenter(CounterGroupListContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void setCount(int groupPosition, int childPosition, int count)
    {
        Counter counter = ((Counter) mExpandableAdapter.getChild(groupPosition, childPosition));
        counter.setCount(count);
        mExpandableAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCounterGroups(List<CounterGroup> counterGroups)
    {
        mCounterExpandableListView.setVisibility(View.VISIBLE);
        mNoCounterTextView.setVisibility(View.GONE);
        mExpandableAdapter.replaceData(counterGroups);
    }

    @Override
    public void showCounterUi(int counterId)
    {
        Intent intent = new Intent(getContext(), CounterActivity.class);
        intent.putExtra(ARGUMENT_COUNTER_ID, counterId);
        startActivityForResult(intent, REQUEST_COUNTER);
    }

    @Override
    public void showEditCounterUi(int counterId)
    {
        Intent intent = new Intent(getContext(), AddEditCounterActivity.class);
        intent.putExtra(ARGUMENT_EDIT_COUNTER_ID, counterId);
        startActivityForResult(intent, REQUEST_COUNTER);
    }

    @Override
    public void showNoCounterGroups()
    {
        mCounterExpandableListView.setVisibility(View.GONE);
        mNoCounterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCounterStatisticsUi(int counterId)
    {
        Intent intent = new Intent(getContext(), StatisticsActivity.class);
        intent.putExtra(ARGUMENT_STATISTICS_COUNTER_ID, counterId);
        startActivityForResult(intent, REQUEST_COUNTER);
    }

    @Override
    public void renameCounterGroup(final int groupPosition)
    {
        final CounterGroup counterGroup = ((CounterGroup) mExpandableAdapter.getGroup(groupPosition));
        CounterGroupUtils.showAddCounterGroup(getContext(), counterGroup.getName(), new CallBack()
        {
            @Override
            public void execute(Object data)
            {
                counterGroup.setName((String) data);
                mPresenter.saveCounterGroup(counterGroup);
                mExpandableAdapter.notifyDataSetChanged();
            }
        });
    }
}
