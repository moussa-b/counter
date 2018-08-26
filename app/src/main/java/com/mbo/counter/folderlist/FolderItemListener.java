package com.mbo.counter.folderlist;

import android.view.View;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Folder;

public interface FolderItemListener
{
    void onCounterDecrement(int groupPosition, int childPosition, int counterId);

    void onCounterIncrement(int groupPosition, int childPosition, int counterId);

    void onCounterShowMenu(View view, Counter clickedCounter, int groupPosition, int childPosition);

    void onFolderShowMenu(View view, Folder clickedFolder, int groupPosition);
}
