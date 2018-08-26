package com.mbo.counter.folderlist;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Folder;
import com.mbo.counter.data.source.CounterDataSource;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

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
    public void decrementCounter(final int groupPosition, final int childPosition, final int counterId)
    {
        mFolderListView.setCount(groupPosition, childPosition, mCounterDataSource.decrementCounter(counterId));
    }

    @Override
    public void deleteCounter(int counterId)
    {
        mCounterDataSource.deleteCounter(counterId);
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
    public void duplicateCounter(int counterId)
    {
        mCounterDataSource.duplicateCounter(counterId);
        loadFolders();
    }

    @Override
    public void duplicateFolder(int folderId)
    {
        mCounterDataSource.duplicateFolder(folderId);
        loadFolders();
    }

    @Override
    public void incrementCounter(final int groupPosition, final int childPosition, final int counterId)
    {
        mFolderListView.setCount(groupPosition, childPosition, mCounterDataSource.incrementCounter(counterId));
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
    public void resetCounter(final int groupPosition, final int childPosition, final int counterId)
    {
        mCounterDataSource.resetCounter(counterId);
        mFolderListView.setCount(groupPosition, childPosition, 0);
    }

    @Override
    public void resetCountersInGroup(int folderId)
    {
        mCounterDataSource.resetCountersInGroup(folderId);
        loadFolders();
    }

    @Override
    public void saveFolder(Folder folder)
    {
        mCounterDataSource.saveFolder(folder);
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
