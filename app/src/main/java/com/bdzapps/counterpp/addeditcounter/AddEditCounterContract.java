package com.bdzapps.counterpp.addeditcounter;

import com.bdzapps.counterpp.BasePresenter;
import com.bdzapps.counterpp.BaseView;
import com.bdzapps.counterpp.data.model.Counter;
import com.bdzapps.counterpp.data.model.Folder;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditCounterContract
{
    interface View extends BaseView<Presenter>
    {
        void processFolders(List<Folder> folders);

        void saveCounter();

        void setColor(String color);

        void setGroup(Folder folder);

        void setLimit(int limit);

        void setNote(String note);

        void setName(String name);

        void setStep(int step);

        void showColorPickerUi();

        void showCountersList();
    }

    interface Presenter extends BasePresenter
    {
        Counter getCounter();

        void loadFolders();

        void saveCounter();

        void saveFolder(Folder folder);

        void populateCounter();
    }
}
