package com.mbo.counter.folderstatistics;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mbo.counter.data.model.Counter;

import java.util.List;

public class FolderStatisticsAdapter extends BaseAdapter
{
    private List<Counter> mCounters;

    public FolderStatisticsAdapter(List<Counter> counters)
    {
        this.mCounters = counters;
    }

    @Override
    public int getCount()
    {
        return mCounters.size();
    }

    @Override
    public Counter getItem(int position)
    {
        return mCounters.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return null;
    }
}
