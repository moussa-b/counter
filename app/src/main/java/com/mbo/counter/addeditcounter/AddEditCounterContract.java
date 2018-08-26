package com.mbo.counter.addeditcounter;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Folder;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditCounterContract
{
    interface View extends BaseView<Presenter>
    {
        void processCounterGroups(List<Folder> folders);

        void saveCounter();

        void setColor(String color);

        void setGroup(Folder folder);

        void setLimit(int limit);

        void setNote(String note);

        void setName(String name);

        void setStep(int step);

        void showCountersList();
    }

    interface Presenter extends BasePresenter
    {
        Counter getCounter();

        void loadCounterGroups();

        void saveCounter();

        void saveCounterGroup(Folder folder);

        void populateCounter();
    }
}
