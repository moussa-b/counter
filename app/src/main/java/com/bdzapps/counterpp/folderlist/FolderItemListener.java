package com.bdzapps.counterpp.folderlist;

import android.view.View;

import com.bdzapps.counterpp.data.model.Folder;

public interface FolderItemListener
{
    void onClick(Folder clickedFolder);

    void onFolderShowMenu(View view, Folder clickedFolder, int groupPosition);

    void onItemMove(Folder folder);
}
