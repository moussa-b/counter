package com.mbo.counter.counterstatistics;

import android.content.Context;
import android.support.v4.os.ConfigurationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.commons.Utils;
import com.mbo.counter.data.model.StatisticsType;

import java.util.List;
import java.util.Locale;

public class CounterStatisticsAdapter extends BaseAdapter
{
    private List<CounterStatisticsAdapter.Row> mStatistics;

    public static class Row
    {
        private long timeStamp;
        private StatisticsType type;
        private int value;

        public Row(long timeStamp, StatisticsType type, int value)
        {
            this.timeStamp = timeStamp;
            this.type = type;
            this.value = value;
        }

        public long getTimeStamp()
        {
            return timeStamp;
        }

        public StatisticsType getType()
        {
            return type;
        }

        public int getValue()
        {
            return value;
        }
    }

    public CounterStatisticsAdapter(List<CounterStatisticsAdapter.Row> statistics)
    {
        this.mStatistics = statistics;
    }

    @Override
    public int getCount()
    {
        return mStatistics.size();
    }

    @Override
    public CounterStatisticsAdapter.Row getItem(int position)
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
        Context context = parent.getContext();

        if (rowView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.counter_statistics_item, parent, false);
        }

        final CounterStatisticsAdapter.Row statistics = getItem(position);

        TextView dateTextView = rowView.findViewById(R.id.date_text_view);
        Locale locale = ConfigurationCompat.getLocales(context.getResources().getConfiguration()).get(0);
        String date = Utils.formatDateForDisplay(statistics.getTimeStamp(), locale);
        dateTextView.setText(date);

        TextView typeTextView = rowView.findViewById(R.id.type_text_view);
        String label = "";
        switch (statistics.getType())
        {
            case DECREMENT:
                label = context.getString(R.string.decrement);
                break;
            case INCREMENT:
                label = context.getString(R.string.increment);
                break;
            case RESET:
                label = context.getString(R.string.reset);
                break;
        }
        typeTextView.setText(label);

        TextView valueTextView = rowView.findViewById(R.id.value_text_view);
        valueTextView.setText(String.valueOf(statistics.getValue()));

        return rowView;
    }

    public void replaceData(List<CounterStatisticsAdapter.Row> statistics)
    {
        this.mStatistics = statistics;
        notifyDataSetChanged();
    }
}
