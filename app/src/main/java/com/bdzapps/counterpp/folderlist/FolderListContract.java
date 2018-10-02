package com.bdzapps.counterpp.folderlist;

import com.bdzapps.counterpp.BasePresenter;
import com.bdzapps.counterpp.BaseView;
import com.bdzapps.counterpp.data.model.Folder;

import java.util.List;

public interface FolderListContract
{
    interface View extends BaseView<FolderListContract.Presenter>
    {
        void showCountersInFolder(Folder folder);

        void showFolders(List<Folder> counters);

        void showFolderStatisticsUi(int folderId);

        void showNoFolders();

        void renameFolder(int folderPosition);
    }

    interface Presenter extends BasePresenter
    {
        void deleteFolder(int folderId);

        void deleteCountersInGroup(int folderId);

        void duplicateFolder(int folderId);

        void loadFolders();

        void resetCountersInFolder(int folderId);

        void saveFolder(Folder folder);
    }
}
