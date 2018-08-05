package com.mbo.counter.countergrouplist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.data.model.CounterGroup;

import java.util.ArrayList;
import java.util.List;

public class CounterGroupListFragment extends Fragment implements CounterGroupListContract.View
{
    private ExpandableListView mCounterExpandableListView;
    private TextView mNoCounterTextView;
    private CounterGroupListContract.Presenter mPresenter;
    private CounterExpandableListAdapter mExpandableAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mExpandableAdapter = new CounterExpandableListAdapter(new ArrayList<CounterGroup>(0));
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
    public void showCounterGroups(List<CounterGroup> counterGroups)
    {
        mCounterExpandableListView.setVisibility(View.VISIBLE);
        mNoCounterTextView.setVisibility(View.GONE);
        mExpandableAdapter.replaceData(counterGroups);
    }

    @Override
    public void showNoCounterGroups()
    {
        mCounterExpandableListView.setVisibility(View.GONE);
        mNoCounterTextView.setVisibility(View.VISIBLE);
    }
}
