package com.bdzapps.counterpp.countersettings;

import com.bdzapps.counterpp.BasePresenter;
import com.bdzapps.counterpp.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CounterSettingsContract
{
    interface View extends BaseView<Presenter>
    {

    }

    interface Presenter extends BasePresenter
    {

    }
}
