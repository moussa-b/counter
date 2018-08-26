package com.mbo.counter.folderlist;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Folder;

import java.util.List;

public class CounterExpandableListAdapter extends BaseExpandableListAdapter
{
    private FolderItemListener mFolderListener;

    private List<Folder> mFolders;

    CounterExpandableListAdapter(List<Folder> folders, FolderItemListener folderListener)
    {
        this.mFolders = folders;
        this.mFolderListener = folderListener;
    }

    @Override
    public int getGroupCount()
    {
        return mFolders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return mFolders.get(groupPosition).getCounters().size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return mFolders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return mFolders.get(groupPosition).getCounters().toArray()[childPosition];
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        final Folder folder = (Folder) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null)
                convertView = layoutInflater.inflate(R.layout.counter_group_item, parent, false);
        }
        if (convertView != null)
        {
            TextView folderNameTextView = convertView.findViewById(R.id.name_text_view);
            folderNameTextView.setTypeface(null, Typeface.BOLD);
            folderNameTextView.setText(folder.getName());

            ImageView editCounterImageView = convertView.findViewById(R.id.edit_counter_image_view);
            editCounterImageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mFolderListener.onFolderShowMenu(v, folder, groupPosition);
                }
            });
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
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
            TextView countTextView = convertView.findViewById(R.id.count_text_view);
            TextView nameTextView = convertView.findViewById(R.id.name_text_view);
            TextView  progressionTextView = convertView.findViewById(R.id.progression_text_view);
            ProgressBar counterItemProgressBar = convertView.findViewById(R.id.counter_item_progress_bar);
            ImageButton decreaseCounterImageButton = convertView.findViewById(R.id.decrease_counter_image_button);
            ImageButton increaseCounterImageButton = convertView.findViewById(R.id.increase_counter_image_button);
            ImageView editCounterImageView = convertView.findViewById(R.id.edit_counter_image_view);

            countTextView.setText(String.valueOf(counter.getCount()));
            nameTextView.setText(counter.getName());
            int progression = (int) (100 * (float) counter.getCount() / ((float) counter.getLimit()));
            progressionTextView.setText(String.format("%s%%", String.valueOf(progression)));
            counterItemProgressBar.setProgress(progression);
            decreaseCounterImageButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mFolderListener.onCounterDecrement(groupPosition, childPosition, counter.getId());
                }
            });
            increaseCounterImageButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mFolderListener.onCounterIncrement(groupPosition, childPosition, counter.getId());
                }
            });
            editCounterImageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mFolderListener.onCounterShowMenu(v, counter, groupPosition, childPosition);
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    public void replaceData(List<Folder> folders)
    {
        this.mFolders = folders;
        notifyDataSetChanged();
    }
}
