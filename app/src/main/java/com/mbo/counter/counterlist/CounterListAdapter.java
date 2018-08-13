package com.mbo.counter.counterlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.utils.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;

public class CounterListAdapter extends RecyclerView.Adapter<CounterListAdapter.CounterViewHolder> implements ItemTouchHelperAdapter
{
    @Override
    public void onItemMove(int fromPosition, int toPosition)
    {
        if (fromPosition < toPosition)
        {
            for (int i = fromPosition; i < toPosition; i++)
            {
                Collections.swap(mCounters, i, i + 1);
            }
        }
        else
        {
            for (int i = fromPosition; i > toPosition; i--)
            {
                Collections.swap(mCounters, i, i - 1);
            }
        }

        Counter currentCounter = mCounters.get(toPosition);
        Counter previousCounter, nextCounter;
        notifyItemMoved(fromPosition, toPosition);

        try
        {
            previousCounter = mCounters.get(toPosition - 1);
        }
        catch (IndexOutOfBoundsException e)
        {
            previousCounter = null;
        }

        try
        {
            nextCounter = mCounters.get(toPosition + 1);
        }
        catch (IndexOutOfBoundsException e)
        {
            nextCounter = null;
        }

        if (previousCounter != null && nextCounter != null) // in middle of the list
            currentCounter.setOrder((previousCounter.getOrder() + nextCounter.getOrder()) / 2);
        else if (previousCounter == null && nextCounter != null) // at top of the list
            currentCounter.setOrder(nextCounter.getOrder() / 2);
        else if (previousCounter != null) // at bottom of the list
            currentCounter.setOrder(previousCounter.getOrder() + previousCounter.getOrder() / 2);

        mCounterListener.onItemMove(currentCounter);
    }

    class CounterViewHolder extends RecyclerView.ViewHolder
    {
        TextView countTextView;
        TextView nameTextView;
        TextView progressionTextView;
        ProgressBar counterItemProgressBar;
        ImageButton decreaseCounterImageButton;
        ImageButton increaseCounterImageButton;
        ImageView editCounterImageView;

        CounterViewHolder(View itemView)
        {
            super(itemView);

            countTextView = itemView.findViewById(R.id.count_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            progressionTextView = itemView.findViewById(R.id.progression_text_view);
            counterItemProgressBar = itemView.findViewById(R.id.counter_item_progress_bar);
            decreaseCounterImageButton = itemView.findViewById(R.id.decrease_counter_image_button);
            increaseCounterImageButton = itemView.findViewById(R.id.increase_counter_image_button);
            editCounterImageView = itemView.findViewById(R.id.edit_counter_image_view);
        }
    }

    private List<Counter> mCounters;

    private CounterItemListener mCounterListener;

    CounterListAdapter(List<Counter> counters, CounterItemListener itemListener)
    {
        this.mCounters = counters;
        this.mCounterListener = itemListener;
    }

    @NonNull
    @Override
    public CounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_item, parent, false);
        return new CounterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CounterViewHolder counterViewHolder, final int position)
    {
        final Counter counter = mCounters.get(position);
        counterViewHolder.countTextView.setText(String.valueOf(counter.getCount()));
        counterViewHolder.nameTextView.setText(String.format("%s %s", counter.getName(), String.format("(%d)", counter.getLimit())));
        int progression = (int) (100 * (float) counter.getCount() / ((float) counter.getLimit()));
        counterViewHolder.progressionTextView.setText(String.format("%s%%", String.valueOf(progression)));
        counterViewHolder.counterItemProgressBar.setProgress(progression);
        counterViewHolder.decreaseCounterImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCounterListener.onCounterDecrement(position, counter.getId(), counter.getLimit());
            }
        });
        counterViewHolder.increaseCounterImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCounterListener.onCounterIncrement(position, counter.getId(), counter.getLimit());
            }
        });
        counterViewHolder.editCounterImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCounterListener.onCounterShowMenu(v, counter, position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mCounters.size();
    }

    public void replaceData(List<Counter> counters)
    {
        this.mCounters = counters;
        notifyDataSetChanged();
    }
}
