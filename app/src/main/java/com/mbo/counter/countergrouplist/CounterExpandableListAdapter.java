package com.mbo.counter.countergrouplist;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.CounterGroup;

import java.util.List;

public class CounterExpandableListAdapter extends BaseExpandableListAdapter
{
    private List<CounterGroup> mCounterGroups;

    CounterExpandableListAdapter(List<CounterGroup> counterGroups)
    {
        this.mCounterGroups = counterGroups;
    }

    @Override
    public int getGroupCount()
    {
        return mCounterGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return mCounterGroups.get(groupPosition).getCounters().size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return mCounterGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return mCounterGroups.get(groupPosition).getCounters().toArray()[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        CounterGroup counterGroup = (CounterGroup) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null)
                convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        if (convertView != null)
        {
            TextView counterGroupNameTextView = convertView.findViewById(android.R.id.text1);
            counterGroupNameTextView.setTypeface(null, Typeface.BOLD);
            counterGroupNameTextView.setText(counterGroup.getName());
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) (parent.getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null)
                convertView = layoutInflater.inflate(R.layout.counter_item, parent, false);
        }
        if (convertView != null)
        {
            final Counter counter = (Counter) getChild(groupPosition, childPosition);
            TextView counterNameTextView = convertView.findViewById(R.id.name_text_view);
            counterNameTextView.setText(counter.getName());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    public void replaceData(List<CounterGroup> counterGroups)
    {
        this.mCounterGroups = counterGroups;
        notifyDataSetChanged();
    }
}
