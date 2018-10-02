package com.bdzapps.counterpp.folderstatistics;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bdzapps.counterpp.data.model.Counter;
import com.mbo.counter.R;

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
        View rowView = convertView;

        if (rowView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.folder_statistics_item, parent, false);
        }

        final Counter counter = getItem(position);

        TextView nameTextView = rowView.findViewById(R.id.name_text_view);
        TextView countTextView = rowView.findViewById(R.id.count_text_view);
        ProgressBar statisticsProgressBar = rowView.findViewById(R.id.statistics_progress_bar);
        float progression = getTotal() == 0 ? 0 : (100.0F * counter.getCount() / getTotal());

        nameTextView.setText(counter.getName());
        countTextView.setText(String.format("%s (%.2f%%)", String.valueOf(counter.getCount()), progression));
        statisticsProgressBar.setProgress((int) progression);
        statisticsProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(counter.getColor())));

        return rowView;
    }

    private int getTotal()
    {
        int total = 0;
        if (mCounters != null && mCounters.size() > 0)
        {
            for (Counter counter : mCounters)
                total += counter.getCount();
        }
        return total;
    }

    public void replaceData(List<Counter> counters)
    {
        this.mCounters = counters;
        notifyDataSetChanged();
    }
}
