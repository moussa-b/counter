package com.bdzapps.counterpp.folderlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdzapps.counterpp.commons.ItemTouchHelperAdapter;
import com.bdzapps.counterpp.data.model.Folder;
import com.mbo.counter.R;

import java.util.Collections;
import java.util.List;

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.FolderViewHolder> implements ItemTouchHelperAdapter
{
    private List<Folder> mFolders;
    private FolderItemListener mFolderListener;

    FolderListAdapter(List<Folder> folders, FolderItemListener itemListener)
    {
        this.mFolders = folders;
        this.mFolderListener = itemListener;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition)
    {
        if (fromPosition < toPosition)
        {
            for (int i = fromPosition; i < toPosition; i++)
            {
                Collections.swap(mFolders, i, i + 1);
            }
        }
        else
        {
            for (int i = fromPosition; i > toPosition; i--)
            {
                Collections.swap(mFolders, i, i - 1);
            }
        }

        Folder currentFolder = mFolders.get(toPosition);
        Folder previousFolder, nextFolder;
        notifyItemMoved(fromPosition, toPosition);

        try
        {
            previousFolder = mFolders.get(toPosition - 1);
        }
        catch (IndexOutOfBoundsException e)
        {
            previousFolder = null;
        }

        try
        {
            nextFolder = mFolders.get(toPosition + 1);
        }
        catch (IndexOutOfBoundsException e)
        {
            nextFolder = null;
        }

        if (previousFolder != null && nextFolder != null) // in middle of the list
            currentFolder.setOrder((previousFolder.getOrder() + nextFolder.getOrder()) / 2);
        else if (previousFolder == null && nextFolder != null) // at top of the list
            currentFolder.setOrder(nextFolder.getOrder() / 2);
        else if (previousFolder != null) // at bottom of the list
            currentFolder.setOrder(previousFolder.getOrder() + previousFolder.getOrder() / 2);

        mFolderListener.onItemMove(currentFolder);
    }

    @NonNull
    @Override
    public FolderListAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item, parent, false);
        return new FolderListAdapter.FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderListAdapter.FolderViewHolder folderViewHolder, final int position)
    {
        final Folder folder = mFolders.get(position);
        folderViewHolder.nameTextView.setText(folder.getName());
        Context context = folderViewHolder.nameTextView.getContext();
        if (context != null)
        {
            String countLabel;
            if (folder.getCounters() == null || folder.getCounters().size() == 0)
                countLabel = context.getString(R.string.no_counters);
            else if (folder.getCounters().size() == 1)
                countLabel = "1 " + context.getString(R.string.counter);
            else
                countLabel = String.valueOf(folder.getCounters().size()) + " " + context.getString(R.string.counters);
            folderViewHolder.countTextView.setText(countLabel);
        }

        folderViewHolder.editCounterImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mFolderListener.onFolderShowMenu(v, folder, position);
            }
        });
        folderViewHolder.row.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mFolderListener.onClick(folder);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mFolders.size();
    }

    public void replaceData(List<Folder> folders)
    {
        mFolders = folders;
        notifyDataSetChanged();
    }

    public void addData(Folder folder)
    {
        mFolders.add(folder);
        notifyDataSetChanged();
    }

    public Folder getFolder(int position)
    {
        return mFolders.get(position);
    }

    class FolderViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameTextView;
        TextView countTextView;
        ImageView editCounterImageView;
        View row;

        FolderViewHolder(View itemView)
        {
            super(itemView);

            row = itemView;
            nameTextView = itemView.findViewById(R.id.name_text_view);
            countTextView = itemView.findViewById(R.id.count_text_view);
            editCounterImageView = itemView.findViewById(R.id.edit_counter_image_view);
        }
    }
}
