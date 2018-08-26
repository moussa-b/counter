package com.mbo.counter.folderlist;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.Folder;

import java.util.List;

public interface FolderListContract
{
    interface View extends BaseView<FolderListContract.Presenter>
    {
        void setCount(int groupPosition, int childPosition, int count);

        void showFolders(List<Folder> counters);

        void showCounterUi(int counterId);

        void showEditCounterUi(int counterId);

        void showNoFolders();

        void showCounterStatisticsUi(int counterId);

        void renameFolder(int groupPosition);
    }

    interface Presenter extends BasePresenter
    {
        void decrementCounter(final int groupPosition, final int childPosition, final int counterId);

        void deleteCounter(int counterId);

        void deleteFolder(int folderId);

        void deleteCountersInGroup(int folderId);

        void duplicateCounter(int counterId);

        void duplicateFolder(int folderId);

        void incrementCounter(final int groupPosition, final int childPosition, final int counterId);

        void loadFolders();

        void resetCounter(int groupPosition, int childPosition, int counterId);

        void resetCountersInGroup(int folderId);

        void saveFolder(Folder folder);
    }
}
