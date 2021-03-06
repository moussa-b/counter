package com.bdzapps.counterpp.folderlist;

import android.support.annotation.NonNull;

import com.bdzapps.counterpp.data.model.Folder;
import com.bdzapps.counterpp.data.source.CounterDataSource;
import com.bdzapps.counterpp.data.source.ormlite.OrmLiteDataSource;

import java.util.List;

public class FolderListPresenter implements FolderListContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;

    @NonNull
    private final FolderListContract.View mFolderListView;

    public FolderListPresenter(@NonNull OrmLiteDataSource counterDataSource, @NonNull FolderListContract.View counterListView)
    {
        this.mCounterDataSource = counterDataSource;
        this.mFolderListView = counterListView;
        mFolderListView.setPresenter(this);
    }

    @Override
    public void start()
    {
        loadFolders();
    }

    @Override
    public void deleteFolder(int folderId)
    {
        mCounterDataSource.deleteFolder(folderId);
        loadFolders();
    }

    @Override
    public void deleteCountersInGroup(int folderId)
    {
        mCounterDataSource.deleteCountersInGroup(folderId);
        loadFolders();
    }

    @Override
    public void duplicateFolder(int folderId)
    {
        mCounterDataSource.duplicateFolder(folderId);
        loadFolders();
    }

    @Override
    public void loadFolders()
    {
        mCounterDataSource.getFolders(new CounterDataSource.LoadFoldersCallback()
        {
            @Override
            public void onFoldersLoaded(List<Folder> folders)
            {
                processFolders(folders);
            }

            @Override
            public void onDataNotAvailable()
            {

            }
        });
    }

    @Override
    public void resetCountersInFolder(int folderId)
    {
        mCounterDataSource.resetCountersInFolder(folderId);
        loadFolders();
    }

    @Override
    public void saveFolder(Folder folder)
    {
        mCounterDataSource.saveFolder(folder);
        loadFolders();
    }

    private void processFolders(List<Folder> folders)
    {
        if (folders.isEmpty())
        {
            // Show a message indicating there are no counter groups
            processEmptyCounters();
        }
        else
        {
            // Show the list of counter groups
            mFolderListView.showFolders(folders);
        }
    }

    private void processEmptyCounters()
    {
        mFolderListView.showNoFolders();
    }
}
