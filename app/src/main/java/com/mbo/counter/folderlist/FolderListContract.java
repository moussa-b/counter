package com.mbo.counter.folderlist;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.Folder;

import java.util.List;

public interface FolderListContract
{
    interface View extends BaseView<FolderListContract.Presenter>
    {
        void showCountersInFolder(Folder folder);

        void showFolders(List<Folder> counters);

        void showNoFolders();

        void renameFolder(int folderPosition);
    }

    interface Presenter extends BasePresenter
    {
        void deleteFolder(int folderId);

        void deleteCountersInGroup(int folderId);

        void duplicateFolder(int folderId);

        void loadFolders();

        void resetCountersInGroup(int folderId);

        void saveFolder(Folder folder);
    }
}
