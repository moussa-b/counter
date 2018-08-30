package com.mbo.counter.folderlist;

import android.view.View;

import com.mbo.counter.data.model.Folder;

public interface FolderItemListener
{
    void onClick(Folder clickedFolder);

    void onFolderShowMenu(View view, Folder clickedFolder, int groupPosition);

    void onItemMove(Folder folder);
}
