package com.mbo.counter.addeditcounter;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditCounterContract
{

    interface View extends BaseView<Presenter>
    {

    }

    interface Presenter extends BasePresenter
    {
        void saveCounter(String title, int count, String direction, String note, String color);
    }
}
